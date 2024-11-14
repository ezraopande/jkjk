package com.example.mvvm.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mvvm.R
import com.example.mvvm.navigation.ROUTE_HOTEL
import com.example.mvvm.navigation.ROUTE_INSERT
import com.example.mvvm.navigation.ROUTE_VIEWPRODUCTS
import com.example.mvvm.ui.products.Product
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val firestore = FirebaseFirestore.getInstance()

    // Fetch up to 4 products from Firestore
    LaunchedEffect(Unit) {
        firestore.collection("products")
            .limit(4) // Limit to 4 items
            .get()
            .addOnSuccessListener { querySnapshot ->
                val fetchedProducts = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Product::class.java)?.apply {
                        id = document.id
                    }
                }
                products = fetchedProducts
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Welcome to Our Restaurant", color = Color.White) },

            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Welcome to Our Restaurant", style = MaterialTheme.typography.h5)

                Spacer(modifier = Modifier.height(16.dp))

                Image(painter = painterResource(id = R.drawable.r),
                    contentDescription ="",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F1F1)),
                    contentScale = ContentScale.Crop)


                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = Color(0xFFFF5722))
                } else if (products.isEmpty()) {
                    Text(text = "No items available", color = Color.Gray)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(products) { product ->
                            ProductCard(product) {
                                navController.navigate("details/${product.id}")
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
                backgroundColor = Color(0xFF1B1F32),
                contentColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", color = Color.White) },
                    selected = true,
                    onClick = { /* Already on Home */ }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Menu") },
                    label = { Text("Menu", color = Color.White) },
                    selected = false,
                    onClick = { navController.navigate(ROUTE_VIEWPRODUCTS) }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Hotel") },
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
        }
    )
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Display the product image
            product.image?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF1F1F1)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Price: ${product.price}",
                fontSize = 16.sp,
                color = Color(0xFFFF5722)
            )
        }
    }
}

