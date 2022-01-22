package com.hrishi.pdfreader

import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hrishi.pdfreader.databinding.ActivityMainBinding
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor

val PICK_PDF_FILE = 2

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    lateinit var binding: ActivityMainBinding
    lateinit var reader: PdfReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.numPicker.maxValue = 10
        binding.numPicker.minValue = 2
        val tts = TextToSpeech(this, this)


        val acti = registerForActivityResult(ActivityResultContracts.GetContent()) {

            var out = StringBuilder("")

            for (i in 1..reader.numberOfPages) {
                out.append(PdfTextExtractor.getTextFromPage(reader, i))
            }
            binding.textView.text = out
            tts.speak(out, TextToSpeech.QUEUE_FLUSH, null, "pdfReader")
        }

        binding.btnSelectFile.setOnClickListener {

            acti.launch("application/pdf")


        }


    }

    private fun loadPdf(uri : Uri){
        reader = PdfReader(contentResolver.openInputStream(uri))
    }

    private fun setPageContent(pageNo : Int){

        if(pageNo <= reader.numberOfPages){
            binding.tvPageContent.text = "Page $pageNo \n" + PdfTextExtractor.getTextFromPage(reader, pageNo)
        }
    }

    override fun onInit(p0: Int) {
        Toast.makeText(this, "Init", Toast.LENGTH_SHORT).show()
    }


}

