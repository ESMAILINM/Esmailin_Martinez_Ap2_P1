package edu.ucne.TicTacToePlay.tareas.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
//import androidx.room.vo.Database
import edu.ucne.esmailin_martinez_ap2_p1.data.local.dao.EntradaHuacalesDao
import edu.ucne.esmailin_martinez_ap2_p1.data.local.entiti.EntradaHuacalesEntity


@Database(
    entities = [
        EntradaHuacalesEntity::class

    ],
    version = 3,
    exportSchema = false,
)
abstract class EntradaHuacalesDB : RoomDatabase() {
    abstract fun entradaHuacalesDao(): EntradaHuacalesDao

}