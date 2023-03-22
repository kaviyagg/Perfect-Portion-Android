package com.example.food_code_android

data class FoodItem(val name: String, val code: String, val category: String, val searchString: String )
data class FoodCategory(val name: String, val raw: List<FoodItem>)