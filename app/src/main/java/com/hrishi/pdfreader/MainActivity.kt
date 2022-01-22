package com.hrishi.pdfreader

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hrishi.pdfreader.databinding.ActivityMainBinding
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.lang.Exception

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    lateinit var binding: ActivityMainBinding
    lateinit var tts: TextToSpeech
    lateinit var reader: PdfReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TextToSpeech(this, this)

        val selectPdfResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if(loadPdf(it)){
                setPageContent(1)
            }
        }

        binding.fabSelectFile.setOnClickListener {
            pickFile(selectPdfResult)
        }

        binding.fabNextPage.setOnClickListener {
            if(::reader.isInitialized){
                var currentPgNo = Integer.parseInt(binding.tvCurrentPgNo.text.toString())
                if(currentPgNo < reader.numberOfPages){
                    currentPgNo++
                    setPageContent(currentPgNo)
                    binding.tvCurrentPgNo.text = currentPgNo.toString()
                }
            }
        }

        binding.fabPreviousPage.setOnClickListener {
            if(::reader.isInitialized){
                var currentPgNo = Integer.parseInt(binding.tvCurrentPgNo.text.toString())
                if(1 < currentPgNo){
                    currentPgNo--
                    setPageContent(currentPgNo)
                    binding.tvCurrentPgNo.text = currentPgNo.toString()
                }
            }
        }
    }

    //Opens File Picker
    private fun pickFile(selectPdfResult : ActivityResultLauncher<String>){
        try{
            selectPdfResult.launch("application/pdf")
        }catch (e : ActivityNotFoundException){
            Toast.makeText(this, "No File Picker Found", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "pickFile: ${e.message}", )
        }
    }

    //Sets the reader
    private fun loadPdf(uri : Uri) : Boolean{

        try {
            reader = PdfReader(contentResolver.openInputStream(uri))

            if(reader.numberOfPages == 0){
                Toast.makeText(this, "Empty Pdf", Toast.LENGTH_SHORT).show()
                return false
            }

            binding.tvTotalPages.text = reader.numberOfPages.toString()
            binding.tvPgNoSeperator.text = "/"
            binding.tvCurrentPgNo.text = "1"
            return true

        }catch (e : Exception){
            Toast.makeText(this, "Error While Reading PDF", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "loadPdf: ${e.message}", )
            return false
        }

    }

    //Sets Text of Page to the TextView
    private fun setPageContent(pageNo : Int){

        if(pageNo <= reader.numberOfPages){
            binding.tvPageContent.setText("Page $pageNo \n" + PdfTextExtractor.getTextFromPage(reader, pageNo))
        }
    }

    //For TextToSpeech Init
    override fun onInit(p0: Int) {
        Toast.makeText(this, "Init", Toast.LENGTH_SHORT).show()
    }

    //Speaks the text with TextToSpeech
    private fun speak(text: String) =
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "PdfReader")

}

