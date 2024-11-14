package com.example.mvvm.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composer
import androidx.compose.ui.Modifier
import com.example.mvvm.navigation.ROUTE_ABOUT


@Composable

fun About(navComposer: Composer){

    LazyColumn (){


        item {
            Column {
                Text(text = "about",
                    modifier = Modifier
//                        .clickable { navController.navigate(ROUTE_ABOUT) }



                    )
            }
        }




















    }












}