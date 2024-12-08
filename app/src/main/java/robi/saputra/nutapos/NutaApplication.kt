package robi.saputra.nutapos

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import robi.saputra.nutapos.di.AppModule
import robi.saputra.nutapos.di.DaoModule
import robi.saputra.nutapos.di.FactoryModule
import robi.saputra.nutapos.di.RepositoryModule
import robi.saputra.nutapos.di.ViewModelModule

class NutaApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this
        context = applicationContext

        startKoin {
            androidLogger()
            androidContext(this@NutaApplication)
            modules(
                listOf(
                    AppModule,
                    DaoModule,
                    RepositoryModule,
                    FactoryModule,
                    ViewModelModule,
                )
            )
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: NutaApplication private set

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context private set
    }
}