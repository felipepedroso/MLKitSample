package br.pedroso.mlkitsample.mlkit

import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import io.reactivex.Observable

class RxMLKit {
    companion object {
        fun detectFaces(bitmap: Bitmap) = Observable.create<List<FirebaseVisionFace>> {
            val observableEmitter = it
            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)

            val options = FirebaseVisionFaceDetectorOptions.Builder()
                    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                    .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                    .setMinFaceSize(0.15f)
                    .enableTracking()
                    .build()

            val detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(options)

            detector.detectInImage(firebaseVisionImage)
                    .addOnSuccessListener {
                        observableEmitter.onNext(it)
                        observableEmitter.onComplete()
                    }
                    .addOnFailureListener {
                        observableEmitter.onError(it)
                    }
        }

        fun detectText(bitmap: Bitmap) = Observable.create<FirebaseVisionText> {
            val observableEmitter = it

            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)

            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

            detector.processImage(firebaseVisionImage)
                    .addOnSuccessListener {
                        observableEmitter.onNext(it)
                        observableEmitter.onComplete()
                    }
                    .addOnFailureListener {
                        observableEmitter.onError(it)
                    }
        }

        fun labelImageLocal(bitmap: Bitmap) = Observable.create<List<FirebaseVisionImageLabel>> {
            val observableEmitter = it

            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)

            val options = FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                    .setConfidenceThreshold(0.8f)
                    .build()

            val detector = FirebaseVision.getInstance()
                    .getOnDeviceImageLabeler(options)

            detector.processImage(firebaseVisionImage)
                    .addOnSuccessListener {
                        observableEmitter.onNext(it)
                        observableEmitter.onComplete()
                    }
                    .addOnFailureListener {
                        observableEmitter.onError(it)
                    }
        }

        fun labelImageRemote(bitmap: Bitmap) = Observable.create<List<FirebaseVisionImageLabel>> {
            val observableEmitter = it

            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)
            val detector = FirebaseVision.getInstance().cloudImageLabeler

            detector.processImage(firebaseVisionImage)
                    .addOnSuccessListener {
                        observableEmitter.onNext(it)
                        observableEmitter.onComplete()
                    }
                    .addOnFailureListener {
                        observableEmitter.onError(it)
                    }
        }
    }
}