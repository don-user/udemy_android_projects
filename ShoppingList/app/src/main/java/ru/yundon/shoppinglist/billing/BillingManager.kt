package ru.yundon.shoppinglist.billing

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*

//класс для втроенных покупок
class BillingManager(val activity: AppCompatActivity){
    private var bClient: BillingClient? = null

    init {
        setupBillingClient()
    }
    private fun setupBillingClient(){
        bClient = BillingClient
            .newBuilder(activity)
            .setListener(getPurchaseListener())
            .enablePendingPurchases()
            .build()
    }
//сохраняет данные по встроенной покепке (отмена рекламы)
    private fun savePref(isPurchase: Boolean){
        val pref = activity.getSharedPreferences(MAIN_PREF, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(REMOVE_ADS_KEY, isPurchase)
        editor.apply()
    }
//функция для слушателя (setListener())
    private fun getPurchaseListener():PurchasesUpdatedListener{
    //лямбда функция подтверждение успешного ответа
        return PurchasesUpdatedListener { bResult, list ->
            if (bResult.responseCode == BillingClient.BillingResponseCode.OK){
                list?.get(0)?.let { nonConsumableItem(it) }
            }
        }
    }
//запускает втроенные покупки соеденяется с PLAY STORE, выдает списко строенных покупок
    fun startConnection(){
        bClient?.startConnection(object : BillingClientStateListener{

            //при дисконекте или ошибке
            override fun onBillingServiceDisconnected() {

            }

            override fun onBillingSetupFinished(p0: BillingResult) {
            }

        })
    }

    //запуска и получения встроенной покупки
    private fun getItem(){
        val skuList = mutableListOf<String>()
        skuList.add(REMOVE_AD_ITEM)
        val skuDetails = SkuDetailsParams
            .newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
        bClient?.querySkuDetailsAsync(skuDetails.build()){ bResult, list ->
            run {
                if (bResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if(list != null && list.isNotEmpty()){
                        val bFlowParams = BillingFlowParams
                            .newBuilder()
                            .setSkuDetails(list[0])
                            .build()
                        bClient?.launchBillingFlow(activity, bFlowParams)
                    }
                }
            }
        }

    }

    //функция покупки отказа от рекламы насовсем
    private fun nonConsumableItem(purchase: Purchase){
        //проверка успешности покупки
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED){
            if (!purchase.isAcknowledged){
                val acParams = AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                bClient?.acknowledgePurchase(acParams){
                    if (it.responseCode == BillingClient.BillingResponseCode.OK){
                        savePref(true)
                    //сделать сообщение спасибо за покупку
                        Toast.makeText(activity, "Спасибо за покупку", Toast.LENGTH_LONG).show()
                    } else {
                        savePref(false)
                        Toast.makeText(activity, "Неудалось провести покупку", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    //закрывает соеденение
    fun closeConnection(){
        bClient?.endConnection()
    }

    companion object{
        const val REMOVE_AD_ITEM = "remove_ad_item_id" //Это название нужно добавлять на PLAY CONSOLE где будет загружаться приложение
        const val MAIN_PREF = "main_pref"
        const val REMOVE_ADS_KEY = "remove_ads_key"
    }
}