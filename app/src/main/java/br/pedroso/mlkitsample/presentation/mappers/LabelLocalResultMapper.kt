package br.pedroso.mlkitsample.presentation.mappers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import br.pedroso.mlkitsample.presentation.ImageProcessingResult
import com.google.firebase.ml.vision.label.FirebaseVisionLabel

class LabelLocalResultMapper {
    companion object {
        fun mapResult(originalImage: Bitmap, labels: List<FirebaseVisionLabel>): ImageProcessingResult {
            val resultText: String = createResultText(labels)

            return ImageProcessingResult(originalImage, resultText)
        }

        private fun createResultText(labels: List<FirebaseVisionLabel>) = if (labels.isNotEmpty()) {
            labels.map { it.label }.joinToString()
        } else {
            "No labels available!"
        }
    }
}