package com.fred.swyppy.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Helper composable to handle phone call permission
 */
@Composable
fun rememberCallPermissionLauncher(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) {
        onPermissionGranted()
    } else {
        onPermissionDenied()
    }
}

/**
 * Check if call permission is granted
 */
fun hasCallPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Make a phone call (with permission check)
 */
fun makePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
    if (hasCallPermission(context)) {
        context.startActivity(intent)
    }
}

/**
 * Open dialer (no permission needed)
 */
fun openDialer(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    context.startActivity(intent)
}

/**
 * Send SMS
 */
fun sendSMS(context: Context, phoneNumber: String, message: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("sms:$phoneNumber"))
    intent.putExtra("sms_body", message)
    context.startActivity(intent)
}