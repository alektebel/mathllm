package com.mathdraw.app

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MathDrawViewModel : ViewModel() {
    private val _strokes = MutableStateFlow<List<Stroke>>(emptyList())
    val strokes: StateFlow<List<Stroke>> = _strokes

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText

    private val _latexEquation = MutableStateFlow("")
    val latexEquation: StateFlow<String> = _latexEquation

    private var currentStroke: MutableList<StrokePoint> = mutableListOf()
    private var recognizer: DigitalInkRecognizer? = null
    private var inkBuilder = Ink.builder()
    private var strokeBuilder: Ink.Stroke.Builder? = null

    fun initializeRecognizer(context: Context) {
        viewModelScope.launch {
            try {
                // Use specialized math model for better symbol recognition
                // The "zxx-x-math" identifier is specifically for mathematical notation
                val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("zxx-x-math")
                    ?: DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
                
                if (modelIdentifier != null) {
                    val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
                    val options = DigitalInkRecognizerOptions.builder(model).build()
                    recognizer = DigitalInkRecognition.getClient(options)
                    
                    // Download model if needed
                    com.google.mlkit.common.model.RemoteModelManager.getInstance()
                        .download(model, com.google.mlkit.common.model.DownloadConditions.Builder().build())
                        .await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startStroke(offset: Offset) {
        currentStroke = mutableListOf()
        strokeBuilder = Ink.Stroke.builder()
    }

    fun addPointToStroke(offset: Offset) {
        val timestamp = System.currentTimeMillis()
        currentStroke.add(StrokePoint(offset.x, offset.y, timestamp))
        strokeBuilder?.addPoint(Ink.Point.create(offset.x, offset.y, timestamp))
    }

    fun endStroke() {
        if (currentStroke.isNotEmpty()) {
            val newStroke = Stroke(currentStroke.toList())
            _strokes.value = _strokes.value + newStroke
            
            // Add to ink for recognition
            strokeBuilder?.let {
                inkBuilder.addStroke(it.build())
            }
            
            // Recognize after a short delay
            viewModelScope.launch {
                kotlinx.coroutines.delay(500)
                recognizeInk()
            }
        }
        currentStroke = mutableListOf()
        strokeBuilder = null
    }

    fun clearCanvas() {
        _strokes.value = emptyList()
        _recognizedText.value = ""
        _latexEquation.value = ""
        inkBuilder = Ink.builder()
        currentStroke = mutableListOf()
        strokeBuilder = null
    }

    fun undo() {
        if (_strokes.value.isNotEmpty()) {
            _strokes.value = _strokes.value.dropLast(1)
            
            // Rebuild ink
            inkBuilder = Ink.builder()
            _strokes.value.forEach { stroke ->
                val builder = Ink.Stroke.builder()
                stroke.points.forEach { point ->
                    builder.addPoint(Ink.Point.create(point.x, point.y, point.timestamp))
                }
                inkBuilder.addStroke(builder.build())
            }
            
            // Re-recognize
            viewModelScope.launch {
                recognizeInk()
            }
        }
    }

    fun recognizeNow() {
        viewModelScope.launch {
            recognizeInk()
        }
    }

    private suspend fun recognizeInk() {
        if (_strokes.value.isEmpty()) {
            _recognizedText.value = ""
            _latexEquation.value = ""
            return
        }

        try {
            val ink = inkBuilder.build()
            val result = recognizer?.recognize(ink)?.await()
            
            result?.candidates?.firstOrNull()?.text?.let { text ->
                _recognizedText.value = text
                _latexEquation.value = convertToLatex(text)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _recognizedText.value = "Recognition failed"
        }
    }

    private fun convertToLatex(text: String): String {
        // Use the comprehensive symbol converter
        return MathSymbolConverter.toLatex(text)
    }

    override fun onCleared() {
        super.onCleared()
        recognizer?.close()
    }
}
