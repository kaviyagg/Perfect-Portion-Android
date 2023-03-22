package com.example.food_code_android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.food_code_android.ui.theme.LightGreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun mainScreen(navController: NavController) {
    val context = LocalContext.current
    val jsonStrings = context.assets.open("items.json").bufferedReader().use { it.readText() }
    val foodItems = Gson().fromJson<List<FoodItem>>(
        jsonStrings,
        object : TypeToken<List<FoodItem>>() {}.type
    )
    val starredItems = remember { mutableStateListOf<String>() }
    var searchText by remember { mutableStateOf("") }
    Column() {
        Row(
            modifier = Modifier.background(LightGreen)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {

            IconButton(
                onClick = {
                    //TODO
                },
            ){
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp),
                )
            }
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .height(50.dp)
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Black,
                    placeholderColor = Color.LightGray,
                    textColor = Color.White,
                    disabledTextColor = Color.Gray,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        "Search",
                    )
                }
            )
            IconButton(
                onClick = {
                //TODO
                },
            ){
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        val categoryItems = mutableMapOf<String, MutableList<FoodItem>>()
        for (item in foodItems) {
            if (item.category in categoryItems) {
                categoryItems[item.category]?.add(item)
            } else {
                categoryItems[item.category] = mutableListOf(item)
            }
        }

        val filteredCategoryItems = categoryItems
            .map { (name, items) ->
                name to items.filter { it.searchString.contains(searchText, ignoreCase = true) }
            }
            .filter { (_, items) -> items.isNotEmpty() }
            .map { (name, items) -> FoodCategory(name, items) }

        LazyColumn {
            items(filteredCategoryItems) { category ->
                var isExpanded by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .padding(5.dp, 16.dp)
                        .fillMaxWidth()
                        .clickable {
                            isExpanded = !isExpanded
                        },
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(5.dp, 1.dp),
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1.5f),
                            textAlign = TextAlign.Start
                        )
                        Badge(
                            backgroundColor = LightGreen,
                            contentColor = Color.White,
                            content = { Text(text = category.raw.size.toString()) }
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Divider(color = Color.Gray, thickness = 1.dp)
                    AnimatedVisibility(visible = isExpanded) {
                        Column {
                            category.raw.forEach { item ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement =  Arrangement.Center) {
                                        val isStarred = starredItems.contains(item.code)
                                        Icon(
                                            imageVector = if (isStarred)  Icons.Default.Star  else  Icons.Outlined.Star ,
                                            contentDescription = null,
                                            tint = if (isStarred) Color.Yellow else Color.DarkGray,
                                            modifier = Modifier.size(22.dp)
                                                .padding(1.dp, 2.dp)
                                                .clickable {
                                                    if (isStarred) {
                                                        starredItems.remove(item.code) // unstar item
                                                    } else {
                                                        starredItems.add(item.code) // star item
                                                    }
                                                }

                                        )
                                        Box(
                                            Modifier.weight(1f),
                                            contentAlignment = Alignment.CenterStart
                                        ) {

                                            Text(
                                                text = item.name,
                                                modifier = Modifier.padding(4.dp),
                                                textAlign = TextAlign.Start
                                            )
                                        }

                                        Box(
                                            Modifier.weight(1f),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Text(
                                                text = item.code,
                                                textAlign = TextAlign.End
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


