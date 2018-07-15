package br.pedroso.mlkitsample.presentation.mappers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import br.pedroso.mlkitsample.presentation.ImageProcessingResult
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark

class FaceDetectionResultMapper {
    companion object {
        fun mapResult(originalImage: Bitmap, detectedFaces: List<FirebaseVisionFace>): ImageProcessingResult {
            val annotatedImage = annotateImage(originalImage, detectedFaces)

            val resultText: String = createResultText(detectedFaces)

            return ImageProcessingResult(annotatedImage, resultText)
        }

        private fun createResultText(detectedFaces: List<FirebaseVisionFace>): String {
            return "Faces count: ${detectedFaces.size}"
        }

        private fun annotateImage(originalImage: Bitmap, detectedFaces: List<FirebaseVisionFace>): Bitmap {
            val annotatedBitmap = Bitmap.createBitmap(originalImage.width, originalImage.height, Bitmap.Config.RGB_565)

            val canvas = Canvas(annotatedBitmap)
            canvas.drawBitmap(originalImage, 0f, 0f, null)

            detectedFaces.forEach {
                drawFace(canvas, it)
            }

            return annotatedBitmap
        }

        private fun drawFace(canvas: Canvas, face: FirebaseVisionFace) {
            canvas.drawRect(face.boundingBox, faceBoundingBoxPaint)

            landmarksToDraw
                    .map { face.getLandmark(it) }
                    .forEach {
                        if (it != null) {
                            drawLandmark(canvas, it)
                        }
                    }
        }

        private fun drawLandmark(canvas: Canvas, faceLandmark: FirebaseVisionFaceLandmark) {
            with(faceLandmark.position) {
                canvas.drawCircle(x, y, LANDMARK_DRAWING_SIZE, faceLandmarksPaint)
            }
        }

        private const val LANDMARK_DRAWING_SIZE = 15.0f

        private val faceBoundingBoxPaint =
                Paint().apply {
                    strokeWidth = 5f
                    color = Color.MAGENTA
                    style = Paint.Style.STROKE
                }

        private val faceLandmarksPaint =
                Paint().apply {
                    strokeWidth = 5f
                    color = Color.MAGENTA
                    style = Paint.Style.FILL
                }

        private val landmarksToDraw = arrayListOf(
                FirebaseVisionFaceLandmark.BOTTOM_MOUTH,
                FirebaseVisionFaceLandmark.LEFT_CHEEK,
                FirebaseVisionFaceLandmark.LEFT_EAR,
                FirebaseVisionFaceLandmark.LEFT_EYE,
                FirebaseVisionFaceLandmark.LEFT_MOUTH,
                FirebaseVisionFaceLandmark.NOSE_BASE,
                FirebaseVisionFaceLandmark.RIGHT_CHEEK,
                FirebaseVisionFaceLandmark.RIGHT_EAR,
                FirebaseVisionFaceLandmark.RIGHT_EYE,
                FirebaseVisionFaceLandmark.RIGHT_MOUTH)
    }
}