package com.fred.swyppy.models

data class Property(
    var id: String? = null,
    var name: String? = null,
    var agentName: String? = null,
    var agentPhone: String? = null, // Agent phone number for calling
    var agentEmail: String? = null, // Agent email for contact
    var description: String? = null,
    var category: String? = null, // "BNB" or "Apartment"
    var price: String? = null,
    var bedrooms: String? = null,
    var bathrooms: String? = null,
    var location: String? = null,
    var amenities: String? = null,
    var imageUrl: String? = null,
    var createdAt: Long? = null, // Timestamp when property was added
    //var latitude: Double? = null,      // For map coordinates
    //var longitude: Double? = null,     // For map coordinates
    var whatsappNumber: String? = null, // Agent's WhatsApp number
    var mediaUrls: List<String>? = null,      // All media URLs (images + videos)
    var mediaTypes: List<String>? = null      // "image" or "video" for each URL
)