# Installation Guide

## Quick Install (Recommended)

### Step 1: Download the APK
Download the pre-built APK directly from GitHub:
- **[MathDraw v1.0 Debug APK (49MB)](https://github.com/alektebel/mathllm/raw/main/releases/MathDraw-v1.0-debug.apk)**

### Step 2: Enable Installation from Unknown Sources
On your Android device:
1. Go to **Settings** → **Security** (or **Privacy**)
2. Enable **"Install unknown apps"** or **"Unknown sources"**
3. Select your browser/file manager and allow installations

### Step 3: Install the APK
1. Open the downloaded APK file
2. Tap **"Install"**
3. Wait for installation to complete
4. Tap **"Open"** to launch MathDraw

### Step 4: First Launch Setup
On the first launch:
1. The app will request internet access
2. It will automatically download the ML Kit math recognition model (~15MB)
3. This takes 30-60 seconds depending on your connection
4. Once complete, the app works offline!

## System Requirements
- **Android Version**: 7.0 (Nougat) or higher (API level 24+)
- **Storage**: ~65MB total
  - APK: 49MB
  - ML Kit Model: ~15MB
  - App data: ~1MB
- **Internet**: Required only for first launch (model download)
- **Permissions**: Internet access (for model download only)

## Using the App

### Writing Math
1. Launch MathDraw
2. Use your finger to write math symbols on the white canvas
3. Recognition happens automatically after you finish each stroke
4. See the LaTeX equation appear at the top in real-time

### Supported Input
- **All Greek letters**: α, β, γ, δ, π, θ, σ, Σ, Δ, Ω, etc.
- **Operators**: +, -, ×, ÷, =, ≠, ≤, ≥, ±, etc.
- **Calculus**: ∫, ∑, ∏, ∂, ∇, lim, √, etc.
- **Logic**: ∀, ∃, ∧, ∨, ¬, ⇒, ⇔, etc.
- **Set theory**: ∈, ⊂, ∪, ∩, ℕ, ℤ, ℚ, ℝ, ℂ, etc.
- **150+ total symbols**

### Controls
- **Clear**: Erase everything and start over
- **Undo**: Remove the last stroke
- **Recognize**: Force recognition if automatic recognition didn't trigger

### Tips for Best Results
1. **Write clearly** - Use consistent stroke order
2. **Standard notation** - Write symbols as you normally would
3. **Spacing** - Brief pauses between symbols help recognition
4. **Complex expressions** - Use the "Recognize" button after writing
5. **Fractions** - Write as "numerator/denominator" (e.g., "1/2")
6. **Exponents** - Write as "base^power" (e.g., "x^2")

## Examples

### Simple Equations
Write: `x^2 + 2x + 1 = 0`
Get: `$x^{2} + 2x + 1 = 0$`

### Calculus
Write: `∫ x^2 dx`
Get: `$\int x^{2} dx$`

### Greek Letters
Write: `α + β = π`
Get: `$\alpha + \beta = \pi$`

### Complex Expressions
Write: `∑_{i=1}^n i^2`
Get: `$\sum_{i=1}^{n} i^{2}$`

## Troubleshooting

### App won't install
- Make sure you enabled "Unknown sources" in Settings
- Check you have enough storage space (65MB free)
- Verify your Android version is 7.0 or higher

### Model download fails
- Check your internet connection
- Try closing and reopening the app
- Make sure the app has internet permission

### Recognition not working
- Ensure the model downloaded successfully (first launch)
- Write more clearly with standard notation
- Use the "Recognize" button to force recognition
- Check that strokes are being drawn (you should see black lines)

### Poor recognition quality
- Write larger symbols
- Use clear, deliberate strokes
- Avoid rushed or scribbled writing
- Try writing one symbol at a time

### App crashes
- Clear app data: Settings → Apps → MathDraw → Clear Data
- Uninstall and reinstall the app
- Redownload the APK if file is corrupted

## Privacy & Permissions

MathDraw only requests **Internet** permission for:
- Downloading the ML Kit recognition model on first launch
- After model download, the app works completely offline
- **No data is collected or sent anywhere**
- All recognition happens locally on your device

## Building from Source

If you prefer to build the app yourself:
1. Clone the repository: `git clone https://github.com/alektebel/mathllm.git`
2. Open in Android Studio
3. Follow instructions in [QUICKSTART.md](QUICKSTART.md)

## Updates

Check the [GitHub repository](https://github.com/alektebel/mathllm) for updates and new versions.

## License

This is an open-source educational project. Feel free to modify and distribute.

## Support

For issues or questions:
- Open an issue on [GitHub](https://github.com/alektebel/mathllm/issues)
- Check [SYMBOLS.md](SYMBOLS.md) for complete symbol reference
- Review [QUICKSTART.md](QUICKSTART.md) for development setup
