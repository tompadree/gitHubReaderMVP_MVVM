package com.githubreader.di

import androidx.fragment.app.FragmentActivity
import androidx.multidex.BuildConfig
import androidx.room.Room
import com.githubreader.utils.helpers.dialogs.DialogManager
import com.githubreader.utils.helpers.dialogs.DialogManagerImpl
import com.githubreader.utils.network.*
import com.githubreader.data.source.GitHubResultsDataSource
import com.githubreader.data.source.GitHubResultsRepository
import com.githubreader.data.source.GitHubResultsRepositoryImpl
import com.githubreader.data.source.local.GitHubDatabase
import com.githubreader.data.source.local.GitHubResultsLocalDataSource
import com.githubreader.data.source.remote.GitHubResultsRemoteDataSource
import com.githubreader.data.source.remote.api.APIConstants
import com.githubreader.data.source.remote.api.GithubApi
import com.githubreader.gitresults.GitResultsViewModel
import com.githubreader.gitresultsdetails.GitResultsDetailsViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Tomislav Curis
 */


val AppModule = module {
    factory { (activity: FragmentActivity) -> DialogManagerImpl(activity) as DialogManager }
}

val DataModule = module {

    single { Room.databaseBuilder(androidContext(), GitHubDatabase::class.java, "github_db").build() }
    single  { get<GitHubDatabase>().getGitHubDao() }

    single { Dispatchers.IO }

    single(named("local")) { GitHubResultsLocalDataSource(get(), get()) as GitHubResultsDataSource }
    single(named("remote")) { GitHubResultsRemoteDataSource(get()) as GitHubResultsDataSource }

    viewModel { GitResultsViewModel(get(), get()) }
    viewModel { GitResultsDetailsViewModel(get(), get()) }
}

val RepoModule = module {
    single { GitHubResultsRepositoryImpl(get(qualifier = named("local")),
        get(qualifier = named("remote"))) as GitHubResultsRepository }
}


val NetModule = module {

    single { InternetConnectionManagerImpl() as InternetConnectionManager }

    single {

        OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
//            .addInterceptor(NetworkExceptionInterceptor())
            .addInterceptor(ResponseInterceptor(get())).apply {
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()
    }

    single {
        (Retrofit.Builder()
            .baseUrl(APIConstants.BASE_URL)
            .client(get())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(RxThreadCallAdapter(Schedulers.io()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GithubApi::class.java)) as GithubApi
    }
}