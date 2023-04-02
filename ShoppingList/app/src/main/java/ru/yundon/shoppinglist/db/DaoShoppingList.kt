package ru.yundon.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.yundon.shoppinglist.entities.LibraryItem
import ru.yundon.shoppinglist.entities.NoteItem
import ru.yundon.shoppinglist.entities.ShopListNameItem
import ru.yundon.shoppinglist.entities.ShoppingListItem

@Dao
interface DaoShoppingList {

    //получение данных из таблицы note_list
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    //получение данных из таблицы shopping_list_name
    @Query("SELECT * FROM shopping_list_name")
    fun getAllShopListName(): Flow<List<ShopListNameItem>>

    //получение данных из таблицы shopping_list_name
    @Query("SELECT * FROM shop_list_item WHERE listId LIKE :listId")
    fun getAllShopListItem(listId: Int): Flow<List<ShoppingListItem>>

    //получение данных из таблицы library
    @Query("SELECT * FROM library WHERE name LIKE :name")
    suspend fun getAllLibraryItems(name: String): List<LibraryItem>


    //добавление данных в таблицу note_list
    @Insert
    suspend fun insertNote(noteItem: NoteItem)

    //добавление данных в таблицу note_list
    @Insert
    suspend fun insertItem(shopListItem: ShoppingListItem)

    //добавление данных в таблицу library
    @Insert
    suspend fun insertLibraryItem(libraryItem: LibraryItem)

    //добавление данных из таблицы shopping_list_name
    @Insert
    suspend fun insertShopListName(nameItem: ShopListNameItem)

    //обновление данных в таблице note_list
    @Update
    suspend fun updateNote(noteItem: NoteItem)

    //обновление данных в таблице library
    @Update
    suspend fun updateLibraryItem(libraryItem: LibraryItem)

    //обновление данных в таблице ShoppingListItem
    @Update
    suspend fun updateListItem(listItem: ShoppingListItem)

    //обновление данных в таблице shopping_list_name
    @Update
    suspend fun updateShopListName(shopListNameItem: ShopListNameItem)

    //удаление данных из таблицы note_list
    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    //удаление данных из таблицы shopping_list_name
    @Query("DELETE FROM shopping_list_name WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)

    //удаление данных из таблицы shopping_list_name
    @Query("DELETE FROM shop_list_item WHERE listId LIKE :listId")
    suspend fun deleteShopItemsByListID(listId: Int)

    //удаление данных из таблицы library
    @Query("DELETE FROM library WHERE id IS :id")
    suspend fun deleteLibraryItem(id: Int)
}