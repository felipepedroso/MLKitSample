package br.pedroso.mlkitsample.presentation.mappers

import android.graphics.Bitmap
import br.pedroso.mlkitsample.presentation.ImageProcessingResult
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel

class LabelRemoteResultMapper {
    companion object {
        fun mapResult(originalImage: Bitmap, labels: List<FirebaseVisionImageLabel>): ImageProcessingResult {
            val resultText: String = createResultText(labels)

            return ImageProcessingResult(originalImage, resultText)
        }

        private fun createResultText(labels: List<FirebaseVisionImageLabel>) = if (labels.isNotEmpty()) {
            labels.map { it.text }.joinToString()
        } else {
            "No labels available!"
        }
    }
}