package br.pedroso.mlkitsample.presentation

import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import br.pedroso.mlkitsample.utils.WORKER_SCHEDULER
import br.pedroso.mlkitsample.mlkit.RxMLKit
import br.pedroso.mlkitsample.presentation.mappers.*
import io.reactivex.Observable

class MainActivityViewModel : ViewModel() {
    fun detectFaces(bitmap: Bitmap): Observable<ImageProcessingResult> {
        return RxMLKit.detectFaces(bitmap)
                .map { FaceDetectionResultMapper.mapResult(bitmap, it) }
                .subscribeOn(WORKER_SCHEDULER)
    }

    fun detectText(bitmap: Bitmap): Observable<ImageProcessingResult> {
        return RxMLKit.detectText(bitmap)
                .map { TextDetectionResultMapper.mapResult(bitmap, it) }
                .subscribeOn(WORKER_SCHEDULER)
    }

    fun labelRemote(bitmap: Bitmap): Observable<ImageProcessingResult> {
        return Observable.just(bitmap)
                .map { RemoteImageSizeMapper.resizeImageToFitRemoteLabeling(it) }
                .flatMap { RxMLKit.labelImageRemote(it) }
                .map { LabelRemoteResultMapper.mapResult(bitmap, it) }
                .subscribeOn(WORKER_SCHEDULER)
    }

    fun labelLocal(bitmap: Bitmap): Observable<ImageProcessingResult> {
        return RxMLKit.labelImageLocal(bitmap)
                .map { LabelLocalResultMapper.mapResult(bitmap, it) }
                .subscribeOn(WORKER_SCHEDULER)
    }
}