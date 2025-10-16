

package edu.ucne.esmailin_martinez_ap2_p1.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.TicTacToePlay.tareas.local.database.EntradaHuacalesDB
import edu.ucne.esmailin_martinez_ap2_p1.data.local.dao.EntradaHuacalesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): EntradaHuacalesDB {
        return Room.databaseBuilder(
            context,
            EntradaHuacalesDB::class.java,
            "Entrada.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideEntradaHuacalesDao(appDatabase: EntradaHuacalesDB): EntradaHuacalesDao {
        return appDatabase.entradaHuacalesDao()
    }
}
