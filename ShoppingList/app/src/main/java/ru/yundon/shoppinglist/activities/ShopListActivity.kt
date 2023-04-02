package ru.yundon.shoppinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.yundon.shoppinglist.R
import ru.yundon.shoppinglist.databinding.ActivityShopListBinding
import ru.yundon.shoppinglist.db.ShoppingListItemAdapter
import ru.yundon.shoppinglist.db.ShoppingListItemAdapter.Companion.ADD
import ru.yundon.shoppinglist.db.ShoppingListItemAdapter.Companion.CHECK_BOX
import ru.yundon.shoppinglist.db.ShoppingListItemAdapter.Companion.DELETE_LIBRARY_ITEM
import ru.yundon.shoppinglist.db.ShoppingListItemAdapter.Companion.EDIT
import ru.yundon.shoppinglist.db.ShoppingListItemAdapter.Companion.EDIT_LIBRARY_ITEM
import ru.yundon.shoppinglist.dialog.EditListItemDialog
import ru.yundon.shoppinglist.entities.LibraryItem
import ru.yundon.shoppinglist.entities.ShopListNameItem
import ru.yundon.shoppinglist.entities.ShoppingListItem
import ru.yundon.shoppinglist.utils.ShareHelper
import ru.yundon.shoppinglist.viewmodel.MainViewModel

class ShopListActivity : AppCompatActivity(), ShoppingListItemAdapter.Listener {

    private lateinit var binding: ActivityShopListBinding
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShoppingListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).dataBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initRecyclerView()
        listItemObserver()
    }

//настройка конпок меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //раздуваем xml файл меню
        menuInflater.inflate(R.menu.shop_list_menu, menu)
    //инициализируем кнопку сохранить
        saveItem = menu?.findItem(R.id.save_item)!!
        val newShopItem = menu.findItem(R.id.new_shop_item)
        edItem = newShopItem.actionView.findViewById(R.id.editTextNewShopItem) as EditText

       //присоеденнение слушателя к кнопке  new item menu
        newShopItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        textWatcher = textWatcherFun()
        return true
    }

    //функция которая следит за изменениеями в едит текст
    private fun textWatcherFun(): TextWatcher{
        return object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.getAllLibraryItems("%$p0%")
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
    }

//слушатель нажатия на кнопку сохранить в меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
        R.id.save_item -> addNewShopItem(edItem?.text.toString())
        R.id.delete_list -> {
            mainViewModel.deleteShopList(shopListNameItem?.id!!, true)
            finish()
        }
        R.id.clear_list -> mainViewModel.deleteShopList(shopListNameItem?.id!!, false)
        //Поделиться с писком, createChooser - создает выбор через кого отправлять whatsApp, mail и т.п.
        R.id.share_list -> startActivity(Intent.createChooser(
            ShareHelper.shareShopList(adapter?.currentList!!, shopListNameItem?.name!!),
            "Поделиться с помощью"
        ))
    }
        return super.onOptionsItemSelected(item)
    }


    //Функция для сохраниение слов в Список Покупок
    private fun addNewShopItem(name: String){
        if (name.isEmpty()) return

        val item = ShoppingListItem(
            null,
            name,
            null,
            false,
            shopListNameItem?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertShoppingListItem(item)
    }
    //наблюдатель который получает списко списка продуктов
    private fun listItemObserver(){
        mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).observe(this, {
            adapter?.submitList(it)
            binding.textViewEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun libraryItemObserver(){
        mainViewModel.libraryItems.observe(this, {
            //перегружаем список из LibraryItem в  ShoppingListItem
            val tempShoppingListItem = mutableListOf<ShoppingListItem>()
            it.forEach { item ->
                val shopItem = ShoppingListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1 //itemType = определяет какой список будет показывать адаптер 1 показывает юиюлиотекуу, 0 показывает список покупок
                )
                tempShoppingListItem.add(shopItem)
            }
            adapter?.submitList(tempShoppingListItem)
            binding.textViewEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun initRecyclerView() = with(binding){
        adapter = ShoppingListItemAdapter(this@ShopListActivity)
        recyclerViewShopListItem.layoutManager = LinearLayoutManager(this@ShopListActivity)
        recyclerViewShopListItem.adapter = adapter

    }
//функция по присваиванию слушателя к кнопки new item menu
    private fun expandActionView(): MenuItem.OnActionExpandListener{
        return object : MenuItem.OnActionExpandListener{
            //открывает едит текст в меню
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).removeObservers(this@ShopListActivity)
                mainViewModel.getAllLibraryItems("%%") //показывает все элементы из библиотеки когда едит текст пустой
                return true
            }
            //открывает едит текст в меню
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                //перересовываем меню из кнопок новый итем и сохранить
                invalidateOptionsMenu()
                mainViewModel.libraryItems.removeObservers(this@ShopListActivity)
                edItem?.setText("")
                listItemObserver()
                return true
            }
        }
    }

    //функция для инициализации приема информации из фрагмента (списка покупок)
    private fun init(){
        shopListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME) as ShopListNameItem
    }

    companion object{
        const val SHOP_LIST_NAME = "shop_list_name"
    }

    override fun onClickItem(shopListItem: ShoppingListItem, state: Int) {
        when(state){
            CHECK_BOX -> mainViewModel.updateListItem(shopListItem)
            EDIT -> {
                EditListItemDialog.showDialog(this, shopListItem, object : EditListItemDialog.ListenerDialog{
                    override fun onClick(listItem: ShoppingListItem) {
                        mainViewModel.updateListItem(listItem)
                    }
                })
            }
            EDIT_LIBRARY_ITEM -> {
                EditListItemDialog.showDialog(this, shopListItem, object : EditListItemDialog.ListenerDialog{
                    override fun onClick(listItem: ShoppingListItem) {
                        mainViewModel.updateLibraryItem(LibraryItem(listItem.id, listItem.name))
                        //обновлям ресайклер вью и выводим те же элементы которые были показаны до запроса
                        mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
                    }
                })
            }
            DELETE_LIBRARY_ITEM -> {
                mainViewModel.deleteLibraryItem(shopListItem.id!!)
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
            ADD -> addNewShopItem(shopListItem.name)
        }
    }

    private fun saveItemCount(){
        var checkedItemCounter = 0
        adapter?.currentList?.forEach {
            if (it.itemChecked) checkedItemCounter++

        }
        val tempShopListNameItem = shopListNameItem?.copy(
            allItemsCounter = adapter?.itemCount!!,
            checkedItemsCounter = checkedItemCounter
        )
        mainViewModel.updateShopListName(tempShopListNameItem!!)
    }

    override fun onBackPressed() {
        saveItemCount()
        super.onBackPressed()
    }
}