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

class ScanCabActivity : AppCompatActivity() {
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
        val beep = intent.getIntExtra(BEEP,0)
        setFormatCab(formatCab)
        setVibrate(vibrate)
        setBeep(beep)
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
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val  barcodeViewRessource:androidx.camera.view.PreviewView =
                findViewById(R.id.fragment_scan_barcode_preview_view)
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(
                    barcodeViewRessource.surfaceProvider
                )
            }

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


            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun searchBarcode(barcode: String) {
        Log.d(Companion.TAG, "searchBarcode:$barcode ")

        if(getVibrate() > 0) LibApp.vibrate(this)
        if(getBeep() > 0) LibApp.beep(1)
        val returnIntent = Intent()
        returnIntent.putExtra("result", barcode)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    companion object {
        const val TAG = "**DEBUG**"
        const val FORMAT_CAB =  "FORMAT_CAB"
        const val VIBRATE =  "VIBRATE"
        const val BEEP =  "BEEP"

        const val PARAM_PREF = "PARAM_PREF"
        const val FORMAT_DATA = "FORMAT_DATA"
        const val PARAM_VIBRATE = "PARAM_VIBRATE"
        const val PARAM_BEEP = "PARAM_BEEP"

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

    fun setBeep(value: Int) {
        val pref = getSharedPreferences(PARAM_PREF, MODE_PRIVATE)
        val editor = pref.edit()
        if (pref.contains(PARAM_BEEP)) editor.remove(PARAM_BEEP)
        editor.putInt(PARAM_BEEP, value)
        editor.commit()
    }

    fun getBeep(): Int {
        val pref = getSharedPreferences(PARAM_PREF, MODE_PRIVATE)
        return pref.getInt(PARAM_BEEP, 0)
    }

}
