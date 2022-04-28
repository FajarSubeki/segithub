package segithub.id.app.application

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import segithub.id.BuildConfig
import segithub.id.app.module.githubApiClientModule
import segithub.id.app.module.githubApiModule
import segithub.id.app.module.usersListDataSourceFactory
import segithub.id.app.module.usersListViewModel
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MyApp)
            modules(
                listOf(
                    usersListViewModel,
                    githubApiModule,
                    githubApiClientModule,
                    usersListDataSourceFactory,
                )
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}