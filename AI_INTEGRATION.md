# Integrating a Small Language Model (SLM) for Math

This guide explains how to integrate a real AI model for advanced math solving and hint generation.

## Options for On-Device AI

### 1. Google AI Edge (Gemini Nano) - RECOMMENDED

**Pros:**
- Runs entirely on-device
- Optimized for mobile (quantized)
- Good at reasoning and explanations
- Official Google support
- Free

**Cons:**
- Requires Android 14+ for best performance
- Limited to compatible devices
- Model size: ~1-3GB

**Integration:**

```kotlin
// Add dependency
implementation("com.google.ai.edge:generativeai:0.1.2")

// Initialize
class AITutorEngine(context: Context) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-nano",
        apiKey = "" // Not needed for on-device
    )
    
    suspend fun validateStep(
        problem: String,
        userStep: String,
        previousSteps: List<String>
    ): AIFeedback {
        val prompt = """
        Math Problem: $problem
        
        Previous steps:
        ${previousSteps.joinToString("\n")}
        
        User's current step: $userStep
        
        Is this step algebraically correct? 
        If not, provide a subtle hint without giving the answer.
        
        Respond in JSON:
        {
          "is_correct": true/false,
          "hint": "subtle hint here or null",
          "error_type": "algebraic/sign/calculation/approach",
          "confidence": 0.0-1.0
        }
        """.trimIndent()
        
        val response = generativeModel.generateContent(prompt)
        return parseAIResponse(response.text)
    }
}
```

### 2. MediaPipe LLM Inference

**Pros:**
- Multiple model options (Gemma, Phi, etc.)
- Flexible and customizable
- Good performance
- Free and open source

**Cons:**
- Requires manual model management
- Less integrated than Gemini Nano
- More setup required

**Integration:**

```kotlin
// Add dependency
implementation("com.google.mediapipe:tasks-genai:0.10.8")

class MediaPipeSolver(context: Context) {
    private val llmInference = LlmInference.createFromOptions(
        context,
        LlmInferenceOptions.builder()
            .setModelPath("/path/to/gemma-2b.bin")
            .setMaxTokens(512)
            .setTopK(40)
            .setTemperature(0.3f) // Lower = more deterministic
            .build()
    )
    
    suspend fun generateHint(
        problem: String,
        userWork: String
    ): String {
        val prompt = """
        As a math tutor, provide a subtle hint for:
        Problem: $problem
        Student's work so far: $userWork
        
        Give a guiding question, not the answer.
        """
        
        return llmInference.generateResponse(prompt)
    }
}
```

### 3. TensorFlow Lite with Custom Model

**Pros:**
- Complete control
- Can train custom math models
- Optimized for mobile

**Cons:**
- Requires ML expertise
- Need to train/fine-tune model
- More complex integration

**Models to consider:**
- **Gemma 2B** (quantized): General reasoning
- **Phi-2** (quantized): Good at math
- **MathBERT**: Specialized for equations
- **Custom fine-tuned**: Best for your use case

### 4. Cloud-Based (Optional Fallback)

For complex problems, fall back to cloud:

```kotlin
class HybridSolver {
    private val onDeviceAI: AIEngine
    private val cloudAPI: CloudMathAPI
    
    suspend fun solve(problem: MathProblem): Solution {
        return try {
            // Try on-device first
            onDeviceAI.solve(problem)
        } catch (e: Exception) {
            // Fallback to cloud if too complex
            if (isConnected() && problem.difficulty == Difficulty.HARD) {
                cloudAPI.solve(problem)
            } else {
                throw e
            }
        }
    }
}
```

## Recommended Implementation

### Phase 1: Pattern-Based (Current)

Start simple with rule-based validation:

```kotlin
class SimpleSolver {
    fun validateAlgebraicStep(
        leftBefore: String,
        rightBefore: String,
        leftAfter: String,
        rightAfter: String
    ): Boolean {
        // Check if same operation applied to both sides
        val leftOp = detectOperation(leftBefore, leftAfter)
        val rightOp = detectOperation(rightBefore, rightAfter)
        
        return leftOp == rightOp && leftOp != null
    }
    
    fun generateHint(step: Int, error: ErrorType): String {
        return when (error) {
            ErrorType.ALGEBRAIC_MISTAKE -> 
                "Remember: what you do to one side, do to the other"
            ErrorType.SIGN_ERROR -> 
                "Check your positive and negative signs"
            ErrorType.CALCULATION_ERROR -> 
                "Double-check your arithmetic"
            else -> 
                "Review this step carefully"
        }
    }
}
```

### Phase 2: Symbolic Math Engine

Use SymJa or similar for exact validation:

```kotlin
// Add dependency for symbolic math
implementation("org.matheclipse:matheclipse-core:2.0.0")

class SymbolicValidator {
    fun areEquivalent(expr1: String, expr2: String): Boolean {
        val engine = ExprEvaluator()
        val result = engine.eval("Simplify[$expr1 - ($expr2)]")
        return result.toString() == "0"
    }
    
    fun solveEquation(equation: String): List<String> {
        val engine = ExprEvaluator()
        val result = engine.eval("Solve[$equation, x]")
        return parseResults(result)
    }
}
```

### Phase 3: Full AI Integration

Combine symbolic engine + LLM:

```kotlin
class AdvancedTutor(
    private val symbolicEngine: SymbolicValidator,
    private val llm: LLMInference
) {
    suspend fun analyzeStep(
        problem: String,
        userStep: String,
        context: SolutionContext
    ): AIFeedback {
        // 1. Fast check with symbolic engine
        val isAlgebraicallyValid = symbolicEngine.areEquivalent(
            context.previousExpression,
            userStep
        )
        
        // 2. If invalid, use LLM for hint generation
        val hint = if (!isAlgebraicallyValid) {
            llm.generateHint(
                problem = problem,
                invalidStep = userStep,
                validSteps = context.validSteps
            )
        } else {
            null
        }
        
        // 3. Use LLM for strategic guidance
        val strategicHint = if (context.isStuck) {
            llm.suggestApproach(problem, context)
        } else {
            null
        }
        
        return AIFeedback(
            isOnTrack = isAlgebraicallyValid,
            currentStepValid = isAlgebraicallyValid,
            subtleHint = hint ?: strategicHint,
            confidence = if (isAlgebraicallyValid) 0.99f else 0.85f
        )
    }
}
```

## Prompt Engineering for Math

### Good Prompts

```kotlin
// ✅ Structured, clear role, format specified
val goodPrompt = """
You are a patient math tutor helping a student.

Problem: Solve 2x + 5 = 13
Student's work: 2x + 5 - 5 = 13

Task: Check if this step is correct. If wrong, give a subtle hint.

Respond ONLY in this JSON format:
{
  "correct": true/false,
  "hint": "your hint or null"
}
""".trimIndent()
```

### Bad Prompts

```kotlin
// ❌ Vague, no structure, unclear output
val badPrompt = """
Is 2x + 5 - 5 = 13 correct for solving 2x + 5 = 13?
Give a hint.
"""
```

### Hint Generation Prompts

```kotlin
fun createHintPrompt(
    problem: String,
    userWork: String,
    errorType: ErrorType
): String = """
You are a Socratic math tutor. Guide, don't tell.

Problem: $problem
Student wrote: $userWork
Error detected: $errorType

Provide a guiding QUESTION (not the answer) that helps them realize their mistake.

Examples of good hints:
- "What happens when you subtract 5 from both sides?"
- "Is 2 × 4 equal to 6?"
- "Remember PEMDAS - what should you do first?"

Your hint (one sentence, as a question):
""".trimIndent()
```

## Performance Optimization

### Model Selection

```kotlin
class AdaptiveAI {
    fun selectModel(problem: Difficulty): AIModel {
        return when (problem) {
            Difficulty.EASY -> SimplePatternMatcher()
            Difficulty.MEDIUM -> SymbolicEngine()
            Difficulty.HARD -> FullLLM()
        }
    }
}
```

### Caching & Batching

```kotlin
class OptimizedAI {
    private val cache = LruCache<String, AIFeedback>(50)
    
    suspend fun analyze(step: String): AIFeedback {
        // Check cache first
        cache.get(step)?.let { return it }
        
        // Batch multiple steps if possible
        val result = if (pendingSteps.size > 3) {
            analyzeBatch(pendingSteps + step)
        } else {
            analyzeSingle(step)
        }
        
        cache.put(step, result)
        return result
    }
}
```

### Quantization

Use quantized models for speed:

```
Original model: 7GB, 5s inference
INT8 quantized: 1.8GB, 1s inference  ← Use this
INT4 quantized: 900MB, 0.5s inference ← Or this
```

## Testing AI Quality

### Validation Dataset

Create test cases:

```kotlin
data class TestCase(
    val problem: String,
    val correctSteps: List<String>,
    val incorrectSteps: List<String>,
    val expectedHints: Map<String, String>
)

class AITester {
    fun testAccuracy() {
        val testCases = loadTestCases()
        var correct = 0
        var total = 0
        
        testCases.forEach { case ->
            case.correctSteps.forEach { step ->
                val result = ai.validate(case.problem, step)
                if (result.isCorrect) correct++
                total++
            }
            
            case.incorrectSteps.forEach { step ->
                val result = ai.validate(case.problem, step)
                if (!result.isCorrect) correct++
                total++
            }
        }
        
        println("Accuracy: ${correct * 100 / total}%")
    }
}
```

### Metrics to Track

- **Validation accuracy**: % of steps correctly judged
- **Hint quality**: User rating of helpfulness
- **False positive rate**: Correct steps marked wrong
- **False negative rate**: Wrong steps marked correct
- **Latency**: Time to generate feedback
- **Battery impact**: Power consumption

## Privacy & Safety

### On-Device Processing

```kotlin
class PrivacyFirstAI {
    // ✅ All processing on device
    private val localModel: LocalLLM
    
    // ❌ Never send user data to cloud
    suspend fun analyze(userWork: String): Feedback {
        // Process locally only
        return localModel.process(userWork)
    }
}
```

### Content Filtering

```kotlin
class SafeAI(private val model: LLM) {
    suspend fun generateHint(prompt: String): String {
        val hint = model.generate(prompt)
        
        // Filter inappropriate content
        if (containsInappropriate(hint)) {
            return "Please try the next step"
        }
        
        // Don't give away answers
        if (containsAnswer(hint)) {
            return makeMoreSubtle(hint)
        }
        
        return hint
    }
}
```

## Example: Complete Integration

```kotlin
class MathTutorAI(context: Context) {
    
    // Three-tier approach
    private val patternMatcher = PatternBasedValidator()
    private val symbolicEngine = SymbolicMathEngine()
    private val llm = GeminiNano(context)
    
    suspend fun analyzeSolution(
        problem: MathProblem,
        userStep: String,
        previousSteps: List<String>
    ): AIFeedback {
        
        // Tier 1: Fast pattern matching (10ms)
        val quickCheck = patternMatcher.validate(userStep)
        if (quickCheck.confidence > 0.9f) {
            return quickCheck
        }
        
        // Tier 2: Symbolic verification (100ms)
        val symbolicCheck = symbolicEngine.validate(
            previous = previousSteps.lastOrNull() ?: problem.equation,
            current = userStep
        )
        
        if (!symbolicCheck.isValid) {
            // Tier 3: LLM hint generation (1-2s)
            val hint = llm.generateHint(
                problem = problem.extractedText,
                userWork = previousSteps + userStep,
                errorType = symbolicCheck.errorType
            )
            
            return AIFeedback(
                isOnTrack = false,
                currentStepValid = false,
                subtleHint = hint,
                confidence = 0.85f
            )
        }
        
        return AIFeedback(
            isOnTrack = true,
            currentStepValid = true,
            subtleHint = null,
            confidence = 0.95f
        )
    }
}
```

## Resources

### Models
- **Gemini Nano**: https://ai.google.dev/edge
- **Gemma**: https://ai.google.dev/gemma
- **Phi-2**: https://huggingface.co/microsoft/phi-2
- **MediaPipe**: https://developers.google.com/mediapipe

### Libraries
- **TensorFlow Lite**: https://www.tensorflow.org/lite
- **ML Kit**: https://developers.google.com/ml-kit
- **SymJa**: https://github.com/axkr/symja_android_library

### Datasets for Training
- **MATH dataset**: https://github.com/hendrycks/math
- **GSM8K**: Grade school math problems
- **Khan Academy**: Education data

### Papers
- "Training Verifiers to Solve Math Word Problems" (OpenAI)
- "MathBERT: A Pre-Trained Language Model for General NLP Tasks in Mathematics"
- "Let's Verify Step by Step"

## Next Steps

1. **Start simple**: Implement pattern-based validation first
2. **Add symbolic engine**: For exact mathematical verification
3. **Integrate Gemini Nano**: For natural language hints
4. **Test thoroughly**: With real users and test cases
5. **Iterate**: Based on feedback and metrics

Good luck building your AI math tutor! 🚀
