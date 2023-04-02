package ru.yundon.shoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.yundon.shoppinglist.activities.MainApp
import ru.yundon.shoppinglist.activities.ShopListActivity
import ru.yundon.shoppinglist.activities.ShopListActivity.Companion.SHOP_LIST_NAME
import ru.yundon.shoppinglist.databinding.FragmentShopListNameBinding
import ru.yundon.shoppinglist.db.ShoppingListNameAdapter
import ru.yundon.shoppinglist.dialog.DeleteDialog
import ru.yundon.shoppinglist.dialog.NewListDialog
import ru.yundon.shoppinglist.entities.ShopListNameItem
import ru.yundon.shoppinglist.utils.TimeManager.getCurrentTime
import ru.yundon.shoppinglist.viewmodel.MainViewModel

class ShopListNamesFragment : BaseFragment(), ShoppingListNameAdapter.Listener {
    private lateinit var binding: FragmentShopListNameBinding
    private lateinit var adapter: ShoppingListNameAdapter
    //Лаунчер для отрытия и передачи данных в активити и результата от активити

    //настройка и покдлючение вьюмодель используюя глобальную датабэйз из класса MainApp
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).dataBase)
    }
    //обработка нажатия кнопки new из ботом навигэйшионвью в текущем фрагмента.
    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.ListenerDialog{
            override fun onClick(name: String) {
                val shopListName = ShopListNameItem(
                    null,
                    name,
                    getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShoppingListName(shopListName)
            }

        }, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observer()
    }

    override fun editItem(shopListNameItem: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.ListenerDialog{
            override fun onClick(name: String) {
                mainViewModel.updateShopListName(shopListNameItem.copy(name = name))
            }
        }, shopListNameItem.name)

    }

    //нажатиен а итем списка покупок
    override fun onClickItem(shopListNameItem: ShopListNameItem) {

        startActivity(Intent(activity, ShopListActivity::class.java).putExtra(SHOP_LIST_NAME, shopListNameItem))
    }

    //настройка и подключение ресайклервью
    private fun initRecyclerView() = with(binding){
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = ShoppingListNameAdapter(this@ShopListNamesFragment)
        recyclerView.adapter = adapter
    }

    //покдлючение наблюдателя по обновляемому списку для адаптера и подключение обновленного списка в адаптер
    private fun observer(){
        mainViewModel.allShopListNameItem.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }
//нажатие на кнопку удалить в дАлерт иалоге
    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.ListenerDialog{
            override fun onClick() {
                mainViewModel.deleteShopList(id, true)
            }

        })
    }
}