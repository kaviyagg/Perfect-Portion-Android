package com.example.food_code_android

sealed class Screen(val route:String){
    object MainScreen: Screen("mainScreen")
}
