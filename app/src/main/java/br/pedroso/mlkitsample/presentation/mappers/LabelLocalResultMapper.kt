package br.pedroso.mlkitsample.presentation.mappers

import android.graphics.Bitmap
import br.pedroso.mlkitsample.presentation.ImageProcessingResult
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel

class LabelLocalResultMapper {
    companion object {
        fun mapResult(
                originalImage: Bitmap,
                labels: List<FirebaseVisionImageLabel>
        ) = ImageProcessingResult(originalImage, createResultText(labels))

        private fun createResultText(labels: List<FirebaseVisionImageLabel>) =
                if (labels.isNotEmpty()) {
                    labels.joinToString { it.text }
                } else {
                    "No labels available!"
                }
    }
}