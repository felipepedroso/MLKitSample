package br.pedroso.mlkitsample.presentation

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import br.pedroso.mlkitsample.R
import br.pedroso.mlkitsample.utils.UI_SCHEDULER
import br.pedroso.mlkitsample.utils.createImageFile
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var imageBitmap: Bitmap? = null

    private var photoUriToLoad: Uri? = null

    private lateinit var viewModel: MainActivityViewModel

    private val subscriptions by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }

    private fun setupViews() {
        buttonGetImageFromCamera.setOnClickListener { getImageFromCamera() }
        buttonGetImageFromFile.setOnClickListener { getImageFromFile() }
        buttonDetectFace.setOnClickListener { doFaceDetection() }
        buttonDetectText.setOnClickListener { doTextDetection() }
        buttonLabelImageRemote.setOnClickListener { doLabelRemote() }
        buttonLabelImageLocal.setOnClickListener { doLabelLocal() }
    }

    private fun doLabelLocal() = imageBitmap?.let {
        val labelLocalObservable = viewModel.labelLocal(it)
        doImageProcessing(labelLocalObservable)
    }

    private fun doLabelRemote() = imageBitmap?.let {
        val labelRemoteObservable = viewModel.labelRemote(it)
        doImageProcessing(labelRemoteObservable)
    }

    private fun doFaceDetection() = imageBitmap?.let {
        val faceDetectionObservable = viewModel.detectFaces(it)
        doImageProcessing(faceDetectionObservable)
    }

    private fun doTextDetection() = imageBitmap?.let {
        val textDetectionObservable = viewModel.detectText(it)
        doImageProcessing(textDetectionObservable)
    }

    private fun doImageProcessing(imageProcessingObservable: Observable<ImageProcessingResult>) {
        val subscription = imageProcessingObservable
                .doOnSubscribe { showLoading() }
                .doOnTerminate { hideLoading() }
                .observeOn(UI_SCHEDULER)
                .subscribe(
                        { displayProcessingResult(it) },
                        {
                            Log.e(LOG_TAG, it.toString())
                            displayError(R.string.generic_error)
                        }
                )

        subscriptions.add(subscription)
    }

    private fun hideLoading() {
        enableFirebaseButtons()
        progressBarLoading.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        disableFirebaseButtons()
        progressBarLoading.visibility = View.VISIBLE
        clearTextView()
        displayBitmap(imageBitmap)
    }

    private fun clearTextView() {
        textViewResult.text = ""
    }

    private fun displayError(@StringRes errorTextResource: Int) {
        val errorText = getString(errorTextResource)
        displayText(errorText, android.R.color.holo_red_dark)
    }

    private fun displayProcessingResult(imageProcessingResult: ImageProcessingResult) {
        displayBitmap(imageProcessingResult.annotatedImage)
        displayText(imageProcessingResult.resultText, android.R.color.black)
    }

    private fun displayText(resultText: String, @ColorRes colorResource: Int) {
        val color = ContextCompat.getColor(this, colorResource)
        textViewResult.setTextColor(color)
        textViewResult.text = resultText
    }

    private fun getImageFromFile() {
        clearTextView()
        val pickPhotoIntent = Intent(Intent.ACTION_PICK)
        pickPhotoIntent.type = "image/*"

        if (pickPhotoIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(pickPhotoIntent, REQUEST_PICK_IMAGE)
        }
    }

    private fun getImageFromCamera() {
        clearTextView()
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val photoFile: File? = try {
                createImageFile(storageDir)
            } catch (exception: IOException) {
                null
            }

            if (photoFile != null) {
                photoUriToLoad = FileProvider.getUriForFile(
                        this,
                        "br.pedroso.mlkitsample",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUriToLoad)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> getCameraImageFromResultData(data)
                REQUEST_PICK_IMAGE -> getFileImageFromResultData(data)
                else -> Unit
            }
        }
    }

    private fun getFileImageFromResultData(data: Intent) {
        imageBitmap = loadPhotoFromUri(data.data)
        imageBitmap?.let { enableFirebaseButtons() }
        displayBitmap(imageBitmap)
    }

    private fun getCameraImageFromResultData(data: Intent) {
        imageBitmap = if (photoUriToLoad != null) {
            loadPhotoFromUri(photoUriToLoad)
        } else {
            val extras = data.extras
            extras["data"] as Bitmap
        }

        imageBitmap?.let { enableFirebaseButtons() }
        displayBitmap(imageBitmap)
    }

    private fun loadPhotoFromUri(photoUri: Uri?): Bitmap? = try {
        val imageStream = contentResolver.openInputStream(photoUri)
        BitmapFactory.decodeStream(imageStream)
    } catch (exception: Exception) {
        null
    }

    private fun displayBitmap(bitmap: Bitmap?) = bitmap?.let { imageViewPicture.setImageBitmap(it) }

    private fun enableFirebaseButtons() = setFirebaseButtonsEnabledState(true)

    private fun disableFirebaseButtons() = setFirebaseButtonsEnabledState(false)

    private fun setFirebaseButtonsEnabledState(isEnabled: Boolean) {
        buttonDetectFace.isEnabled = isEnabled
        buttonLabelImageLocal.isEnabled = isEnabled
        buttonLabelImageRemote.isEnabled = isEnabled
        buttonDetectText.isEnabled = isEnabled
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_PICK_IMAGE = 2
        val LOG_TAG = MainActivity::class.java.name!!
    }
}
