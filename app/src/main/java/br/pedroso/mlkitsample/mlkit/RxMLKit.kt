package br.pedroso.mlkitsample.mlkit

import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import io.reactivex.Observable

class RxMLKit {
    companion object {
        fun detectFaces(bitmap: Bitmap) = Observable.create<List<FirebaseVisionFace>> {
            val observableEmitter = it
            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)

            val options = FirebaseVisionFaceDetectorOptions.Builder()
                    .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                    .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                    .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                    .setMinFaceSize(0.15f)
                    .setTrackingEnabled(true)
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

            val detector = FirebaseVision.getInstance().visionTextDetector

            detector.detectInImage(firebaseVisionImage)
                    .addOnSuccessListener {
                        observableEmitter.onNext(it)
                        observableEmitter.onComplete()
                    }
                    .addOnFailureListener {
                        observableEmitter.onError(it)
                    }
        }

        fun labelImageLocal(bitmap: Bitmap) = Observable.create<List<FirebaseVisionLabel>> {
            val observableEmitter = it

            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)

            val options = FirebaseVisionLabelDetectorOptions.Builder()
                    .setConfidenceThreshold(0.8f)
                    .build()

            val detector = FirebaseVision.getInstance()
                    .getVisionLabelDetector(options)

            detector.detectInImage(firebaseVisionImage)
                    .addOnSuccessListener {
                        observableEmitter.onNext(it)
                        observableEmitter.onComplete()
                    }
                    .addOnFailureListener {
                        observableEmitter.onError(it)
                    }
        }

        fun labelImageRemote(bitmap: Bitmap) = Observable.create<List<FirebaseVisionCloudLabel>> {
            val observableEmitter = it

            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)

            val options = FirebaseVisionCloudDetectorOptions.Builder()
                    .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                    .setMaxResults(15)
                    .build()

            val detector = FirebaseVision.getInstance()
                    .getVisionCloudLabelDetector(options)

            detector.detectInImage(firebaseVisionImage)
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