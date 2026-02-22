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
    private val mathValidator = MathValidator()
    private val hintGenerator = HintGenerator()
    
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
            
            // If step is complete and correct, add it to history
            if (feedback.currentStepValid && isStepComplete(latexWork)) {
                addCompletedStep(recognizedText, latexWork, true, feedback)
            } else if (!feedback.currentStepValid && isStepComplete(latexWork)) {
                // Track incorrect attempts
                incrementAttemptCount()
            }
            
            // Check if problem is solved
            if (isSolutionComplete(latexWork)) {
                markSolutionComplete()
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
        // Get previous step (if exists)
        val currentState = _solutionState.value
        val previousStep = currentState?.userSteps?.lastOrNull()
        
        // Validate current step against previous step
        val validation = if (previousStep != null) {
            mathValidator.validateStep(previousStep.userWork, userWork)
        } else {
            // First step - validate against original problem
            mathValidator.validateStep(problem.latexEquation, userWork)
        }
        
        // Generate contextual hint if step is incorrect
        val hint = if (!validation.isValid) {
            hintGenerator.generateHint(
                errorType = validation.errorType,
                problemType = detectProblemType(problem.latexEquation),
                currentWork = userWork,
                stepNumber = stepNumber
            )
        } else {
            // Provide encouragement and next step suggestion
            hintGenerator.generateNextStepSuggestion(userWork, stepNumber)
        }
        
        // Calculate confidence based on validation certainty
        val confidence = if (validation.isValid) 0.9f else 0.85f
        
        return AIFeedback(
            isOnTrack = validation.isValid,
            currentStepValid = validation.isValid,
            subtleHint = hint,
            suggestedNextStep = if (validation.isValid) hintGenerator.suggestNextStep(userWork) else null,
            confidence = confidence
        )
    }
    
    /**
     * Detect problem type for contextual hints
     */
    private fun detectProblemType(equation: String): ProblemType {
        val cleanEq = equation.replace("$", "").trim()
        
        return when {
            cleanEq.contains("x^2") || cleanEq.contains("x²") -> ProblemType.QUADRATIC
            cleanEq.contains("sqrt") || cleanEq.contains("√") -> ProblemType.RADICAL
            cleanEq.contains("/") -> ProblemType.RATIONAL
            cleanEq.contains("sin") || cleanEq.contains("cos") || cleanEq.contains("tan") -> ProblemType.TRIGONOMETRIC
            cleanEq.contains("log") || cleanEq.contains("ln") -> ProblemType.LOGARITHMIC
            Regex("\\d*x").containsMatchIn(cleanEq) -> ProblemType.LINEAR
            else -> ProblemType.ARITHMETIC
        }
    }
    
    /**
     * Simple validation logic (would be replaced with AI)
     */
    private fun validateStep(problem: MathProblem, userWork: String, stepNumber: Int): Boolean {
        // Use MathValidator for real algebraic validation
        val currentState = _solutionState.value
        val previousStep = currentState?.userSteps?.lastOrNull()
        
        val validation = if (previousStep != null) {
            mathValidator.validateStep(previousStep.userWork, userWork)
        } else {
            mathValidator.validateStep(problem.latexEquation, userWork)
        }
        
        return validation.isValid
    }
    
    /**
     * Generate contextual hints without giving away the answer
     */
    private fun generateHint(problem: MathProblem, userWork: String, stepNumber: Int): String {
        val problemType = detectProblemType(problem.latexEquation)
        
        return hintGenerator.generateHint(
            errorType = ErrorType.WRONG_APPROACH, // Default if we can't detect specific error
            problemType = problemType,
            currentWork = userWork,
            stepNumber = stepNumber
        )
    }
    
    private fun isStepComplete(work: String): Boolean {
        // Check if the step seems complete (has equation, no trailing operators, etc.)
        return work.contains("=") && !work.endsWith("+") && !work.endsWith("-") && 
               !work.endsWith("*") && !work.endsWith("/")
    }
    
    private fun addCompletedStep(userWork: String, latexWork: String, isCorrect: Boolean, feedback: AIFeedback) {
        val currentState = _solutionState.value ?: return
        
        val newStep = SolutionStep(
            stepNumber = currentState.currentStep,
            userWork = latexWork,
            expectedWork = "", // Could be calculated by solving the problem
            isCorrect = isCorrect,
            hint = feedback.subtleHint,
            errorType = null // No error if correct
        )
        
        // Calculate score based on correctness and hints used
        val stepScore = if (isCorrect) {
            1.0f - (currentState.hintsUsed * 0.1f).coerceAtMost(0.5f)
        } else {
            0.0f
        }
        
        val totalScore = if (currentState.userSteps.isEmpty()) {
            stepScore
        } else {
            (currentState.score * currentState.userSteps.size + stepScore) / (currentState.userSteps.size + 1)
        }
        
        _solutionState.value = currentState.copy(
            userSteps = currentState.userSteps + newStep,
            currentStep = currentState.currentStep + 1,
            score = totalScore,
            hintsUsed = if (feedback.subtleHint != null) currentState.hintsUsed + 1 else currentState.hintsUsed
        )
    }
    
    private fun incrementAttemptCount() {
        val currentState = _solutionState.value ?: return
        _solutionState.value = currentState.copy(
            attemptCount = currentState.attemptCount + 1
        )
    }
    
    private fun isSolutionComplete(work: String): Boolean {
        // Check if this looks like a final answer: x = [number]
        val cleaned = work.replace(" ", "").toLowerCase()
        return cleaned.matches(Regex("x=[-]?[0-9.]+(/[-]?[0-9.]+)?"))
    }
    
    private fun markSolutionComplete() {
        val currentState = _solutionState.value ?: return
        
        _solutionState.value = currentState.copy(
            isComplete = true,
            endTime = System.currentTimeMillis()
        )
        
        // Generate completion feedback
        _aiFeedback.value = AIFeedback(
            isOnTrack = true,
            currentStepValid = true,
            subtleHint = generateCompletionMessage(currentState),
            confidence = 1.0f
        )
    }
    
    private fun generateCompletionMessage(state: SolutionState): String {
        val timeTaken = (System.currentTimeMillis() - state.startTime) / 1000 // seconds
        val score = (state.score * 100).toInt()
        
        return when {
            score >= 90 -> "Excellent work! You solved it with minimal hints in ${timeTaken}s."
            score >= 70 -> "Great job! You completed the problem successfully."
            score >= 50 -> "Good effort! You reached the solution."
            else -> "You got there! Keep practicing to improve your efficiency."
        }
    }
    
    /**
     * Get solution history for review
     */
    fun getSolutionHistory(): List<SolutionStep> {
        return _solutionState.value?.userSteps ?: emptyList()
    }
    
    /**
     * Get solution statistics
     */
    fun getSolutionStats(): SolutionStats? {
        val state = _solutionState.value ?: return null
        val timeTaken = if (state.endTime != null) {
            state.endTime - state.startTime
        } else {
            System.currentTimeMillis() - state.startTime
        }
        
        return SolutionStats(
            totalSteps = state.userSteps.size,
            correctSteps = state.userSteps.count { it.isCorrect },
            incorrectAttempts = state.attemptCount,
            hintsUsed = state.hintsUsed,
            timeTaken = timeTaken,
            score = state.score
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
