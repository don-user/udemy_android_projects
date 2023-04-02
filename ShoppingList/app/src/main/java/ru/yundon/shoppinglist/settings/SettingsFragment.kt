package ru.yundon.shoppinglist.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ru.yundon.shoppinglist.R
import ru.yundon.shoppinglist.billing.BillingManager


//класс настройки удаления рекламы (remove_ads)

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var removeAdsPref: Preference
    private lateinit var bManager: BillingManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        init()
    }

    private fun init(){
        bManager = BillingManager(activity as AppCompatActivity)
        removeAdsPref = findPreference<Preference>("remove_ads_key")!!
        removeAdsPref.setOnPreferenceClickListener {
            bManager.startConnection()
            true
        }
    }

    override fun onDestroy() {
        bManager.closeConnection()
        super.onDestroy()
    }
}