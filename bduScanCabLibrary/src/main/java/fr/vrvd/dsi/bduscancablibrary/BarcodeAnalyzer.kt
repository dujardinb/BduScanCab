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
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcodeListener(barcode.rawValue ?: "")
                    }
                }
                .addOnFailureListener {
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    fun getFormatCab(): Int {
        val pref = StoreData.getContext().getSharedPreferences(ScanCabActivity.PARAM_PREF, AppCompatActivity.MODE_PRIVATE)
        return pref.getInt(ScanCabActivity.FORMAT_DATA, 256)
    }

}