# Math Tutor AI - Smart Math Problem Solver

An AI-powered Android app that helps you solve math problems from textbooks with real-time guidance, handwriting recognition, and step-by-step feedback.

## Download APK

**[Download Math Tutor AI v2.1 (96MB) - WITH FULL AI FEATURES](releases/MathDraw-v2.1-debug.apk)** ⭐ LATEST

*Previous versions:*
- *v2.0 (96MB) - Basic framework: [Download](releases/MathDraw-v2.0-debug.apk)*
- *v1.0 (49MB) - Handwriting only: [Download](releases/MathDraw-v1.0-debug.apk)*

### Installation
1. Download the APK file
2. Enable "Install from Unknown Sources" in Android settings
3. Install and grant camera permissions
4. On first launch, ML models will download (~20MB total)

### Requirements
- Android 7.0 (API 24) or higher
- Camera for capturing problems
- ~80MB free storage
- Internet for first launch only

## Features

### 📸 Capture Math Problems
- Take photos of problems from textbooks, worksheets, or exams
- OCR automatically extracts the equation
- Supports printed and handwritten text

### ✍️ Write Your Solution
- Natural handwriting input with 150+ math symbol recognition
- Real-time LaTeX conversion as you write
- Smooth, responsive canvas

### 🤖 AI Tutor Guidance
- **Real-time checking**: AI analyzes each step as you write
- **Subtle hints**: Get guidance without giving away the answer
- **Error detection**: Identifies algebraic mistakes, sign errors, calculation errors
- **Step-by-step feedback**: Learn the proper approach

### 🎯 Smart Features
- Split-screen view: Problem above, workspace below
- Confidence scoring on AI feedback
- Step tracking and progress monitoring
- Undo/Clear functions

## How It Works

1. **Capture**: Take a photo of any math problem
2. **Extract**: OCR reads and converts to LaTeX
3. **Solve**: Start writing your solution by hand
4. **Check**: AI monitors your work in real-time
5. **Learn**: Get hints when you make mistakes

## Example Workflow

```
1. Take photo: "Solve: 2x + 5 = 13"
   ↓
2. AI extracts: $2x + 5 = 13$
   ↓
3. You write: "2x + 5 - 5 = 13 - 5"
   ↓
4. AI: ✓ "Looking good! Keep going..."
   ↓
5. You write: "2x = 8"
   ↓
6. AI: ✓ "Correct! Now what?"
   ↓
7. You write: "x = 8/2"
   ↓
8. AI: ⚠️ "Check your work - is 8/2 = 8?"
   ↓
9. You fix: "x = 4"
   ↓
10. AI: ✓ "Perfect! Solution complete."
```

## Features

### 🎯 Core Features
- **Real-time handwriting recognition** using Google ML Kit's specialized math model
- **Comprehensive symbol detection** - recognizes 150+ mathematical symbols
- **Automatic LaTeX conversion** with proper formatting
- **Smart pattern recognition** for fractions, exponents, subscripts, roots, and more
- **Camera capture** - Take photos of problems from textbooks
- **OCR text extraction** - Automatically reads printed equations

### 🤖 AI Validation (v2.1)
- **Real algebraic validation** - Checks if each step is mathematically correct
- **Error detection** - Identifies sign errors, calculation mistakes, algebraic errors
- **Contextual hints** - Problem-specific guidance without giving away answers
- **Problem type recognition** - Linear, quadratic, radical, rational, and more
- **Step-by-step tracking** - Complete solution history with timestamps
- **Performance scoring** - Grades based on correctness and hint usage

### 🧮 Mathematical Capabilities
- Expression equivalence checking (tests multiple values)
- Equation balancing validation
- Automatic problem type detection (9 types)
- 50+ contextual hint templates
- Solution completion detection
- Statistics and progress tracking

## Supported Symbols

### Basic Operators
×, ÷, ·, ±, ∓, ∗, ⊕, ⊗, ⊙

### Relations & Comparisons
=, ≠, <, >, ≤, ≥, ≪, ≫, ≈, ≡, ∼, ≃, ∝

### Greek Letters (All)
Lowercase: α, β, γ, δ, ε, ζ, η, θ, ι, κ, λ, μ, ν, ξ, π, ρ, σ, τ, υ, φ, χ, ψ, ω
Uppercase: Γ, Δ, Θ, Λ, Ξ, Π, Σ, Φ, Ψ, Ω

### Set Theory
∈, ∉, ⊂, ⊃, ⊆, ⊇, ∪, ∩, ∅, ℕ, ℤ, ℚ, ℝ, ℂ

### Logic
∀, ∃, ∄, ¬, ∧, ∨, ⊤, ⊥, ⊢, ⊨

### Calculus & Analysis
∫, ∬, ∭, ∮, ∂, ∇, ∑, ∏, √, ∛, ∜, lim

### Arrows
→, ←, ↔, ⇒, ⇐, ⇔, ↦, ↗, ↘, ↙, ↖

### Trigonometry & Functions
sin, cos, tan, cot, sec, csc, arcsin, arccos, arctan, sinh, cosh, tanh, log, ln

### Geometry
∠, ⊥, ∥, °, ∴, ∵

### Special Symbols
∞, ∂, ∇, ℏ, ℓ, ℘, ℜ, ℑ, ℵ, †, ‡

### Pattern Recognition
- Fractions: `1/2` → `\frac{1}{2}`
- Exponents: `x^2` → `x^{2}`
- Subscripts: `x_1` → `x_{1}`
- Roots: `√2` → `\sqrt{2}`
- Absolute value: `|x|` → `\left|x\right|`
- Auto-sizing delimiters: `()`, `[]`, `{}`

## Tech Stack
- **Kotlin** with Jetpack Compose
- **Google ML Kit** Digital Ink Recognition (handwriting to text)
- **Google ML Kit** Text Recognition (OCR for printed math)
- **CameraX** for image capture
- **AI/SLM Integration** for solution verification (extensible)
- Custom LaTeX converter with 150+ symbols

## AI Tutor Capabilities

### Current (v2.0)
- Pattern-based solution validation
- Step-by-step guidance
- Contextual hint generation
- Common error detection

### Future Enhancements
- **Gemini Nano** integration for on-device reasoning
- Advanced symbolic math solving
- Multi-step problem planning
- Personalized learning paths
- Solution explanation generation

## Development Setup
1. Clone this repository
2. Open project in Android Studio
3. Sync Gradle
4. Run on Android device or emulator (Android 7.0+ / API 24+)
5. App will download ML Kit math model on first launch (requires internet)

For detailed build instructions, see [QUICKSTART.md](QUICKSTART.md)

## Documentation
- [Symbol Reference](SYMBOLS.md) - Complete list of all 150+ supported symbols
- [Quick Start Guide](QUICKSTART.md) - Build and development instructions

## Usage
1. Write math equations or symbols on the canvas
2. Recognition happens automatically after each stroke
3. See both raw recognition and LaTeX conversion in real-time
4. Use "Clear" to start over, "Undo" to remove last stroke
5. Use "Recognize" to manually trigger recognition

## How It Works
1. **Drawing**: Canvas captures touch input as digital ink strokes
2. **Recognition**: ML Kit's math-specific model recognizes handwritten symbols
3. **Conversion**: Custom converter maps symbols to LaTeX equivalents
4. **Pattern Matching**: Smart regex patterns detect complex expressions
5. **Display**: LaTeX equation is formatted and displayed

## Tips for Best Recognition
- Write clearly with consistent stroke order
- Use standard mathematical notation
- Wait briefly between symbols for better segmentation
- For complex expressions, the "Recognize" button forces re-recognition
