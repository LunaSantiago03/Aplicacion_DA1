package com.example.retrofit_da1.Data.LocalDataBase.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.retrofit_da1.Model.CategorySingle
import com.google.gson.annotations.SerializedName

@Entity(
   tableName = "Products",
   foreignKeys = [
      ForeignKey(
         entity = CategorySingleLocal::class,
         parentColumns = ["id"],
         childColumns = ["categoryId"],
         onDelete = ForeignKey.CASCADE
      )
   ]
)
data class ProductDetailLocal (
   @PrimaryKey val id: Int,
   val title: String,
   val price: String,
   val description: String,
   val categoryId: Int,
   val images: String
)