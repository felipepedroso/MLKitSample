package br.pedroso.mlkitsample.presentation.mappers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import br.pedroso.mlkitsample.presentation.ImageProcessingResult
import com.google.firebase.ml.vision.text.FirebaseVisionText

class TextDetectionResultMapper {
    companion object {
        fun mapResult(originalImage: Bitmap, detectedText: FirebaseVisionText): ImageProcessingResult {
            val annotatedImage = annotateImage(originalImage, detectedText)

            val resultText: String = createResultText(detectedText)

            return ImageProcessingResult(annotatedImage, resultText)
        }

        private fun createResultText(detectedText: FirebaseVisionText): String {
            val blocksAmount = detectedText.blocks.flatMap { it.lines }.flatMap { it.elements }.size

            return "Elements detected: $blocksAmount"
        }

        private fun annotateImage(originalImage: Bitmap, detectedText: FirebaseVisionText): Bitmap {
            val annotatedBitmap = Bitmap.createBitmap(originalImage.width, originalImage.height, Bitmap.Config.RGB_565)

            val canvas = Canvas(annotatedBitmap)
            canvas.drawBitmap(originalImage, 0f, 0f, null)

            detectedText.blocks.forEach {
                canvas.drawRect(it.boundingBox, textBoundingBoxPaint)

                it.lines.flatMap { it.elements }.forEach {
                    canvas.drawRect(it.boundingBox, textBoundingBoxPaint)
                }
            }

            return annotatedBitmap
        }

        private val textBoundingBoxPaint =
                Paint().apply {
                    strokeWidth = 5f
                    color = Color.MAGENTA
                    style = Paint.Style.STROKE
                }
    }
}