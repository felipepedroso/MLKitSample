package br.pedroso.mlkitsample.presentation.mappers

import android.graphics.Bitmap

class RemoteImageSizeMapper {
    companion object {
        fun resizeImageToFitRemoteLabeling(bitmap: Bitmap): Bitmap = with(bitmap) {
            if (width > height && width > 640) {
                Bitmap.createScaledBitmap(this, 640, 480, false)
            } else if (width < height && height > 640) {
                Bitmap.createScaledBitmap(this, 480, 640, false)
            } else if (width == height && width > 640) {
                Bitmap.createScaledBitmap(this, 550, 550, false)
            } else {
                this
            }
        }
    }
}