package robi.saputra.nutapos.di

import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import robi.saputra.nutapos.data.AppDatabase
import robi.saputra.nutapos.repository.FinanceRepository
import robi.saputra.nutapos.repository.FinanceRepositoryImpl
import robi.saputra.nutapos.repository.FinanceUseCase
import robi.saputra.nutapos.ui.FinanceViewModel

val AppModule = module {
    single { Room.databaseBuilder( get(), AppDatabase::class.java,"nuta_pos").build() }
}

val DaoModule = module {
    single { get<AppDatabase>().FinanceInDao() }
}

val RepositoryModule = module {
    single<FinanceRepository> { FinanceRepositoryImpl(get()) }
}

val FactoryModule = module  {
    factory { FinanceUseCase(get()) }
}

val ViewModelModule = module {
    viewModel { FinanceViewModel(get()) }
}