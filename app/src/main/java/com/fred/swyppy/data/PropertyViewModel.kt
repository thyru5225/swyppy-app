package com.fred.swyppy.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.fred.swyppy.models.Property
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class PropertyViewModel : ViewModel() {

    // IMPORTANT: Changed URL to 'auto' to handle both images and videos
    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dazmz0qs0/auto/upload"
    private val uploadPreset = "BuyBloom2025"

    private val _properties = mutableStateListOf<Property>()
    val properties: List<Property> = _properties

    private val _bnbProperties = mutableStateListOf<Property>()
    val bnbProperties: List<Property> = _bnbProperties

    private val _apartmentProperties = mutableStateListOf<Property>()
    val apartmentProperties: List<Property> = _apartmentProperties

    /**
     * Uploads multiple mixed media (images/videos) and saves property data to Firebase.
     * @param onProgressUpdate Provides upload status feedback.
     * @param onUploadComplete Reports success/failure status.
     */
    fun uploadProperty(
        imageUris: List<Uri>,
        videoUris: List<Uri>,
        name: String,
        agentName: String,
        description: String,
        category: String,
        price: String,
        bedrooms: String,
        bathrooms: String,
        location: String,
        amenities: String,
        whatsappNumber: String,
        agentPhone: String, // Added the missing agentPhone
        latitude: String,
        longitude: String,
        context: Context,
        onProgressUpdate: (String) -> Unit, // 🆕 Callback for progress updates
        onUploadComplete: (Boolean) -> Unit // 🆕 Callback for completion status
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val mediaUrls = mutableListOf<String>()
                val mediaTypes = mutableListOf<String>()
                val totalMedia = imageUris.size + videoUris.size
                var completedUploads = 0

                onProgressUpdate("Starting upload of $totalMedia media files...")

                // Upload all images
                imageUris.forEachIndexed { index, uri ->
                    onProgressUpdate("Uploading image ${index + 1} of ${imageUris.size}...")
                    val imageUrl = uploadToCloudinary(context, uri)
                    mediaUrls.add(imageUrl)
                    mediaTypes.add("image")
                    completedUploads++
                }

                // Upload all videos
                videoUris.forEachIndexed { index, uri ->
                    onProgressUpdate("Uploading video ${index + 1} of ${videoUris.size}...")
                    val videoUrl = uploadVideoToCloudinary(context, uri)
                    mediaUrls.add(videoUrl)
                    mediaTypes.add("video")
                    completedUploads++
                }

                // Check for successful media uploads
                if (mediaUrls.size != totalMedia) {
                    throw Exception("Failed to upload all media files.")
                }

                onProgressUpdate("Media upload complete. Saving property data to Firebase...")

                // Save to Firebase
                val ref = FirebaseDatabase.getInstance().getReference("Properties").push()
                val propertyData = mapOf(
                    "id" to ref.key,
                    "name" to name,
                    "agentName" to agentName,
                    "description" to description,
                    "category" to category,
                    "price" to price,
                    "bedrooms" to bedrooms,
                    "bathrooms" to bathrooms,
                    "location" to location,
                    "amenities" to amenities,
                    // Store the first image URL as primary imageUrl for backward compatibility
                    "imageUrl" to (mediaUrls.zip(mediaTypes).firstOrNull { it.second == "image" }?.first ?: mediaUrls.firstOrNull() ?: ""),
                    "mediaUrls" to mediaUrls,
                    "mediaTypes" to mediaTypes,
                    "whatsappNumber" to whatsappNumber,
                    "agentPhone" to agentPhone, // Added agentPhone to Firebase data
                    "latitude" to latitude.toDoubleOrNull(),
                    "longitude" to longitude.toDoubleOrNull()
                )
                ref.setValue(propertyData).await()

                withContext(Dispatchers.Main) {
                    onProgressUpdate("Property saved successfully!")
                    onUploadComplete(true) // Report success
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onProgressUpdate("Upload failed: ${e.message}")
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                    onUploadComplete(false) // Report failure
                }
            }
        }
    }

    // Upload to Cloudinary (for images)
    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes() ?: throw Exception("Image read failed")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()

        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = OkHttpClient().newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Upload failed")
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"").find(responseBody ?: "")?.groupValues?.get(1)
        return secureUrl ?: throw Exception("Failed to get image URL")
    }

    // Upload Video to Cloudinary (with longer timeout)
    private fun uploadVideoToCloudinary(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: throw Exception("Video file not found")
        val fileBytes = inputStream.use { it.readBytes() }

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", "video.mp4",
                RequestBody.create("video/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .addFormDataPart("resource_type", "video") // Explicitly set resource type for videos
            .build()

        // Create a client with longer timeouts for video
        val client = OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            val errorResponse = response.body?.string()
            throw Exception("Video upload failed: $errorResponse")
        }

        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"").find(responseBody ?: "")?.groupValues?.get(1)

        return secureUrl ?: throw Exception("Failed to get video URL")
    }

    // Fetch All Properties
    fun fetchProperties(context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Properties")
        ref.get().addOnSuccessListener { snapshot ->
            _properties.clear()
            _bnbProperties.clear()
            _apartmentProperties.clear()

            for (child in snapshot.children) {
                val property = child.getValue(Property::class.java)
                property?.let {
                    it.id = child.key
                    _properties.add(it)

                    // Categorize properties
                    when (it.category) {
                        "BNB" -> _bnbProperties.add(it)
                        "Apartment" -> _apartmentProperties.add(it)
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to load properties", Toast.LENGTH_LONG).show()
        }
    }

    // Fetch BNB Properties Only
    fun fetchBNBProperties(context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Properties")
        ref.orderByChild("category").equalTo("BNB").get().addOnSuccessListener { snapshot ->
            _bnbProperties.clear()
            for (child in snapshot.children) {
                val property = child.getValue(Property::class.java)
                property?.let {
                    it.id = child.key
                    _bnbProperties.add(it)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to load BNB properties", Toast.LENGTH_LONG).show()
        }
    }

    // Fetch Apartment Properties Only
    fun fetchApartmentProperties(context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Properties")
        ref.orderByChild("category").equalTo("Apartment").get().addOnSuccessListener { snapshot ->
            _apartmentProperties.clear()
            for (child in snapshot.children) {
                val property = child.getValue(Property::class.java)
                property?.let {
                    it.id = child.key
                    _apartmentProperties.add(it)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to load apartment properties", Toast.LENGTH_LONG).show()
        }
    }

    // Search Properties
    fun searchProperties(query: String): List<Property> {
        return if (query.isBlank()) {
            _properties
        } else {
            _properties.filter {
                it.name?.contains(query, ignoreCase = true) == true ||
                        it.agentName?.contains(query, ignoreCase = true) == true ||
                        it.location?.contains(query, ignoreCase = true) == true ||
                        it.category?.contains(query, ignoreCase = true) == true ||
                        it.price?.contains(query, ignoreCase = true) == true

            }
        }
    }

    // Delete Property
    fun deleteProperty(propertyId: String, context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Properties").child(propertyId)
        ref.removeValue().addOnSuccessListener {
            _properties.removeAll { it.id == propertyId }
            _bnbProperties.removeAll { it.id == propertyId }
            _apartmentProperties.removeAll { it.id == propertyId }
            Toast.makeText(context, "Property deleted successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Property not deleted", Toast.LENGTH_LONG).show()
        }
    }

    // Update Property
    fun updateProperty(
        propertyId: String,
        imageUri: Uri?,
        name: String,
        agentName: String,
        description: String,
        category: String,
        price: String,
        bedrooms: String,
        bathrooms: String,
        location: String,
        amenities: String,
        agentPhone: String = "",
        whatsappNumber: String = "", // Added whatsappNumber for completeness
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val updateProperty = mapOf(
                    "id" to propertyId,
                    "name" to name,
                    "agentName" to agentName,
                    "description" to description,
                    "category" to category,
                    "price" to price,
                    "bedrooms" to bedrooms,
                    "bathrooms" to bathrooms,
                    "location" to location,
                    "amenities" to amenities,
                    "agentPhone" to agentPhone,
                    "whatsappNumber" to whatsappNumber,
                    "createdAt" to System.currentTimeMillis(),
                    "imageUrl" to imageUrl
                )
                val ref = FirebaseDatabase.getInstance().getReference("Properties").child(propertyId)
                ref.setValue(updateProperty).await()
                fetchProperties(context)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Property updated successfully", Toast.LENGTH_LONG).show()
                    //navController.navigate(ROUTE_VIEW_PROPERTIES)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Get Property by ID
    fun getPropertyById(propertyId: String): Property? {
        return _properties.find { it.id == propertyId }
    }

    // Filter Properties by Price Range
    fun filterByPriceRange(minPrice: Double, maxPrice: Double): List<Property> {
        return _properties.filter {
            val price = it.price?.toDoubleOrNull() ?: 0.0
            price in minPrice..maxPrice
        }
    }

    // Filter Properties by Bedrooms
    fun filterByBedrooms(bedrooms: Int): List<Property> {
        return _properties.filter {
            val beds = it.bedrooms?.toIntOrNull() ?: 0
            beds >= bedrooms
        }
    }

    // Get Properties by Location
    fun getPropertiesByLocation(location: String): List<Property> {
        return _properties.filter {
            it.location?.contains(location, ignoreCase = true) == true
        }
    }
}