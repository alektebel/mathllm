package com.mathdraw.app

/**
 * Types of math problems for contextual hint generation
 */
enum class ProblemType {
    LINEAR,           // ax + b = c
    QUADRATIC,        // ax^2 + bx + c = 0
    RATIONAL,         // Equations with fractions
    RADICAL,          // Equations with square roots
    TRIGONOMETRIC,    // sin, cos, tan equations
    LOGARITHMIC,      // log/ln equations
    ARITHMETIC,       // Simple arithmetic
    SYSTEM,           // System of equations
    INEQUALITY        // Inequalities
}

/**
 * Generates contextual hints based on error type and problem context
 */
class HintGenerator {
    
    /**
     * Generate a subtle hint based on the error and context
     */
    fun generateHint(
        errorType: ErrorType?,
        problemType: ProblemType,
        currentWork: String,
        stepNumber: Int
    ): String {
        // First, check for error-specific hints
        if (errorType != null) {
            val errorHint = getErrorTypeHint(errorType, problemType, currentWork)
            if (errorHint != null) return errorHint
        }
        
        // Fall back to problem-type hints
        return getProblemTypeHint(problemType, stepNumber)
    }
    
    /**
     * Generate hints specific to error types
     */
    private fun getErrorTypeHint(errorType: ErrorType, problemType: ProblemType, currentWork: String): String? {
        return when (errorType) {
            ErrorType.SIGN_ERROR -> {
                listOf(
                    "Check your signs - did you apply the operation to both sides?",
                    "When moving terms across the equals sign, remember to change the sign",
                    "Double-check the signs in your last step",
                    "Be careful with negative signs when distributing"
                ).random()
            }
            
            ErrorType.CALCULATION_ERROR -> {
                listOf(
                    "Double-check your arithmetic",
                    "Try calculating that step again carefully",
                    "The numbers don't quite add up - review your calculation",
                    "Take another look at that calculation"
                ).random()
            }
            
            ErrorType.ALGEBRAIC_MISTAKE -> {
                when (problemType) {
                    ProblemType.LINEAR -> "Remember: whatever you do to one side, do to the other"
                    ProblemType.QUADRATIC -> "Did you factor correctly? Check each term"
                    ProblemType.RATIONAL -> "When working with fractions, find a common denominator"
                    ProblemType.RADICAL -> "Remember to square both sides to eliminate the square root"
                    else -> "This step doesn't follow algebraically from the previous one"
                }
            }
            
            ErrorType.WRONG_APPROACH -> {
                when (problemType) {
                    ProblemType.LINEAR -> "Try isolating the variable by moving constants to one side"
                    ProblemType.QUADRATIC -> "Have you considered factoring or using the quadratic formula?"
                    ProblemType.RATIONAL -> "Consider multiplying both sides by the denominator"
                    ProblemType.RADICAL -> "Try isolating the radical first before squaring"
                    else -> "Think about a different approach to solve this"
                }
            }
            
            ErrorType.INCOMPLETE_STEP -> {
                listOf(
                    "This step looks incomplete - what's the next operation?",
                    "Keep going - you're not quite finished with this step",
                    "What should you do with both sides of the equation?"
                ).random()
            }
        }
    }
    
    /**
     * Generate hints based on problem type and step number
     */
    private fun getProblemTypeHint(problemType: ProblemType, stepNumber: Int): String {
        return when (problemType) {
            ProblemType.LINEAR -> {
                when (stepNumber) {
                    0 -> "Start by moving all variable terms to one side"
                    1 -> "Now combine like terms"
                    2 -> "What operation will isolate the variable?"
                    else -> "You're close - simplify your answer"
                }
            }
            
            ProblemType.QUADRATIC -> {
                when (stepNumber) {
                    0 -> "First, get everything on one side so it equals zero"
                    1 -> "Can this expression be factored?"
                    2 -> "Set each factor equal to zero"
                    else -> "Solve for each possible value of x"
                }
            }
            
            ProblemType.RATIONAL -> {
                when (stepNumber) {
                    0 -> "Consider multiplying both sides by the common denominator"
                    1 -> "Now you have an equation without fractions - solve it"
                    else -> "Don't forget to check your answer in the original equation"
                }
            }
            
            ProblemType.RADICAL -> {
                when (stepNumber) {
                    0 -> "First, isolate the square root on one side"
                    1 -> "Now square both sides to eliminate the radical"
                    2 -> "Solve the resulting equation"
                    else -> "Remember to check for extraneous solutions"
                }
            }
            
            ProblemType.TRIGONOMETRIC -> {
                "Use trigonometric identities or inverse functions as needed"
            }
            
            ProblemType.LOGARITHMIC -> {
                when (stepNumber) {
                    0 -> "Consider using logarithm properties to simplify"
                    1 -> "Can you convert to exponential form?"
                    else -> "Solve for the variable"
                }
            }
            
            ProblemType.ARITHMETIC -> {
                "Perform the operations step by step"
            }
            
            ProblemType.SYSTEM -> {
                "Use substitution or elimination to solve the system"
            }
            
            ProblemType.INEQUALITY -> {
                "Remember: multiplying by a negative flips the inequality sign"
            }
        }
    }
    
    /**
     * Generate encouragement and next step suggestion when current step is correct
     */
    fun generateNextStepSuggestion(currentWork: String, stepNumber: Int): String? {
        // Check if solution is complete
        if (isLikelySolution(currentWork)) {
            return "Great work! You've found the solution."
        }
        
        // Provide subtle encouragement and direction
        return when (stepNumber) {
            0 -> "Good start! What's your next move?"
            1 -> "You're on the right track. Keep simplifying."
            2 -> "Nice work. What operation comes next?"
            3 -> "Almost there! Continue simplifying."
            else -> "Looking good! Keep going."
        }
    }
    
    /**
     * Suggest what the next step should be (without solving it)
     */
    fun suggestNextStep(currentWork: String): String? {
        val work = currentWork.toLowerCase()
        
        return when {
            // Has terms on both sides with variables
            work.contains("x") && work.substringAfter("=").contains(Regex("[0-9]")) -> {
                "Try combining like terms or moving variables to one side"
            }
            
            // Has addition/subtraction with variable
            work.contains("+") && work.contains("x") -> {
                "What operation will help isolate the variable?"
            }
            
            // Has multiplication/division with variable
            (work.contains("*") || work.contains("/")) && work.contains("x") -> {
                "Use the inverse operation to isolate the variable"
            }
            
            // Looks simplified enough
            work.matches(Regex(".*x\\s*=\\s*[0-9.-]+.*")) -> {
                "You're done! Check your answer by substituting back."
            }
            
            else -> null
        }
    }
    
    /**
     * Check if the current work looks like a final solution
     */
    private fun isLikelySolution(work: String): Boolean {
        val cleaned = work.replace(" ", "").toLowerCase()
        
        // Pattern: x = number (possibly with operations)
        return cleaned.matches(Regex("x=[-]?[0-9.]+")) ||
               cleaned.matches(Regex("x=[-]?[0-9.]+/[-]?[0-9.]+"))
    }
    
    /**
     * Generate a hint for when student seems stuck
     */
    fun generateStuckHint(problemType: ProblemType, timeStuck: Long): String {
        return when {
            timeStuck < 30000 -> "Take your time and think about what you've learned"
            timeStuck < 60000 -> {
                when (problemType) {
                    ProblemType.LINEAR -> "Remember: isolate the variable by performing inverse operations"
                    ProblemType.QUADRATIC -> "Try factoring or using the quadratic formula"
                    ProblemType.RATIONAL -> "Multiply by the common denominator to clear fractions"
                    else -> "Break the problem down into smaller steps"
                }
            }
            else -> {
                "Would you like a more detailed hint about the next step?"
            }
        }
    }
}
