package ru.yundon.shoppinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.yundon.shoppinglist.entities.LibraryItem
import ru.yundon.shoppinglist.entities.NoteItem
import ru.yundon.shoppinglist.entities.ShoppingListItem
import ru.yundon.shoppinglist.entities.ShopListNameItem

@Database(entities = [LibraryItem::class, NoteItem::class, ShoppingListItem::class, ShopListNameItem::class], version = 1)
abstract class MainDataBase: RoomDatabase() {

    abstract fun getDaoShoppingList(): DaoShoppingList

    companion object{

        @Volatile
        private var INSTANCE: MainDataBase? = null

        fun getDataBase(context: Context): MainDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase:: class.java,
                    "shopping_list_db"
                ).build()
                instance
            }
        }
    }
}