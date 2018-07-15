package br.pedroso.mlkitsample.utils

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

val WORKER_SCHEDULER = Schedulers.io()

val UI_SCHEDULER = AndroidSchedulers.mainThread()