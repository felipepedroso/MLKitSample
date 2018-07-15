package br.pedroso.mlkitsample.presentation

import android.graphics.Bitmap

data class ImageProcessingResult(
        val annotatedImage: Bitmap,
        val resultText: String)