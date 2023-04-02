package ru.yundon.shoplist.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.yundon.shoplist.R
import ru.yundon.shoplist.databinding.ActivityShopItemBinding
import ru.yundon.shoplist.domain.model.ShopItem.Companion.UNDEFINED_ID
import ru.yundon.shoplist.presentation.fragment.ShopItemFragment

class ShopItemActivity : AppCompatActivity() {

    private val  binding by lazy { ActivityShopItemBinding.inflate(layoutInflater) }

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        parseIntent()
        //если фрагмент не создан создает фрагмент, если создан то не пересоздает
        if (savedInstanceState == null) launchRightMode()
    }

    private fun launchRightMode(){
        val fragment = when(screenMode){
            MODE_EDIT -> ShopItemFragment.newInstanceEditNewItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddNewItem()
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
    supportFragmentManager.beginTransaction()
        .replace(R.id.shop_item_container, fragment)
        .commit()
    }

    //проверка параметров которые передаются через интент
    private fun parseIntent(){
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) throw RuntimeException("Param screen mode is absent")

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) throw RuntimeException("Unknown screen mode $mode")

        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item ID is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }

    fun newIntentAddItem(context: Context): Intent{
        val intent = Intent(context, ShopItemActivity::class.java)
        intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
        return intent
    }

    fun newIntentEditItem(context: Context, shopItemId: Int): Intent{
        val intent = Intent(context, ShopItemActivity::class.java)
        intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
        intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)

        return intent
    }

    companion object{
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""
    }
}