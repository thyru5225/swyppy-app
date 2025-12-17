package com.fred.swyppy.ui.screens.admin

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions // <--- 1. CORRECT IMPORT ADDED
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.fred.swyppy.data.PropertyViewModel
import com.fred.swyppy.navigation.ROUTE_VIEW_PROPERTY
import com.fred.swyppy.ui.theme.newpurple

// Data class to hold selected media and its type (new)
data class MediaItem(val uri: Uri, val isVideo: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyScreen(
    navController: NavController,
    propertyViewModel: PropertyViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Property data states
    var name by remember { mutableStateOf("") }
    var agentName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Apartment") }
    var location by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var bedrooms by remember { mutableStateOf("") }
    var bathrooms by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }
    var agentPhone by remember { mutableStateOf("") }
    var amenities by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    // Media states (Consolidated into one list of MediaItem)
    var selectedMedia by remember { mutableStateOf(listOf<MediaItem>()) }

    // UI States
    var isLoading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf("Starting upload...") }

    // Dropdown state for category
    val categories = listOf("Apartment", "BNB")
    var expanded by remember { mutableStateOf(false) }

    // Unified Media Picker Launcher (Accepts all media types)
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        val contentResolver = context.contentResolver
        selectedMedia = uris.map { uri ->
            val mimeType = contentResolver.getType(uri)
            val isVideo = mimeType?.startsWith("video/") == true
            MediaItem(uri, isVideo)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Property", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = newpurple),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(ROUTE_VIEW_PROPERTY) }) {
                        Icon(Icons.Default.Home, contentDescription = "View Properties", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Property Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Property Name") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Agent Name
            OutlinedTextField(
                value = agentName,
                onValueChange = { agentName = it },
                label = { Text("Agent Name") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().height(120.dp).padding(bottom = 8.dp),
                maxLines = 5
            )

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = category,
                    onValueChange = { },
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                category = selectionOption
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            Text("Property Details", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Location
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Amenities
            OutlinedTextField(
                value = amenities,
                onValueChange = { amenities = it },
                label = { Text("Amenities (e.g., Pool, Gym)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )


            // Price
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (KSh)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // <--- 2. USAGE CORRECTED
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Bedrooms
                OutlinedTextField(
                    value = bedrooms,
                    onValueChange = { bedrooms = it },
                    label = { Text("Bedrooms") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // <--- 2. USAGE CORRECTED
                    modifier = Modifier.weight(1f).padding(bottom = 8.dp)
                )

                // Bathrooms
                OutlinedTextField(
                    value = bathrooms,
                    onValueChange = { bathrooms = it },
                    label = { Text("Bathrooms") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // <--- 2. USAGE CORRECTED
                    modifier = Modifier.weight(1f).padding(bottom = 8.dp)
                )
            }

            // Latitude and Longitude Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // <--- 2. USAGE CORRECTED
                    modifier = Modifier.weight(1f).padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // <--- 2. USAGE CORRECTED
                    modifier = Modifier.weight(1f).padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Contact Details", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // WhatsApp Number
            OutlinedTextField(
                value = whatsappNumber,
                onValueChange = { whatsappNumber = it },
                label = { Text("WhatsApp Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // <--- 2. USAGE CORRECTED
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Agent Phone
            OutlinedTextField(
                value = agentPhone,
                onValueChange = { agentPhone = it },
                label = { Text("Agent Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // <--- 2. USAGE CORRECTED
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // MEDIA SELECTION BUTTON (Unified)
            Button(
                onClick = { mediaPickerLauncher.launch("*/*") }, // Launch picker for all media types
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = newpurple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Select Images/Videos (${selectedMedia.size})", color = Color.White)
            }

            // Media Preview
            if (selectedMedia.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Selected Media:", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedMedia.forEach { item ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (item.isVideo) {
                                // For videos, just show a placeholder
                                Icon(
                                    Icons.Default.ArrowBack, // Placeholder icon
                                    contentDescription = "Video Placeholder",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                )
                            } else {
                                // For images, show the thumbnail
                                Image(
                                    painter = rememberAsyncImagePainter(model = item.uri),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            // Upload Button
            Button(
                onClick = {
                    if (selectedMedia.isEmpty()) {
                        Toast.makeText(context, "Please select at least one image or video.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (name.isBlank() || price.isBlank() || agentName.isBlank()) {
                        Toast.makeText(context, "Please fill in all required fields.", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    isLoading = true
                    uploadProgress = "Preparing to upload media..."

                    // Separate the unified media list back into images and videos for the ViewModel
                    val imageUris = selectedMedia.filter { !it.isVideo }.map { it.uri }
                    val videoUris = selectedMedia.filter { it.isVideo }.map { it.uri }

                    // Call the modified uploadProperty function in the ViewModel
                    propertyViewModel.uploadProperty(
                        imageUris = imageUris,
                        videoUris = videoUris,
                        name = name.trim(),
                        agentName = agentName.trim(),
                        description = description.trim(),
                        category = category,
                        location = location.trim(),
                        price = price.trim(),
                        bedrooms = bedrooms.trim(),
                        bathrooms = bathrooms.trim(),
                        whatsappNumber = whatsappNumber.trim(),
                        agentPhone = agentPhone.trim(),
                        amenities = amenities.trim(),
                        latitude = latitude.trim(),
                        longitude = longitude.trim(),
                        context = context,
                        onProgressUpdate = { progress -> uploadProgress = progress },
                        onUploadComplete = { success ->
                            isLoading = false
                            if (success) {
                                // Clear fields and navigate back on success
                                name = ""
                                agentName = ""
                                description = ""
                                location = ""
                                price = ""
                                bedrooms = ""
                                bathrooms = ""
                                whatsappNumber = ""
                                agentPhone = ""
                                amenities = ""
                                latitude = ""
                                longitude = ""
                                selectedMedia = emptyList()
                                navController.popBackStack()
                            }
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ADD PROPERTY", color = Color.White)
            }
        }

        // Upload Progress Overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(32.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = newpurple,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uploadProgress,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Please wait...",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddPropertyScreenPreview() {
    AddPropertyScreen(rememberNavController())
}