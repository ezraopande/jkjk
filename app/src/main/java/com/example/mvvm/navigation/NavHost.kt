package com.example.mvvm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvvm.ui.about.About
import com.example.mvvm.ui.home.Home
import com.example.mvvm.ui.products.DetailsScreen
import com.example.mvvm.ui.products.InsertProductsScreen
import com.example.mvvm.ui.products.ViewProductsScreen
import com.example.mvvm.ui.restaurant.Hotel

@Composable
fun AppNavHost(

navController: NavHostController = rememberNavController(),
startDestination: String = ROUTE_HOME




) {


    NavHost(

        navController = navController,
        startDestination = startDestination

    ) {

        composable(ROUTE_HOME) {


            Home(navController)

        }

        composable(ROUTE_ABOUT) {

//            About(navController)


        }

        composable(ROUTE_INSERT) {

            InsertProductsScreen(navController)
        }

        composable(ROUTE_VIEWPRODUCTS) {

            ViewProductsScreen(navController)

        }

        composable("details/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            productId?.let {
                DetailsScreen(productId, navController)
            }
        }






        composable(
            ROUTE_HOTEL
        ) { Hotel(navController) }



















    }







}

