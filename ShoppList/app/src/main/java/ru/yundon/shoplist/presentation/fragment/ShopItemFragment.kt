package ru.yundon.shoplist.presentation.fragment

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.yundon.shoplist.databinding.FragmentShopItemBinding
import ru.yundon.shoplist.domain.model.ShopItem.Companion.UNDEFINED_ID
import ru.yundon.shoplist.presentation.ShopItemApp
import ru.yundon.shoplist.presentation.viewmodels.ShopItemViewModel
import ru.yundon.shoplist.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject
import kotlin.concurrent.thread

class ShopItemFragment: Fragment() {

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("Param screen name is absent")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModels by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
    }
    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = UNDEFINED_ID

   private val component by lazy {
       (requireActivity().application as ShopItemApp).component
   }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModels
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() = with(viewModels){

        shouldCloseScreen.observe(viewLifecycleOwner){
            activity?.onBackPressed()
        }
    }
    private fun addTextChangeListeners(){
        binding.edName.addTextChangedListener ( object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModels.resetErrorInputName()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.edCount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModels.resetErrorInputCount()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun launchRightMode(){
        when(screenMode){
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() = with(binding) {

        viewModels.getShopItemById(shopItemId)
        btSave.setOnClickListener {
            viewModels.editShopItem(edName.text?.toString(), edCount.text?.toString())
//
//            thread {
//                context?.contentResolver?.update(
//                    Uri.parse("content://ru.yundon.shoplist/shop_items"),
//                    ContentValues().apply {
//                        put("id", 0)
//                        put("name", edName.text?.toString())
//                        put("count", edCount.text?.toString()?.toInt())
//                        put("enable", true)
//                    },
//                    null,
//                    arrayOf()
//                )
//            }
        }
    }

    private fun launchAddMode() = with(binding) {
        btSave.setOnClickListener {
//            Toast.makeText(context, "BUTTON", Toast.LENGTH_SHORT).show()
            viewModels.addShopItem(edName.text?.toString(), edCount.text?.toString())

//  Код для передачи ContentValues (данные для добавления в базу  контент провайдере)
//            thread {
//                context?.contentResolver?.insert(
//                    Uri.parse("content://ru.yundon.shoplist/shop_items"),
//                    ContentValues().apply {
//                        put("id", 0)
//                        put("name", edName.text?.toString())
//                        put("count", edCount.text?.toString()?.toInt())
//                        put("enable", true)
//                    }
//                )
//            }
        }
    }
    //проверка параметров которые передаются через аргументы
    private fun parseParams(){
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) throw RuntimeException("Param screen mode is absent")

        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) throw RuntimeException("Unknown screen mode $mode")

        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item ID is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }

    companion object{
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        //передача параметров в активити через аргументы
        fun newInstanceAddNewItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }
        //передача параметров в активити через аргументы
        fun newInstanceEditNewItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}
