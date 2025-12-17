package com.fred.swyppy.ui.screens.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.fred.swyppy.data.PropertyViewModel
import com.fred.swyppy.ui.theme.newpurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePropertyScreen(
    navController: NavController,
    propertyId: String,
    propertyViewModel: PropertyViewModel = viewModel()
) {
    val context = LocalContext.current
    val property = propertyViewModel.getPropertyById(propertyId)

    // Form states - Initialize with existing property data
    var name by remember { mutableStateOf(property?.name ?: "") }
    var agentName by remember { mutableStateOf(property?.agentName ?: "") }
    var description by remember { mutableStateOf(property?.description ?: "") }
    var category by remember { mutableStateOf(property?.category ?: "BNB") }
    var price by remember { mutableStateOf(property?.price ?: "") }
    var bedrooms by remember { mutableStateOf(property?.bedrooms ?: "") }
    var bathrooms by remember { mutableStateOf(property?.bathrooms ?: "") }
    var location by remember { mutableStateOf(property?.location ?: "") }
    var amenities by remember { mutableStateOf(property?.amenities ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Update Property",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = newpurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Current/New Image section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "New Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (property?.imageUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(property.imageUrl),
                            contentDescription = "Current Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Add Image",
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No image available",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = newpurple)
            ) {
                Text(if (imageUri != null) "Change Image" else "Update Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Property Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Property Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = newpurple,
                    focusedLabelColor = newpurple
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Agent Name
            OutlinedTextField(
                value = agentName,
                onValueChange = { agentName = it },
                label = { Text("Agent Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = newpurple,
                    focusedLabelColor = newpurple
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = newpurple,
                        focusedLabelColor = newpurple
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("BNB") },
                        onClick = {
                            category = "BNB"
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Apartment") },
                        onClick = {
                            category = "Apartment"
                            expanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (KSh)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = newpurple,
                    focusedLabelColor = newpurple
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bedrooms and Bathrooms
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = bedrooms,
                    onValueChange = { bedrooms = it },
                    label = { Text("Bedrooms") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = newpurple,
                        focusedLabelColor = newpurple
                    )
                )
                OutlinedTextField(
                    value = bathrooms,
                    onValueChange = { bathrooms = it },
                    label = { Text("Bathrooms") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = newpurple,
                        focusedLabelColor = newpurple
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Location
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = newpurple,
                    focusedLabelColor = newpurple
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amenities
            OutlinedTextField(
                value = amenities,
                onValueChange = { amenities = it },
                label = { Text("Amenities (e.g., WiFi, Pool, Parking)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = newpurple,
                    focusedLabelColor = newpurple
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = newpurple,
                    focusedLabelColor = newpurple
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Update Button
            Button(
                onClick = {
                    if (name.isNotBlank() && agentName.isNotBlank() &&
                        price.isNotBlank() && location.isNotBlank() && propertyId.isNotBlank()) {
                        propertyViewModel.updateProperty(
                            propertyId = propertyId,
                            imageUri = imageUri,
                            name = name,
                            agentName = agentName,
                            description = description,
                            category = category,
                            price = price,
                            bedrooms = bedrooms,
                            bathrooms = bathrooms,
                            location = location,
                            amenities = amenities,
                            context = context,
                            navController = navController
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = newpurple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Update Property",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdatePropertyScreenPreview() {
    UpdatePropertyScreen(rememberNavController(), "sample-id")
}