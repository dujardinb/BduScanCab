package fr.vrvd.dsi.bduscancablibrary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean


typealias BarcodeListener = ( barcode: String) -> Unit


class scanQR : AppCompatActivity() {
    private var processingBarcode = AtomicBoolean(false)
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var scanQRContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(Companion.TAG, "onCreate")
        setContentView(R.layout.activity_scan_qr)
        cameraExecutor = Executors.newSingleThreadExecutor()
        scanQRContext = this
        PermUtility.permissionCheck(this)

        val intent = this.intent
        val formatCab = intent.getIntExtra(FORMAT_CAB,getResources().getInteger(R.integer.FORMAT_QR_CODE))
        StoreData.setContext(this);
        val vibrate = intent.getIntExtra(VIBRATE,0)
        setFormatCab(formatCab)
        setVibrate(vibrate)
        startCamera()
    }

    override fun onResume() {
        super.onResume()
        Log.d(Companion.TAG, "onResume")
        processingBarcode.set(false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d(Companion.TAG, "onBackPressed")
        cameraExecutor.shutdown()
        finish()
    }

    override fun onDestroy() {
        Log.d(Companion.TAG, "onDestroy")
        super.onDestroy()
        cameraExecutor.shutdown()
        finish()
    }


    private fun startCamera() {
        Log.d(Companion.TAG, "startCamera")
        // Create an instance of the ProcessCameraProvider,
        // which will be used to bind the use cases to a lifecycle owner.
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        // Add a listener to the cameraProviderFuture.
        // The first argument is a Runnable, which will be where the magic actually happens.
        // The second argument (way down below) is an Executor that runs on the main thread.
        cameraProviderFuture.addListener({
            // Add a ProcessCameraProvider, which binds the lifecycle of your camera to
            // the LifecycleOwner within the application's life.
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Initialize the Preview object, get a surface provider from your PreviewView,
            // and set it on the preview instance.
            val  barcodeViewRessource:androidx.camera.view.PreviewView =
                findViewById(R.id.fragment_scan_barcode_preview_view)
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(

                    barcodeViewRessource.surfaceProvider
                )
            }
            // Setup the ImageAnalyzer for the ImageAnalysis use case
            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                        if (processingBarcode.compareAndSet(false, true)) {
                            Log.d("TRACE","searchBarcode - scanQT" )
                            searchBarcode(barcode)
                        }
                    })
                }

            // Select back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind any bound use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to lifecycleOwner
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun searchBarcode(barcode: String) {
        Log.d(Companion.TAG, "searchBarcode:$barcode ")

        if(getVibrate() > 0) LibApp.vibrate(this)
        val returnIntent = Intent()
        returnIntent.putExtra("result", barcode)
        setResult(RESULT_OK, returnIntent)
        finish()
    }


    companion object {
        const val TAG = "**DEBUG**"
        const val FORMAT_CAB =  "FORMAT_CAB"
        const val VIBRATE =  "VIBRATE"

        const val PARAM_PREF = "PARAM_PREF"
        const val FORMAT_DATA = "FORMAT_DATA"
        const val PARAM_VIBRATE = "PARAM_VIBRATE"

    }

    fun setFormatCab(value: Int) {
        val pref = getSharedPreferences(PARAM_PREF, MODE_PRIVATE)
        val editor = pref.edit()
        if (pref.contains(FORMAT_DATA)) editor.remove(FORMAT_DATA)
        editor.putInt(FORMAT_DATA, value)
        editor.commit()
    }

    fun setVibrate(value: Int) {
        val pref = getSharedPreferences(PARAM_PREF, MODE_PRIVATE)
        val editor = pref.edit()
        if (pref.contains(PARAM_VIBRATE)) editor.remove(PARAM_VIBRATE)
        editor.putInt(PARAM_VIBRATE, value)
        editor.commit()
    }

    fun getVibrate(): Int {
        val pref = getSharedPreferences(PARAM_PREF, MODE_PRIVATE)
        return pref.getInt(PARAM_VIBRATE, 0)
    }

}
