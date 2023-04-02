package ru.yundon.shoppinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import ru.yundon.shoppinglist.R
import ru.yundon.shoppinglist.billing.BillingManager.Companion.MAIN_PREF
import ru.yundon.shoppinglist.billing.BillingManager.Companion.REMOVE_ADS_KEY
import ru.yundon.shoppinglist.databinding.ActivityMainBinding
import ru.yundon.shoppinglist.dialog.NewListDialog
import ru.yundon.shoppinglist.fragments.FragmentManager
import ru.yundon.shoppinglist.fragments.NoteFragment
import ru.yundon.shoppinglist.fragments.ShopListNamesFragment
import ru.yundon.shoppinglist.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.ListenerDialog {

    lateinit var binding: ActivityMainBinding
    private var currentMenuItemId = R.id.shop_list
    private lateinit var defPreferences: SharedPreferences  //настройка цвета темы
    private var currentTheme = ""
    private var iAd: InterstitialAd? = null //переменная класса для показа Interstitial рекламы (на весь экран картинка)
    private var adShowCnt = 0
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        defPreferences = PreferenceManager.getDefaultSharedPreferences(this) //инициализируем настройку темы
        currentTheme = defPreferences.getString("theme_key", "blue").toString()
        //настраиваем и выбираем цвет темы до выбора экрана
        setTheme(getSelectedTheme())

        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(MAIN_PREF, MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()
        //условия запуска рекламы
        if (!pref.getBoolean(REMOVE_ADS_KEY, false)) loadInterAd()
    }
//функция для запуска рекламы
    private fun loadInterAd(){
        val request = AdRequest.Builder().build() //запрос для получения рекламы
        InterstitialAd.load(this, getString(R.string.inter_ad_id), request, object : InterstitialAdLoadCallback(){
            ///функция по успешной загрузки объявления
            override fun onAdLoaded(ad: InterstitialAd) {
                iAd = ad
            }
            override fun onAdFailedToLoad(p0: LoadAdError) {
                iAd = null
            }
        })
    }
    //Функция которая показывает рекламу
    private fun showInterAd(adListener: AdListener){
        if(iAd != null || iAd != null && adShowCnt > 3){
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                //выполняется после закрытия рекламы
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }
                //если происходит какая-то ошибка
                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                }
                //запускается после пролного просмотра рекламы
                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
            }
            iAd?.show(this) //показывает объявление
        }else{
            adListener.onFinish()
        }
    }


//обработка клика на конпку создать список в Алерт Диалог
    override fun onClick(name: String) {
        Log.d("MyLog", "$name")
    }

//установка и обработка клика по копкам BottomNavigationView
    private fun setBottomNavListener(){
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings -> {
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        }
                    })
                }
                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list -> {
                    if (adShowCnt < 3) {
                        adShowCnt++
                        currentMenuItemId = R.id.shop_list
                        FragmentManager.setFragment(ShopListNamesFragment.newInstance(),this@MainActivity)
                    } else {
                        adShowCnt = 0
                        showInterAd(object : AdListener {
                            override fun onFinish() {
                                currentMenuItemId = R.id.shop_list
                                FragmentManager.setFragment(ShopListNamesFragment.newInstance(),this@MainActivity)
                            }
                        })
                    }

                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.selectedItemId = currentMenuItemId

        if (defPreferences.getString("theme_key", "blue") != currentTheme) recreate()
    }
//функция по выбору цвета темы
    private fun getSelectedTheme(): Int{
        return if (defPreferences.getString("theme_key", "blue") == "blue") R.style.Theme_ShoppingListBlue
        else R.style.Theme_ShoppingLisOrange
    }

    //интерфейс для того чтоб следить за просмотром рекламы
    interface AdListener{
        fun onFinish()
    }
}