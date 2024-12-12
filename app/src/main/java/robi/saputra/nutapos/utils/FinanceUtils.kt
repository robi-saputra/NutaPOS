package robi.saputra.nutapos.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun String.formatString(): String {
    return this.padStart(12, ' ')
}

fun Int.formatToRupiah(): String {
    return "Rp "+this.toString()
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
}

@SuppressLint("SimpleDateFormat")
fun String.dateTimeToDate(dateTimeFormat: String, dateFormat: String): String {
    val inputFormat = SimpleDateFormat(dateTimeFormat, Locale("in", "ID"))
    val dateFormat = SimpleDateFormat(dateFormat, Locale("in", "ID"))

    val date: Date = inputFormat.parse(this) ?: return "Invalid date time"
    return dateFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun String.dateTimeToTime(dateTimeFormat: String): String {
    val inputFormat = SimpleDateFormat(dateTimeFormat, Locale("in", "ID"))
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale("in", "ID"))

    val date: Date = inputFormat.parse(this) ?: return "Invalid date time"
    return timeFormat.format(date)
}

fun String.dateTimeToMilliseconds(vararg formats: String): Long {
    for (format in formats) {
        try {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            val date = dateFormat.parse(this)
            if (date != null) {
                return date.time
            }
        } catch (e: Exception) {
            // Continue to the next format if parsing fails
        }
    }
    throw IllegalArgumentException("Unparseable date: $this with provided formats")
}

fun Long.millisecondsToDateTime(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(this))
}

fun String.extractDate(): String {
    return try {
        val dateParts = this.split(" ")
        dateParts[0]
    } catch (e: Exception) {
        ""
    }
}

fun getLocalDateTime(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

fun String.decodeBase64ToBitmap(): Bitmap? {
    return try {
        val decodedString = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Bitmap.convertBitmapToBase64(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val byteArray = baos.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

const val CAMERA_PERMISSION_REQUEST_CODE = 101
const val STORAGE_PERMISSION_REQUEST_CODE = 102

fun Context.isCameraPermissionGranted(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}
fun Activity.requestCameraPermission() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.CAMERA),
        CAMERA_PERMISSION_REQUEST_CODE
    )
}

fun Context.isStoragePermissionGranted(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}
fun Activity.requestStoragePermission() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
        STORAGE_PERMISSION_REQUEST_CODE
    )
}

fun Context.arePermissionsGranted(): Boolean {
    return this.isCameraPermissionGranted() && this.isStoragePermissionGranted()
}

fun Uri.getBitmapFromUri(context: Context): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(this)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}