package com.fred.swyppy.ui.screens.user

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.fred.swyppy.data.PropertyViewModel
import com.fred.swyppy.navigation.ROUTE_ADD_PROPERTY
import com.fred.swyppy.navigation.ROUT_LOGIN
import com.fred.swyppy.ui.components.VideoPlayer
import com.fred.swyppy.ui.theme.newpurple
import com.fred.swyppy.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.fred.swyppy.data.AuthViewModel  // 🆕 ADD IMPORT

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    propertyViewModel: PropertyViewModel = viewModel()
)
{
    val sessionManager = SessionManager(LocalContext.current)
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    // 🆕 ADD AuthViewModel for logout
    val authViewModel = remember { AuthViewModel(navController, context)}
    var selectedIndex by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var currentProperty by remember { mutableStateOf<com.fred.swyppy.models.Property?>(null) }

    // Category mode state: "BNB" or "Apartment"
    var categoryMode by remember { mutableStateOf("BNB") }


    // Phone call permission launcher
    val callPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted - make the call
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:+254700000000"))
            context.startActivity(intent)
        } else {
            // Permission denied - show message
            Toast.makeText(context, "Call permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Fetch properties when screen loads
    LaunchedEffect(Unit) {
        propertyViewModel.fetchProperties(context)
    }

    // Get properties from ViewModel
    val bnbProperties = propertyViewModel.bnbProperties
    val apartmentProperties = propertyViewModel.apartmentProperties

    // Search functionality
    val filteredBnbProperties = remember(searchQuery, bnbProperties) {
        if (searchQuery.isBlank()) {
            bnbProperties
        } else {
            bnbProperties.filter {
                it.name?.contains(searchQuery, ignoreCase = true) == true ||
                        it.agentName?.contains(searchQuery, ignoreCase = true) == true ||
                        it.location?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    val filteredApartmentProperties = remember(searchQuery, apartmentProperties) {
        if (searchQuery.isBlank()) {
            apartmentProperties
        } else {
            apartmentProperties.filter {
                it.name?.contains(searchQuery, ignoreCase = true) == true ||
                        it.agentName?.contains(searchQuery, ignoreCase = true) == true ||
                        it.location?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Swyppy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = newpurple,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()

                    })
                    {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Single NavigationBar containing both items
            NavigationBar(containerColor = newpurple) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color.White
                        )
                    },
                    label = { Text("Call", color = Color.White) },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        // Get current property's agent phone
                        val phoneNumber = currentProperty?.agentPhone ?: "+254700000000"
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                        context.startActivity(intent)
                    }
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "WhatsApp",
                            tint = Color.White
                        )
                    },
                    label = { Text("WhatsApp", color = Color.White) }, // Changed label
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1

                        // 🆕 WHATSAPP INTEGRATION
                        val whatsappNumber = currentProperty?.whatsappNumber ?: currentProperty?.agentPhone ?: "+254700000000"
                        val message = "Hi, I'm interested in your ${currentProperty?.name} property on Swyppy. Price: KSh ${currentProperty?.price}"

                        // Remove + and spaces from number
                        val cleanNumber = whatsappNumber.replace("+", "").replace(" ", "")

                        try {
                            // Try to open WhatsApp directly
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://wa.me/$cleanNumber?text=${Uri.encode(message)}")
                            intent.setPackage("com.whatsapp")
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // If WhatsApp not installed, open in browser
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://wa.me/$cleanNumber?text=${Uri.encode(message)}")
                            context.startActivity(intent)
                        }
                    }
                )
            }
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))

            ) {
                var isFocused by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(30.dp),
                    color = Color(0xFFF2F2F2),
                    tonalElevation = if (isFocused) 3.dp else 0.dp,
                    shadowElevation = if (isFocused) 3.dp else 0.dp
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search properties...", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxSize()
                            .onFocusChanged { isFocused = it.isFocused },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.DarkGray
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,

                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,

                            cursorColor = newpurple,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                        )
                    )
                }

                // Category Toggle Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // BNB Button
                    Button(
                        onClick = { categoryMode = "BNB" },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (categoryMode == "BNB") newpurple else Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "BNB (${filteredBnbProperties.size})",
                            color = if (categoryMode == "BNB") Color.White else Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Apartment Button
                    Button(
                        onClick = { categoryMode = "Apartment" },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (categoryMode == "Apartment") newpurple else Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Apartments (${filteredApartmentProperties.size})",
                            color = if (categoryMode == "Apartment") Color.White else Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                // Show properties based on selected mode
                when {

                    categoryMode == "BNB" && filteredBnbProperties.isNotEmpty() -> {
                        // Vertical Pager for BNB (up/down swipe) - FIX
                        val bnbPagerState = rememberPagerState(
                            pageCount = { filteredBnbProperties.size }
                        )

                        VerticalPager( // <-- Changed to VerticalPager
                            state = bnbPagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) { bnbPage ->
                            val property = filteredBnbProperties[bnbPage]

                            // Update current property for call/SMS
                            LaunchedEffect(property) {
                                currentProperty = property
                            }

                            PropertyCard(property = property)
                        }
                    }
                    categoryMode == "Apartment" && filteredApartmentProperties.isNotEmpty() -> {
                        // Vertical Pager for Apartments (up/down swipe)
                        val apartmentPagerState = rememberPagerState(
                            pageCount = { filteredApartmentProperties.size }
                        )

                        VerticalPager(
                            state = apartmentPagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) { apartmentPage ->
                            val property = filteredApartmentProperties[apartmentPage]

                            // Update current property for call/SMS
                            LaunchedEffect(property) {
                                currentProperty = property
                            }

                            PropertyCard(property = property)
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PropertyCard(property: com.fred.swyppy.models.Property) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Agent Name
        Text(
            text = "${property.agentName ?: "Unknown"}",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // 🆕 MEDIA GALLERY with INNER PAGER for media swiping
        val mediaUrls = property.mediaUrls ?: listOf(property.imageUrl ?: "")
        val mediaTypes = property.mediaTypes ?: List(mediaUrls.size) { "image" }

        if (mediaUrls.isNotEmpty()) {
            // 🔥 INNER HORIZONTAL PAGER for media within property
            val mediaPagerState = rememberPagerState(pageCount = { mediaUrls.size })

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // 🔥 Media swiper - swipe through images/videos
                    HorizontalPager(
                        state = mediaPagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { mediaPage ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            when (mediaTypes.getOrNull(mediaPage)) {
                                "video" -> {
                                    // Video Player
                                    VideoPlayer(
                                        videoUrl = mediaUrls[mediaPage],
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                else -> {
                                    // Image
                                    AsyncImage(
                                        model = mediaUrls[mediaPage],
                                        contentDescription = "Property Media",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }

                    // ❌ REMOVED: Old Media Indicator Dots (bottom center)

                    // 🔄 NEW: Media Indicator Badge (Bottom Center, No Numbers)
                    if (mediaUrls.size > 1) { // Only show when there is more than one media item
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp), // Positioned clearly at the bottom
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Swipe for More",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null, // decorative icon
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }


                    // ❌ REMOVED: Old Media Count Badge (top center)

                    // Category Badge (Top End)
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
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            )
                        )
                    }

                    // Price Badge (Top Start)
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        color = newpurple,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "KSh ${property.price ?: "0"}",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            )
                        )
                    }

                    // Location Badge (Bottom Start)
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
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            )
                        )
                    }
                }
            }
        }

        // Property Name
        Text(
            text = property.name ?: "Unnamed Property",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(rememberNavController())
}