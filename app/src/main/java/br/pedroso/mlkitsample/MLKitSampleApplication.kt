package br.pedroso.mlkitsample

import android.app.Application
import com.google.firebase.FirebaseApp

class MLKitSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}