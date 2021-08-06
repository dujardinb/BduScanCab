package fr.vrvd.dsi.bduscancablibrary

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(private val barcodeListener: BarcodeListener) : ImageAnalysis.Analyzer {

    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(getFormatCab())
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
       // Log.d(TAG, "analyze")
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to the scanner and have it do its thing
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    for (barcode in barcodes) {
                        barcodeListener(barcode.rawValue ?: "")
                    }
                }
                .addOnFailureListener {
                    // You should really do something about Exceptions
                }
                .addOnCompleteListener {
                    // It's important to close the imageProxy
                    imageProxy.close()
                }
        }
    }

    fun getFormatCab(): Int {
        val pref = StoreData.getContext().getSharedPreferences(scanQR.PARAM_PREF, AppCompatActivity.MODE_PRIVATE)
        return pref.getInt(scanQR.FORMAT_DATA, 256)
    }


    companion object {
        const val TAG = "MYTEST"
        const val PARAM_PREF = "PARAM_PREF"
        const val FORMAT_DATA = "FORMAT_DATA"
    }
}