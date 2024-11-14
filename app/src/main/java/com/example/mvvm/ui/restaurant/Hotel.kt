package com.example.mvvm.ui.restaurant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage


@Composable
fun Hotel(navController: NavController){

    LazyColumn {
        item {

            Column {
                Spacer(modifier=Modifier.height(50.dp))

                Row {



                    Text(text = "Favorite",
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold





                    )

                    Spacer(modifier=Modifier.width(50.dp))

//
                    AsyncImage(
                        model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwfttJp2Cq-Es0lgxLdJeN5Ha4-YudSX8C3vQLjZtkvzZXBmO6cI8pilpy07plD3vPk8E&usqp=CAU.png",
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                    )








                }

                Spacer(modifier=Modifier.height(50.dp))

                Column {



                    













                }



                







































            }


















































        }






















































    }





















































































}