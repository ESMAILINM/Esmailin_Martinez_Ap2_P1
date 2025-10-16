
package edu.ucne.esmailin_martinez_ap2_p1.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.esmailin_martinez_ap2_p1.data.repository.EntradaHuacalesRepositoryImpl
import edu.ucne.esmailin_martinez_ap2_p1.domain.repository.EntradaHuacalesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEntradaHuacalesRepository(
        entradaHuacalesRepositoryImpl: EntradaHuacalesRepositoryImpl
    ): EntradaHuacalesRepository
}
