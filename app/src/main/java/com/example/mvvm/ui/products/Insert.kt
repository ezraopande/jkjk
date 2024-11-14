package com.example.mvvm.ui.products

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.mvvm.navigation.ROUTE_HOME
import com.example.mvvm.navigation.ROUTE_HOTEL
import com.example.mvvm.navigation.ROUTE_INSERT
import com.example.mvvm.navigation.ROUTE_VIEWPRODUCTS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertProductsScreen(navController: NavController) {
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploading by remember { mutableStateOf(false) }

    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance().reference
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        productImageUri = uri
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
                    selected = true,
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
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.systemBars),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Text(
                text = "add menu",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B1F32)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("item", color = Color(0xFF1B1F32)) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFFFF5722),
                            cursorColor = Color(0xFFFF5722)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = productPrice,
                        onValueChange = { productPrice = it },
                        label = { Text("price", color = Color(0xFF1B1F32)) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFFFF5722),
                            cursorColor = Color(0xFFFF5722)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = productDescription,
                        onValueChange = { productDescription = it },
                        label = { Text("about", color = Color(0xFF1B1F32)) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFFFF5722),
                            cursorColor = Color(0xFFFF5722)
                        ),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    )

                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                    ) {
                        Text("Item Image", color = Color.White)
                    }

                    productImageUri?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = rememberImagePainter(it),
                            contentDescription = "Item Image",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF1F1F1))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (productName.isNotEmpty() && productPrice.isNotEmpty() && productDescription.isNotEmpty() && productImageUri != null) {
                        uploading = true
                        uploadProduct(
                            productName, productPrice, productDescription,
                            productImageUri!!, storage, firestore, context, navController
                        ) { success ->
                            uploading = false
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Item added successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to add item",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                enabled = !uploading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors( Color(0xFFFF5722))
            ) {
                if (uploading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Item", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

fun uploadProduct(
    name: String,
    price: String,
    description: String,
    imageUri: Uri,
    storageReference: StorageReference,
    firestore: FirebaseFirestore,
    context: android.content.Context,
    navController: NavController,
    onComplete: (Boolean) -> Unit
) {
    val storageRef = storageReference.child("products/${imageUri.lastPathSegment}")
    val uploadTask = storageRef.putFile(imageUri)

    uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let {
                throw it
            }
        }
        storageRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result
            val product = hashMapOf(
                "name" to name,
                "price" to price,
                "description" to description,
                "image" to downloadUri.toString()
            )

            firestore.collection("products").add(product)
                .addOnSuccessListener {
                    onComplete(true)
                    navController.navigate(ROUTE_HOME)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        } else {
            onComplete(false)
        }
    }
}
