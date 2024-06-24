package com.example.retrofit_da1.Data.LocalDataBase.Mapping

import com.example.retrofit_da1.Data.LocalDataBase.Entities.CategorySingleLocal
import com.example.retrofit_da1.Data.LocalDataBase.Entities.FavoriteProductLocal
import com.example.retrofit_da1.Data.LocalDataBase.Entities.ProductDetailLocal
import com.example.retrofit_da1.Data.LocalDataBase.Entities.ProductWithCategory
import com.example.retrofit_da1.Model.CategorySingle
import com.example.retrofit_da1.Model.FavoriteProduct
import com.example.retrofit_da1.Model.ProductDetail

fun ProductDetailLocal.toProduct(category: CategorySingleLocal) = ProductDetail(
    id, title, price, description, category.toCategorySingle(), listOf(images)
)

fun ProductWithCategory.toProduct() = ProductDetail(
    product.id,
    product.title,
    product.price,
    product.description,
    category.toCategorySingle(),
    listOf(product.images)
)

fun ProductDetail.toProductLocal() = ProductDetailLocal(
    id, title, price, description, category.id, images.firstOrNull() ?: ""
)

fun CategorySingleLocal.toCategorySingle() = CategorySingle(id, name, image)

fun CategorySingle.toCategorySingleLocal() = CategorySingleLocal(id, name, image)

fun List<ProductWithCategory>.toProductList() = map {
    it.product.toProduct(it.category)
}

fun List<ProductDetail>.toProductListLocal() = map(ProductDetail::toProductLocal)

//Favorites
fun FavoriteProductLocal.toFavoriteProduct() = FavoriteProduct(
    id, title, price, listOf(image)
)

fun FavoriteProduct.toFavoriteProductLocal() = FavoriteProductLocal(
    id, title, price, images.firstOrNull() ?: ""
)

fun List<FavoriteProductLocal>.toFavoriteProductList() = map(FavoriteProductLocal::toFavoriteProduct)

fun List<FavoriteProduct>.toFavoriteProductListLocal() = map(FavoriteProduct::toFavoriteProductLocal)

