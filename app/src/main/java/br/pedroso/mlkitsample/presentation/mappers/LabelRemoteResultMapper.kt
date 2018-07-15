package br.pedroso.mlkitsample.presentation.mappers

import android.graphics.Bitmap
import br.pedroso.mlkitsample.presentation.ImageProcessingResult
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel

class LabelRemoteResultMapper {
    companion object {
        fun mapResult(originalImage: Bitmap, labels: List<FirebaseVisionCloudLabel>): ImageProcessingResult {
            val resultText: String = createResultText(labels)

            return ImageProcessingResult(originalImage, resultText)
        }

        private fun createResultText(labels: List<FirebaseVisionCloudLabel>) = if (labels.isNotEmpty()) {
            labels.map { it.label }.joinToString()
        } else {
            "No labels available!"
        }
    }
}