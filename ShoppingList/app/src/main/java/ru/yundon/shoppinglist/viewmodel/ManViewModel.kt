package ru.yundon.shoppinglist.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.yundon.shoppinglist.db.MainDataBase
import ru.yundon.shoppinglist.entities.LibraryItem
import ru.yundon.shoppinglist.entities.NoteItem
import ru.yundon.shoppinglist.entities.ShopListNameItem
import ru.yundon.shoppinglist.entities.ShoppingListItem

class MainViewModel(database: MainDataBase): ViewModel() {
    //экземпляр класса дата бэйз
    val dao = database.getDaoShoppingList()
    //получение всех заметок из базы данных
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    //получение всех списков покупок из базы даных покупок
    val allShopListNameItem: LiveData<List<ShopListNameItem>> = dao.getAllShopListName().asLiveData()
//получаем список из библиотеки слов списка покупок
    val libraryItems = MutableLiveData<List<LibraryItem>>()

//получение списка покупок по лист айди
    fun getAllItemsFromList(listId: Int): LiveData<List<ShoppingListItem>>{
        return dao.getAllShopListItem(listId).asLiveData()
    }

    //получение списка слов из библотеки для лайфдаты
    fun getAllLibraryItems(name: String) = viewModelScope.launch{
        libraryItems.postValue(dao.getAllLibraryItems(name))

    }

    //функция для записи в таблицу Заметок
    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }
    //функция для записи в таблицу Списка покупок
    fun insertShoppingListName(nameItem: ShopListNameItem) = viewModelScope.launch {
        dao.insertShopListName(nameItem)
    }

    //функция для записи в таблицу бибилотеки>
    fun insertShoppingListItem(shopListItem: ShoppingListItem) = viewModelScope.launch {
        dao.insertItem(shopListItem)
        if (!isLibraryItemExists(shopListItem.name)) dao.insertLibraryItem(LibraryItem(null, shopListItem.name))
    }
//Обновление даных в таблице заметок
    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    //Обновление даных в таблице библиотеки
    fun updateLibraryItem(libraryItem: LibraryItem) = viewModelScope.launch {
        dao.updateLibraryItem(libraryItem)
    }

    //Обновление даных в таблице списка покупок
    fun updateListItem(listItem: ShoppingListItem) = viewModelScope.launch {
        dao.updateListItem(listItem)
    }

    //Обновление даных в таблице списка покупок
    fun updateShopListName(shopListNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.updateShopListName(shopListNameItem)
    }

//Удаление заметок
    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }
//удаление списка покупок через боковое меню и кнопку делет
    fun deleteShopList(id: Int, deleteList: Boolean) = viewModelScope.launch {

        if (deleteList) dao.deleteShopListName(id)

        dao.deleteShopItemsByListID(id)

    }

    fun deleteLibraryItem(id: Int) = viewModelScope.launch {
        dao.deleteLibraryItem(id)
    }

    private suspend fun isLibraryItemExists(name: String): Boolean{
        return dao.getAllLibraryItems(name).isNotEmpty()
    }

    class MainViewModelFactory(val database: MainDataBase): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}