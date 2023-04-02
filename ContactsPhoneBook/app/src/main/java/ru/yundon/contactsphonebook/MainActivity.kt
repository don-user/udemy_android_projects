package ru.yundon.contactsphonebook

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //запрос разрешения на просмотр контактов
        val permissionGranted = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED  //проверяем условие PERMISSION_GRANTED (разрешение дано) или PERMISSION_DENIED(разрешение не дано)

        if (permissionGranted) requestContacts ()
        else requestPermissions()
    }

    //функция которая запрашивает все контакты
    private fun requestContacts (){
        thread {
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
            )

            while (cursor?.moveToNext() == true){
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val contact = Contacts(
                    id = id,
                    name = name
                )
                Log.d("MyLog", "contact $contact")
            }
            cursor?.close()
        }
    }

    //запрос на разрещение просмотра контактов
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_CONTACTS), //массив с разрещениями от пользователя
            READ_CONTACTS_RC
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_CONTACTS_RC && grantResults.isNotEmpty()){
            val permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (permissionGranted) requestContacts ()
            else Log.d("MyTag", "PERMISSION DENIED")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object{
        const val READ_CONTACTS_RC = 100
    }
}