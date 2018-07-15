package br.pedroso.mlkitsample.utils

import android.annotation.SuppressLint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
fun createImageFile(storageDir: File): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"


    return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
    )
}