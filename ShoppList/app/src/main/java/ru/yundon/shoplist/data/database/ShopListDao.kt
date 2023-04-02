package ru.yundon.shoplist.data.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_items")
    fun getShopList(): LiveData<List<ShopItemEntity>>

    @Query("SELECT * FROM shop_items")
    fun getShopListCursor(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItemEntity: ShopItemEntity)

    //для добавление через контент провайдер, без корутин, потому что пока не реализовано через корутины
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShopItemContentProvider(shopItemEntity: ShopItemEntity)

    @Query("DELETE FROM shop_items WHERE id=:shopItemId")
    suspend fun deleteShopItem(shopItemId: Int)

    @Query("DELETE FROM shop_items WHERE id=:shopItemId")
    fun deleteShopItemContentProvider(shopItemId: Int): Int

    @Query("SELECT * FROM shop_items WHERE id=:shopItemId LIMIT 1")
    suspend fun getShopItem(shopItemId: Int): ShopItemEntity
}