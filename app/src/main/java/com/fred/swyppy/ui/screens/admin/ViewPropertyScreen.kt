package com.fred.swyppy.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import coil.compose.AsyncImage
import com.fred.swyppy.data.PropertyViewModel
import com.fred.swyppy.models.Property
import com.fred.swyppy.navigation.ROUTE_ADD_PROPERTY
import com.fred.swyppy.navigation.ROUTE_MAIN_SCREEN
import com.fred.swyppy.navigation.ROUTE_UPDATE_PROPERTY
import com.fred.swyppy.navigation.ROUTE_VIEW_PROPERTY
import com.fred.swyppy.navigation.ROUT_LOGIN
import com.fred.swyppy.ui.theme.newpurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPropertyScreen(navController: NavController) {
    val propertyViewModel: PropertyViewModel = viewModel()
    val properties = propertyViewModel.properties
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        propertyViewModel.fetchProperties(context)
    }

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        //Top Bar
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Property Listings",
                        fontWeight = FontWeight.Bold
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = newpurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )

            )
        },


        //Bottom Bar
        bottomBar = {
            NavigationBar(containerColor = newpurple) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "User View",
                            tint = Color.White
                        )
                    },
                    label = { Text("User View", color = Color.White) },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        navController.navigate(ROUTE_MAIN_SCREEN)
                    }
                )
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ROUTE_ADD_PROPERTY)
                },
                containerColor = newpurple
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Property",
                    tint = Color.White
                )
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Empty state or property list
            if (properties.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Properties Yet",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Click + to add your first property",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(properties) { property ->
                        PropertyCard(
                            property = property,
                            onDelete = { propertyId ->
                                propertyViewModel.deleteProperty(propertyId, context)
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyCard(
    property: Property,
    onDelete: (String) -> Unit,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete ${property.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    property.id?.let { onDelete(it) }
                }) {
                    Text("Yes", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            // Property Image
            AsyncImage(
                model = property.imageUrl,
                contentDescription = "Property Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            // Category Badge at Top Right
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                color = newpurple,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = property.category ?: "Property",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // Price Badge at Top Left
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                color = Color.Green,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "KSh ${property.price ?: "0"}",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // Location Badge at Bottom Left
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                color = newpurple,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = property.location ?: "Unknown location",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // Action Buttons at Bottom Right
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // UPDATE/EDIT ICON
                Surface(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate("$ROUTE_UPDATE_PROPERTY/${property.id}")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Update",
                            tint = newpurple
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // DELETE ICON
                Surface(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }

    // Property Details Below Image
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp)
    ) {
        // Property Name
        Text(
            text = property.name ?: "Unnamed Property",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Agent Name
        Text(
            text = "Agent: ${property.agentName ?: "Unknown"}",
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )

        // Bedrooms and Bathrooms
        if (!property.bedrooms.isNullOrEmpty() || !property.bathrooms.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!property.bedrooms.isNullOrEmpty()) {
                    Text(
                        text = "${property.bedrooms} Beds",
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                }
                if (!property.bathrooms.isNullOrEmpty()) {
                    Text(
                        text = "${property.bathrooms} Baths",
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewPropertyScreenPreview() {
    ViewPropertyScreen(rememberNavController())
}
