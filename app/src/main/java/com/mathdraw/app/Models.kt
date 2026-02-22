package com.mathdraw.app

import android.net.Uri

/**
 * Represents a math problem captured from a book or entered manually
 */
data class MathProblem(
    val id: String,
    val imageUri: Uri? = null,
    val extractedText: String = "",
    val latexEquation: String = "",
    val difficulty: Difficulty = Difficulty.MEDIUM
)

enum class Difficulty {
    EASY, MEDIUM, HARD
}

/**
 * Represents a step in the solution process
 */
data class SolutionStep(
    val stepNumber: Int,
    val userWork: String,  // What the user wrote
    val expectedWork: String,  // What should be written
    val isCorrect: Boolean,
    val hint: String? = null,
    val errorType: ErrorType? = null
)

enum class ErrorType {
    ALGEBRAIC_MISTAKE,
    SIGN_ERROR,
    CALCULATION_ERROR,
    WRONG_APPROACH,
    INCOMPLETE_STEP
}

/**
 * Represents the AI's analysis of the current solution attempt
 */
data class AIFeedback(
    val isOnTrack: Boolean,
    val currentStepValid: Boolean,
    val subtleHint: String? = null,
    val suggestedNextStep: String? = null,
    val confidence: Float = 0.5f  // 0.0 to 1.0
)

/**
 * Complete solution state
 */
data class SolutionState(
    val problem: MathProblem,
    val userSteps: List<SolutionStep> = emptyList(),
    val currentStep: Int = 0,
    val isComplete: Boolean = false,
    val score: Float = 0f
)
