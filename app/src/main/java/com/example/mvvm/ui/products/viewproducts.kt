package com.example.mvvm.ui.products



import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.mvvm.navigation.ROUTE_HOME
import com.example.mvvm.navigation.ROUTE_HOTEL
import com.example.mvvm.navigation.ROUTE_INSERT
import com.example.mvvm.navigation.ROUTE_VIEWPRODUCTS
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ViewProductsScreen(navController: NavController) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isEmpty by remember { mutableStateOf(false) }

    val firestore = FirebaseFirestore.getInstance()

    // Fetch products from Firestore
    LaunchedEffect(Unit) {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val fetchedProducts = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Product::class.java)?.apply {
                        id = document.id  // Assign document ID to product
                    }
                }
                products = fetchedProducts
                isLoading = false
                isEmpty = fetchedProducts.isEmpty()
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
        // UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFFFF5722)) // Use custom color for loader
            } else if (isEmpty) {
                Text(text = "No items found", fontSize = 20.sp, color = Color.Gray)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Display items in 2 columns
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(products) { product ->
                        ProductItem(product) {
                            navController.navigate("details/${product.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1F32)), // Custom card color for modern look
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Box(modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1), RoundedCornerShape(8.dp))) {
                product.image?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = product.name, fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Price: ${product.price}", fontSize = 16.sp, color = Color(0xFFFF5722)) // Custom price text color

        }
    }
}

data class Product(
    var id: String = "",
    val name: String = "",
    val price: String = "",
    val description: String = "",
    val image: String? = null
)