package uz.gita.mynotesapplication.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : T)

    @Update
    fun update(data : T)

    @Delete
    fun delete(data :T)


}