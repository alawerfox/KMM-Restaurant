package restaurant.kmm.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import restaurant.kmm.androidModule
import restaurant.kmm.platformModule

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            logger(AndroidLogger())
            modules(
                platformModule,
                androidModule
            )
        }
    }
}