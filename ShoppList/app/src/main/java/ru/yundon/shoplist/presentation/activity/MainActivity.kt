package ru.yundon.shoplist.presentation.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.yundon.shoplist.R
import ru.yundon.shoplist.databinding.ActivityMainBinding
import ru.yundon.shoplist.domain.model.ShopItem
import ru.yundon.shoplist.presentation.ShopItemApp
import ru.yundon.shoplist.presentation.adapter.ShopListAdapter
import ru.yundon.shoplist.presentation.adapter.ShopListAdapter.Companion.MAX_PULL_SIZE
import ru.yundon.shoplist.presentation.adapter.ShopListAdapter.Companion.TYPE_VIEW_DISABLED
import ru.yundon.shoplist.presentation.adapter.ShopListAdapter.Companion.TYPE_VIEW_ENABLED
import ru.yundon.shoplist.presentation.fragment.ShopItemFragment
import ru.yundon.shoplist.presentation.viewmodels.MainViewModel
import ru.yundon.shoplist.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject
import kotlin.concurrent.thread

class MainActivity: AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapterShopList: ShopListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as ShopItemApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.shopList.observe(this) {
            adapterShopList.submitList(it)
        }

        binding.buttonAddShopItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity().newIntentAddItem(this)
                startActivity(intent)
            }else{
                launchFragment(ShopItemFragment.newInstanceAddNewItem())
            }
        }
        //необходимо запустить не на главном потоке, пока без корутин, надо через корутины попробовать
        thread {
            val cursor = contentResolver.query(
                Uri.parse("content://ru.yundon.shoplist/shop_items"),
                null,
                null,
                null,
                null,
                null,
            )
            //проверяем на true если тру, то записи есть и переходим по всем записям
            while (cursor?.moveToNext() == true){
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val count = cursor.getInt(cursor.getColumnIndexOrThrow("count"))
                val enable = cursor.getInt(cursor.getColumnIndexOrThrow("enable")) > 0 // если больше 0 то получаем true, если меньше 0 то false
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enable = enable
                )
                Log.d("MyTag", "$shopItem")
            }
            cursor?.close()  //обязательно надо курсор закрывать
        }
    }

    //проверяем если shopItemContainer равен null то вертикальный вид в 1 колонку, если нет то горизонтальный в 2 колонки
    private fun isOnePaneMode(): Boolean{
        return binding.shopItemContainer == null
    }
    //запуск верного контейнера фрагмента
    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() = with(binding.rvShopList){

        adapterShopList = ShopListAdapter()
        adapter = adapterShopList

        //recycledViewPool определяет количество неиспользуемых элементов для каждого типа
        recycledViewPool.setMaxRecycledViews(TYPE_VIEW_ENABLED, MAX_PULL_SIZE)
        recycledViewPool.setMaxRecycledViews(TYPE_VIEW_DISABLED, MAX_PULL_SIZE)
        //вызов лямба функции из адаптера
        setupOnShopItemLongClickListener()
        setupClickListener()
        setupSwipeTouchListener(this)
    }


    //функция свайпа
    private fun setupSwipeTouchListener(rvShopItem: RecyclerView) {
        val swipeCallback = object :
            ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapterShopList.currentList[viewHolder.adapterPosition]
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteShopListItem(item)
                    }
                    ItemTouchHelper.RIGHT -> {

                        //проводим не в майн потоке
                        thread {
                            //вызываем функцию делет у contentResolver и передаем массив
                            contentResolver.delete(
                                Uri.parse("content://ru.yundon.shoplist/shop_items"),
                                null,
                                arrayOf(item.id.toString())
                            )
                        }
//                        viewModel.deleteShopListItem(item)
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(rvShopItem)
    }

    private fun setupClickListener() {
        adapterShopList.onShopItemClickListener = {
            if(isOnePaneMode()){
                val intent = ShopItemActivity().newIntentEditItem(this, it.id)
                startActivity(intent)
            } else launchFragment(ShopItemFragment.newInstanceEditNewItem(it.id))


            //Toast.makeText(this@MainActivity, "TEST", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupOnShopItemLongClickListener() {
        adapterShopList.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }
}
