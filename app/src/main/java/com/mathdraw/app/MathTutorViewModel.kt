package com.mathdraw.app

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class MathTutorViewModel : ViewModel() {
    
    private val _currentProblem = MutableStateFlow<MathProblem?>(null)
    val currentProblem: StateFlow<MathProblem?> = _currentProblem
    
    private val _solutionState = MutableStateFlow<SolutionState?>(null)
    val solutionState: StateFlow<SolutionState?> = _solutionState
    
    private val _aiFeedback = MutableStateFlow<AIFeedback?>(null)
    val aiFeedback: StateFlow<AIFeedback?> = _aiFeedback
    
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing
    
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    /**
     * Capture and process a math problem from an image
     */
    fun processProblemImage(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                // Load image
                val image = InputImage.fromFilePath(context, imageUri)
                
                // Extract text using ML Kit OCR
                val result = textRecognizer.process(image).await()
                val extractedText = result.text
                
                // Convert to LaTeX (simplified - would need more sophisticated parsing)
                val latexEquation = extractedTextToLatex(extractedText)
                
                // Create problem
                val problem = MathProblem(
                    id = UUID.randomUUID().toString(),
                    imageUri = imageUri,
                    extractedText = extractedText,
                    latexEquation = latexEquation
                )
                
                _currentProblem.value = problem
                
                // Initialize solution state
                _solutionState.value = SolutionState(
                    problem = problem
                )
                
                // Generate initial AI guidance
                generateInitialHints(problem)
                
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isProcessing.value = false
            }
        }
    }
    
    /**
     * Process user's handwritten work in real-time
     */
    fun processUserWork(recognizedText: String, latexWork: String) {
        viewModelScope.launch {
            val currentState = _solutionState.value ?: return@launch
            
            // Analyze current step
            val feedback = analyzeCurrentStep(
                problem = currentState.problem,
                userWork = latexWork,
                stepNumber = currentState.currentStep
            )
            
            _aiFeedback.value = feedback
            
            // If step is complete and correct, move to next step
            if (feedback.currentStepValid && isStepComplete(latexWork)) {
                addCompletedStep(recognizedText, latexWork, true)
            }
        }
    }
    
    /**
     * AI analyzes if the current work is correct and provides hints
     */
    private suspend fun analyzeCurrentStep(
        problem: MathProblem,
        userWork: String,
        stepNumber: Int
    ): AIFeedback {
        // This is a simplified version. In production, you'd use:
        // 1. Google AI Edge (Gemini Nano) for on-device reasoning
        // 2. Or a cloud-based math solver API
        // 3. Or a fine-tuned SLM for math
        
        // For now, simple pattern matching and heuristics
        val isCorrect = validateStep(problem, userWork, stepNumber)
        val hint = if (!isCorrect) generateHint(problem, userWork, stepNumber) else null
        
        return AIFeedback(
            isOnTrack = isCorrect,
            currentStepValid = isCorrect,
            subtleHint = hint,
            confidence = 0.85f
        )
    }
    
    /**
     * Simple validation logic (would be replaced with AI)
     */
    private fun validateStep(problem: MathProblem, userWork: String, stepNumber: Int): Boolean {
        // Simplified validation - real implementation would use AI
        // Check for common patterns, algebraic correctness, etc.
        
        // For demo: just check if user is writing something reasonable
        return userWork.isNotEmpty() && userWork.length > 3
    }
    
    /**
     * Generate contextual hints without giving away the answer
     */
    private fun generateHint(problem: MathProblem, userWork: String, stepNumber: Int): String {
        // This would use an SLM to generate subtle, contextual hints
        // For now, return generic hints based on step
        
        return when (stepNumber) {
            0 -> "What operation should you apply to both sides first?"
            1 -> "Remember to simplify before proceeding"
            2 -> "Check your signs carefully"
            else -> "Review the previous step - something doesn't look right"
        }
    }
    
    private fun isStepComplete(work: String): Boolean {
        // Check if the step seems complete (has equation, no trailing operators, etc.)
        return work.contains("=") && !work.endsWith("+") && !work.endsWith("-")
    }
    
    private fun addCompletedStep(userWork: String, latexWork: String, isCorrect: Boolean) {
        val currentState = _solutionState.value ?: return
        
        val newStep = SolutionStep(
            stepNumber = currentState.currentStep,
            userWork = latexWork,
            expectedWork = "", // Would be filled by AI
            isCorrect = isCorrect
        )
        
        _solutionState.value = currentState.copy(
            userSteps = currentState.userSteps + newStep,
            currentStep = currentState.currentStep + 1
        )
    }
    
    private fun extractedTextToLatex(text: String): String {
        // Convert OCR text to LaTeX
        // This is simplified - would need sophisticated parsing
        var latex = text.trim()
        
        // Basic conversions
        latex = latex.replace("^", "^{").replace(" ", "")
        // More sophisticated parsing needed for real use
        
        return "$$latex$"
    }
    
    private suspend fun generateInitialHints(problem: MathProblem) {
        // AI generates approach suggestions without solving
        val initialFeedback = AIFeedback(
            isOnTrack = true,
            currentStepValid = true,
            subtleHint = "Start by identifying what you're solving for",
            confidence = 1.0f
        )
        
        _aiFeedback.value = initialFeedback
    }
    
    fun clearProblem() {
        _currentProblem.value = null
        _solutionState.value = null
        _aiFeedback.value = null
    }
}
