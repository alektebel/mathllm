# Quick Start Guide

## Prerequisites
- Android Studio (latest version recommended)
- Android SDK with API 24 (Android 7.0) or higher
- Android device or emulator
- Internet connection (for first launch to download ML Kit model)

## Building the App

### Step 1: Open in Android Studio
```bash
1. Open Android Studio
2. Click "Open an Existing Project"
3. Navigate to the MathDraw directory
4. Click "OK"
```

### Step 2: Gradle Sync
```bash
- Android Studio will automatically start syncing Gradle
- Wait for the sync to complete
- If prompted, accept any SDK installations
```

### Step 3: Connect Device or Start Emulator
```bash
Option A - Physical Device:
1. Enable Developer Options on your Android device
2. Enable USB Debugging
3. Connect device via USB
4. Accept USB debugging prompt on device

Option B - Emulator:
1. Click "Device Manager" in Android Studio
2. Click "Create Device"
3. Select any device (Pixel 5 recommended)
4. Choose system image (API 24+ required, API 33+ recommended)
5. Click "Finish" and start the emulator
```

### Step 4: Run the App
```bash
1. Click the green "Run" button (▶) in Android Studio
2. Select your device/emulator from the dropdown
3. Wait for the build to complete
4. App will install and launch automatically
```

## First Launch

On first launch, the app will:
1. Request internet permission (if needed)
2. Download the ML Kit math recognition model (~15MB)
3. This only happens once

After model download completes, you can use the app offline.

## Project Structure

```
MathDraw/
├── app/
│   ├── src/main/
│   │   ├── java/com/mathdraw/app/
│   │   │   ├── MainActivity.kt              # Entry point
│   │   │   ├── MathDrawScreen.kt            # Main UI
│   │   │   ├── MathDrawViewModel.kt         # Business logic
│   │   │   ├── MathSymbolConverter.kt       # LaTeX converter
│   │   │   ├── Stroke.kt                    # Data models
│   │   │   └── ui/theme/                    # Theme files
│   │   ├── res/                             # Resources
│   │   └── AndroidManifest.xml              # App manifest
│   └── build.gradle.kts                     # App dependencies
├── build.gradle.kts                         # Project config
├── settings.gradle.kts                      # Gradle settings
├── README.md                                # Main documentation
├── SYMBOLS.md                               # Symbol reference
└── QUICKSTART.md                            # This file
```

## Key Files Explained

### MainActivity.kt
- Entry point of the app
- Sets up Compose UI
- Minimal code, delegates to MathDrawScreen

### MathDrawScreen.kt
- Main UI using Jetpack Compose
- Drawing canvas component
- LaTeX display
- Action buttons (Clear, Undo, Recognize)

### MathDrawViewModel.kt
- Manages app state
- Handles ML Kit recognition
- Coordinates between UI and recognition engine

### MathSymbolConverter.kt
- Converts recognized text to LaTeX
- 150+ symbol mappings
- Pattern recognition for complex expressions

## Using the App

1. **Draw**: Write any math symbol on the white canvas
2. **Wait**: Recognition happens automatically after 500ms
3. **View**: See both raw text and LaTeX conversion at the top
4. **Clear**: Remove all strokes and start over
5. **Undo**: Remove the last stroke
6. **Recognize**: Manually trigger recognition

## Tips for Best Results

### Recognition Tips
- Write clearly and deliberately
- Use standard mathematical notation
- Allow brief pauses between symbols
- Keep strokes smooth and continuous

### Symbol Tips
- Greek letters: Write clearly (ML Kit knows them all)
- Operators: Standard symbols work best (×, ÷, ±, etc.)
- Fractions: Write as "numerator/denominator"
- Exponents: Use "x^2" notation
- Roots: Draw the √ symbol clearly

## Troubleshooting

### Model Download Fails
- Check internet connection
- Restart the app
- Clear app data and try again

### Recognition Not Working
- Ensure model downloaded successfully
- Check for errors in Logcat
- Try writing more clearly
- Use the "Recognize" button to force recognition

### Build Errors
- Clean project: Build → Clean Project
- Rebuild: Build → Rebuild Project
- Invalidate caches: File → Invalidate Caches / Restart
- Check Gradle version compatibility

### Emulator Issues
- Ensure emulator has API 24+
- Allocate sufficient RAM (2GB+)
- Enable hardware acceleration
- Try a different system image

## Development

### Adding New Symbols
Edit `MathSymbolConverter.kt` and add to the `symbolMap`:
```kotlin
"your_symbol" to "\\your_latex_command"
```

### Changing Recognition Model
Edit `MathDrawViewModel.kt` line 37:
```kotlin
val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("zxx-x-math")
```

### Adjusting Recognition Delay
Edit `MathDrawViewModel.kt` line 78:
```kotlin
kotlinx.coroutines.delay(500)  // Change delay in milliseconds
```

## Next Steps

- Explore SYMBOLS.md for complete symbol reference
- Check README.md for architecture details
- Customize the theme in ui/theme/ directory
- Add your own pattern recognition rules

## Getting Help

If you encounter issues:
1. Check Logcat for error messages
2. Review the error stack trace
3. Verify all dependencies are properly synced
4. Check ML Kit documentation for model-specific issues

Happy math writing!
