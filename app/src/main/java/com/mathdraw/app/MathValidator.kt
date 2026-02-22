package com.mathdraw.app

import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.abs

/**
 * Validates mathematical expressions and equations using expression evaluation
 * Lightweight alternative to heavy symbolic math libraries
 */
class MathValidator {
    
    /**
     * Check if two expressions are algebraically equivalent by testing values
     */
    fun areEquivalent(expr1: String, expr2: String): Boolean {
        return try {
            val cleaned1 = cleanExpression(expr1)
            val cleaned2 = cleanExpression(expr2)
            
            // If both are constants, just compare
            if (!cleaned1.contains("x") && !cleaned2.contains("x")) {
                val val1 = evaluateExpression(cleaned1)
                val val2 = evaluateExpression(cleaned2)
                return abs(val1 - val2) < 0.0001
            }
            
            // Test with multiple x values to check equivalence
            val testValues = listOf(0.0, 1.0, -1.0, 2.0, -2.0, 0.5, 10.0)
            
            testValues.all { xVal ->
                try {
                    val result1 = evaluateWithVariable(cleaned1, "x", xVal)
                    val result2 = evaluateWithVariable(cleaned2, "x", xVal)
                    abs(result1 - result2) < 0.0001
                } catch (e: Exception) {
                    false
                }
            }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Check if an equation is valid (both sides equal)
     */
    fun isValidEquation(equation: String): Boolean {
        return try {
            val sides = splitEquation(equation)
            if (sides == null) return false
            
            areEquivalent(sides.first, sides.second)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Validate a solution step by checking if it follows from the previous step
     */
    fun validateStep(previousStep: String, currentStep: String, operation: String? = null): StepValidation {
        try {
            // Extract equations from steps
            val prevEq = extractEquation(previousStep)
            val currEq = extractEquation(currentStep)
            
            if (prevEq == null || currEq == null) {
                return StepValidation(false, ErrorType.INCOMPLETE_STEP, "Could not parse equation")
            }
            
            val prevSides = splitEquation(prevEq)
            val currSides = splitEquation(currEq)
            
            if (prevSides == null || currSides == null) {
                return StepValidation(false, ErrorType.INCOMPLETE_STEP, "Invalid equation format")
            }
            
            // Check if both equations are valid (balanced)
            if (!isValidEquation(prevEq)) {
                return StepValidation(false, ErrorType.ALGEBRAIC_MISTAKE, "Previous equation is invalid")
            }
            
            if (!isValidEquation(currEq)) {
                return StepValidation(false, ErrorType.ALGEBRAIC_MISTAKE, "Current equation is invalid")
            }
            
            // Check for specific error types
            val errorType = detectErrorType(prevSides, currSides)
            
            if (errorType != null) {
                return StepValidation(false, errorType, getErrorMessage(errorType))
            }
            
            // Check if equations maintain equivalence
            if (!doEquationsMaintainSolution(prevSides, currSides)) {
                return StepValidation(false, ErrorType.ALGEBRAIC_MISTAKE, "This step doesn't maintain equivalence")
            }
            
            return StepValidation(true, null, "Step is correct")
            
        } catch (e: Exception) {
            return StepValidation(false, ErrorType.ALGEBRAIC_MISTAKE, "Error validating step: ${e.message}")
        }
    }
    
    /**
     * Check if two equations have the same solution by testing values
     */
    private fun doEquationsMaintainSolution(
        prevSides: EquationSides,
        currSides: EquationSides
    ): Boolean {
        val testValues = listOf(0.0, 1.0, -1.0, 2.0, 5.0, -5.0)
        
        return testValues.all { xVal ->
            try {
                val prevLeft = evaluateWithVariable(prevSides.first, "x", xVal)
                val prevRight = evaluateWithVariable(prevSides.second, "x", xVal)
                val prevDiff = abs(prevLeft - prevRight)
                
                val currLeft = evaluateWithVariable(currSides.first, "x", xVal)
                val currRight = evaluateWithVariable(currSides.second, "x", xVal)
                val currDiff = abs(currLeft - currRight)
                
                // If previous equation was satisfied, current should be too
                if (prevDiff < 0.0001) {
                    currDiff < 0.0001
                } else {
                    true // If test value wasn't a solution before, skip
                }
            } catch (e: Exception) {
                false
            }
        }
    }
    
    /**
     * Detect specific error types in step transitions
     */
    private fun detectErrorType(
        prevSides: EquationSides,
        currSides: EquationSides
    ): ErrorType? {
        // Check for sign errors (operation applied to only one side)
        if (hasSignError(prevSides, currSides)) {
            return ErrorType.SIGN_ERROR
        }
        
        // Check for calculation errors
        if (hasCalculationError(prevSides, currSides)) {
            return ErrorType.CALCULATION_ERROR
        }
        
        return null
    }
    
    /**
     * Check if there's a sign error (operation not applied to both sides)
     */
    private fun hasSignError(prevSides: EquationSides, currSides: EquationSides): Boolean {
        try {
            // Check if terms were moved incorrectly
            // Common pattern: left side changes but right doesn't (or vice versa)
            
            val leftUnchanged = areExpressionsIdentical(prevSides.first, currSides.first)
            val rightUnchanged = areExpressionsIdentical(prevSides.second, currSides.second)
            
            // If only one side changed, possible sign error
            if (leftUnchanged && !rightUnchanged) {
                return !areEquivalent(prevSides.second, currSides.second)
            }
            if (!leftUnchanged && rightUnchanged) {
                return !areEquivalent(prevSides.first, currSides.first)
            }
            
        } catch (e: Exception) {
            // Can't determine
        }
        return false
    }
    
    /**
     * Check for calculation errors (arithmetic mistakes)
     */
    private fun hasCalculationError(prevSides: EquationSides, currSides: EquationSides): Boolean {
        try {
            // Check if expressions with only numbers are calculated incorrectly
            if (!prevSides.first.contains("x") && !currSides.first.contains("x")) {
                val prevVal = evaluateExpression(prevSides.first)
                val currVal = evaluateExpression(currSides.first)
                if (abs(prevVal - currVal) > 0.0001) {
                    return true
                }
            }
            
            if (!prevSides.second.contains("x") && !currSides.second.contains("x")) {
                val prevVal = evaluateExpression(prevSides.second)
                val currVal = evaluateExpression(currSides.second)
                if (abs(prevVal - currVal) > 0.0001) {
                    return true
                }
            }
            
        } catch (e: Exception) {
            // Can't verify
        }
        return false
    }
    
    /**
     * Check if two expressions are identical (same string after normalization)
     */
    private fun areExpressionsIdentical(expr1: String, expr2: String): Boolean {
        val normalized1 = expr1.replace(" ", "").lowercase()
        val normalized2 = expr2.replace(" ", "").lowercase()
        return normalized1 == normalized2
    }
    
    /**
     * Evaluate an expression with a variable value
     */
    private fun evaluateWithVariable(expression: String, variable: String, value: Double): Double {
        val expr = cleanExpression(expression).replace(variable, value.toString())
        return evaluateExpression(expr)
    }
    
    /**
     * Evaluate a mathematical expression
     */
    private fun evaluateExpression(expression: String): Double {
        val cleaned = cleanExpression(expression)
        return ExpressionBuilder(cleaned)
            .variables("x")
            .build()
            .setVariable("x", 0.0)
            .evaluate()
    }
    
    /**
     * Extract equation from a step string (may contain text)
     */
    private fun extractEquation(step: String): String? {
        // Look for pattern: expression = expression
        val equationPattern = Regex("""([^=]+)\s*=\s*([^=]+)""")
        val match = equationPattern.find(step)
        return match?.value?.trim()
    }
    
    /**
     * Split equation into left and right sides
     */
    private fun splitEquation(equation: String): EquationSides? {
        val parts = equation.split("=")
        if (parts.size != 2) return null
        return EquationSides(parts[0].trim(), parts[1].trim())
    }
    
    /**
     * Clean expression for evaluation (convert LaTeX-like syntax)
     */
    private fun cleanExpression(expr: String): String {
        var cleaned = expr
            .replace("$", "")
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")  // Math minus
            .replace("–", "-")  // En dash
            .replace("—", "-")  // Em dash
            .replace(" ", "")
            .trim()
        
        // Handle implicit multiplication: 2x -> 2*x, (x)(y) -> (x)*(y)
        cleaned = Regex("(\\d)([a-zA-Z])").replace(cleaned) { "${it.groupValues[1]}*${it.groupValues[2]}" }
        cleaned = Regex("\\)\\(").replace(cleaned, ")*(")
        cleaned = Regex("(\\d)\\(").replace(cleaned) { "${it.groupValues[1]}*(" }
        cleaned = Regex("\\)(\\d)").replace(cleaned) { ")*${it.groupValues[1]}" }
        
        return cleaned
    }
    
    /**
     * Get error message for error type
     */
    private fun getErrorMessage(errorType: ErrorType): String {
        return when (errorType) {
            ErrorType.SIGN_ERROR -> "Check your signs - did you apply the operation to both sides?"
            ErrorType.CALCULATION_ERROR -> "Double-check your arithmetic"
            ErrorType.ALGEBRAIC_MISTAKE -> "This algebraic step doesn't follow from the previous one"
            ErrorType.WRONG_APPROACH -> "This approach may not lead to the solution"
            ErrorType.INCOMPLETE_STEP -> "This step is incomplete or unclear"
        }
    }
}

/**
 * Represents the two sides of an equation
 */
private data class EquationSides(val first: String, val second: String)

/**
 * Result of step validation
 */
data class StepValidation(
    val isValid: Boolean,
    val errorType: ErrorType?,
    val message: String
)
