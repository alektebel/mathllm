# MathDraw - Handwriting Math Recognition App

An Android app that converts handwritten math equations to LaTeX in real-time with comprehensive symbol support.

## Download APK

**[Download MathDraw v1.0 (49MB)](releases/MathDraw-v1.0-debug.apk)**

### Installation
1. Download the APK file from the link above
2. Enable "Install from Unknown Sources" in your Android settings
3. Open the APK file and install
4. On first launch, the app will download the ML Kit math model (~15MB, requires internet)
5. After model download, the app works offline

### Requirements
- Android 7.0 (API 24) or higher
- ~65MB free storage (app + model)
- Internet connection for first launch only

## Features
- **Real-time handwriting recognition** using Google ML Kit's specialized math model
- **Comprehensive symbol detection** - recognizes ANY mathematical symbol
- **Automatic LaTeX conversion** with proper formatting
- **Smart pattern recognition** for fractions, exponents, subscripts, roots, and more
- Clear and undo functionality

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
- Kotlin
- Google ML Kit Digital Ink Recognition (Math Model)
- Jetpack Compose for UI
- Custom LaTeX converter with 150+ symbols

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
