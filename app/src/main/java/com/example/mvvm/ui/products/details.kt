package com.example.mvvm.ui.products

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.mvvm.navigation.ROUTE_HOME
import com.example.mvvm.navigation.ROUTE_HOTEL
import com.example.mvvm.navigation.ROUTE_INSERT
import com.example.mvvm.navigation.ROUTE_VIEWPRODUCTS
import com.google.firebase.firestore.FirebaseFirestore
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailsScreen(productId: String, navController: NavController) {
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val firestore = FirebaseFirestore.getInstance()

    // Fetch product details from Firestore using product ID
    LaunchedEffect(productId) {
        firestore.collection("products").document(productId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                product = documentSnapshot.toObject(Product::class.java)
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
                backgroundColor = Color(0xFF1B1F32),
                contentColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", color = Color.White) },
                    selected = false,
                    onClick = { navController.navigate(ROUTE_HOME) }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Menu") },
                    label = { Text("Menu", color = Color.White) },
                    selected = false,
                    onClick = { navController.navigate(ROUTE_VIEWPRODUCTS) }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Add Product") },
                    label = { Text("Add", color = Color.White) },
                    selected = false,
                    onClick = { navController.navigate(ROUTE_INSERT) }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Hotel") },
                    label = { Text("Hotel", color = Color.White) },
                    selected = false,
                    onClick = { navController.navigate(ROUTE_HOTEL) }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFFFF5722))
            } else if (product == null) {
                Text(text = "Product not found", fontSize = 20.sp, color = Color.Gray)
            } else {
                ProductDetails(product!!)
            }
        }
    }
}

@Composable
fun ProductDetails(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF1B1F32), RoundedCornerShape(12.dp))
            .padding(16.dp), // Inner padding for content
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1), RoundedCornerShape(12.dp))
        ) {
            product.image?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.name,
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Price: ${product.price}",
            fontSize = 20.sp,
            color = Color(0xFFFF5722),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.description,
            fontSize = 16.sp,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}
