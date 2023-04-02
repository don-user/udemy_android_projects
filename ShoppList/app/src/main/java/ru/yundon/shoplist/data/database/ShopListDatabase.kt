package ru.yundon.shoplist.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemEntity::class], version = 1, exportSchema = false)
abstract class ShopListDatabase : RoomDatabase(){

    abstract fun shopListDao(): ShopListDao

    companion object{

        private var INSTANCE: ShopListDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "shop_items"

        fun getInstance(application: Application): ShopListDatabase{

            INSTANCE?.let {
                return it
            }

            synchronized(LOCK){
                INSTANCE?.let {
                    return it
                }

                val db = Room.databaseBuilder(
                    application,
                    ShopListDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}