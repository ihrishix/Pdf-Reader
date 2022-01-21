package com.hrishi.pdfreader

import android.app.Activity
import android.content.ContentProvider
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hrishi.pdfreader.databinding.ActivityMainBinding
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.net.URL

val PICK_PDF_FILE = 2

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSelectFile.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"

            }


            startActivityForResult(intent, PICK_PDF_FILE)

        }


    }


    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == PICK_PDF_FILE
            && resultCode == Activity.RESULT_OK
        ) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->
                // Perform operations on the document using its URI.
                binding.textView.text = uri.toString()
               // val reader = PdfReader(URL(uri.toString()))

                val urri = Uri.parse("/storage/emulated/0/Download/hello.pdf")
                Log.d("Main", "onActivityResult: ${urri.toString()}")
                val reader = PdfReader(contentResolver.openInputStream(uri))

                binding.textView.text = PdfTextExtractor.getTextFromPage(reader, 3)
                    //val f = PdfTextExtractor.getTextFromPage(reader, 1)




            }
        }
    }

}

