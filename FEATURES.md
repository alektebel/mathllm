# Math Tutor AI - Feature Documentation

## Overview

Math Tutor AI transforms how students solve math problems by combining:
- **Computer Vision** (OCR) to read problems from books
- **Handwriting Recognition** to understand your work
- **Artificial Intelligence** to check your steps and provide hints

## Core Features

### 1. Problem Capture

**How to use:**
1. Tap the camera button (bottom right)
2. Point camera at a math problem in your textbook
3. Take the photo
4. AI automatically extracts the equation

**What it recognizes:**
- Printed equations from textbooks
- Handwritten problems
- Simple to complex expressions
- Multiple equation types:
  - Linear equations: `2x + 5 = 13`
  - Quadratic equations: `x² - 5x + 6 = 0`
  - Systems of equations
  - Calculus problems
  - Trigonometry
  - And more...

**Pro tips:**
- Good lighting improves recognition
- Keep text horizontal and level
- Avoid shadows or glare
- Crop to just the problem if possible

### 2. Split-Screen Interface

**Layout:**
```
┌─────────────────────────────────┐
│     PROBLEM (Top 30%)          │
│  - Captured image               │
│  - Extracted equation           │
│  - LaTeX representation         │
├─────────────────────────────────┤
│     AI HINT BAR                 │
│  - Real-time feedback           │
│  - Subtle hints                 │
│  - Confidence level             │
├─────────────────────────────────┤
│   YOUR WORK (Bottom 60%)        │
│  - Handwriting canvas           │
│  - Live LaTeX conversion        │
│  - Step tracking                │
│  - Undo/Clear buttons           │
└─────────────────────────────────┘
```

### 3. Handwriting Recognition

**Symbol Support:**
- All 150+ mathematical symbols
- Greek letters (α, β, π, θ, etc.)
- Operators (×, ÷, ±, √, ∫, ∑, etc.)
- Relations (=, ≠, ≤, ≥, ≈, etc.)
- Full list in [SYMBOLS.md](SYMBOLS.md)

**Pattern Recognition:**
- Fractions: Write `1/2` → Get `\frac{1}{2}`
- Exponents: Write `x^2` → Get `x^{2}`
- Subscripts: Write `a_n` → Get `a_{n}`
- Roots: Write `√9` → Get `\sqrt{9}`

**Writing tips:**
- Write clearly and deliberately
- Use standard mathematical notation
- Brief pauses between symbols help
- The AI improves as you write more

### 4. Real-Time AI Checking

**How it works:**

The AI monitors your work continuously and:

1. **Validates Each Step**
   - Checks algebraic correctness
   - Verifies calculations
   - Ensures proper mathematical operations

2. **Detects Errors**
   - Algebraic mistakes
   - Sign errors (+ vs -)
   - Calculation errors
   - Wrong approach
   - Incomplete steps

3. **Provides Hints**
   - **Green bar**: "Looking good! Keep going..."
   - **Yellow bar**: "Hint: [subtle guidance]"
   - **Red bar**: "Hmm, check your work"

**Hint Examples:**

*Instead of:*
- ❌ "The answer is x = 4"
- ❌ "You need to divide by 2"

*We give:*
- ✅ "What operation should you apply to both sides?"
- ✅ "Check your signs carefully"
- ✅ "Remember to simplify before proceeding"

### 5. Step-by-Step Progress

**Step Tracking:**
- Each complete step is recorded
- Progress indicator shows current step number
- Review past steps (coming in future update)
- Score calculation based on accuracy

**What counts as a step:**
- A complete mathematical transformation
- Usually ends with an equals sign (=)
- Shows clear progression toward the solution
- Example steps for `2x + 5 = 13`:
  - Step 1: `2x + 5 - 5 = 13 - 5`
  - Step 2: `2x = 8`
  - Step 3: `x = 4`

## AI Feedback System

### Feedback Types

1. **Positive Reinforcement**
   - Shown when step is correct
   - Encourages continued progress
   - Example: "Looking good! Keep going..."

2. **Subtle Hints**
   - Shown when user is stuck or slowing down
   - Doesn't give away answer
   - Example: "What operation eliminates the constant term?"

3. **Error Alerts**
   - Shown when mistake detected
   - Identifies type of error
   - Example: "Check your work - calculation error detected"

### Confidence Levels

Each hint shows confidence percentage:
- **90-100%**: AI is very confident
- **70-89%**: AI is reasonably sure
- **50-69%**: AI is uncertain
- **<50%**: AI needs more context

### Error Types Detected

| Error Type | Description | Example |
|------------|-------------|---------|
| Algebraic | Wrong operation | `2x = 8` → `x = 8 - 2` ❌ |
| Sign Error | + vs - mistake | `5 - 3 = 8` ❌ |
| Calculation | Arithmetic error | `2 × 4 = 6` ❌ |
| Wrong Approach | Valid but inefficient | Using quadratic formula on linear equation |
| Incomplete | Step not finished | `2x + 5 =` (no right side) |

## Usage Workflow

### Typical Session

```
1. START
   └─> Open app
   └─> Tap camera button

2. CAPTURE PROBLEM
   └─> Take photo of problem
   └─> Wait for OCR (2-3 seconds)
   └─> Problem appears in top panel

3. READ & UNDERSTAND
   └─> Review extracted equation
   └─> Plan your approach
   └─> AI may give initial hint

4. SOLVE STEP-BY-STEP
   └─> Write first step
   └─> AI validates in real-time
   └─> If correct: green feedback
   └─> If wrong: hint appears
   └─> Continue to next step

5. COMPLETE
   └─> Final answer reached
   └─> Review your work
   └─> Start new problem or review
```

### Best Practices

**DO:**
- ✅ Write one clear step at a time
- ✅ Show all your work
- ✅ Read AI hints carefully
- ✅ Try to figure it out before looking at hints
- ✅ Practice regularly

**DON'T:**
- ❌ Rush through steps
- ❌ Skip intermediate work
- ❌ Ignore hints and continue if wrong
- ❌ Write multiple steps at once
- ❌ Use for cheating on tests

## Advanced Features

### Future AI Capabilities

**Planned for v3.0:**

1. **Gemini Nano Integration**
   - On-device AI reasoning
   - Natural language explanations
   - "Why" questions answered
   - Multiple solution approaches

2. **Adaptive Learning**
   - Tracks your common mistakes
   - Personalizes hints
   - Adjusts difficulty
   - Suggests practice problems

3. **Solution Explanations**
   - "Explain this step" button
   - Voice explanations (TTS)
   - Step-by-step breakdown
   - Visual aids

4. **Multi-Step Planning**
   - AI suggests solution roadmap
   - Shows expected number of steps
   - Previews approach without solving
   - Alternative methods

### Customization Options

**Coming Soon:**
- Hint aggressiveness (more/less help)
- Feedback timing (instant vs delayed)
- Difficulty preferences
- Subject focus (algebra, calculus, etc.)

## Technical Details

### OCR Accuracy

**Factors affecting recognition:**
- Image quality (lighting, focus, resolution)
- Print clarity (clean vs faded)
- Font type (standard vs fancy)
- Equation complexity

**Expected accuracy:**
- Simple equations: 95%+
- Complex expressions: 85-90%
- Handwritten text: 70-80%

**Improving OCR:**
- Retake photo if recognition is poor
- Use good lighting
- Keep camera steady
- Crop to just the problem

### Handwriting Processing

**Recognition pipeline:**
1. Digital ink captured (touch coordinates + timestamps)
2. ML Kit processes strokes
3. Symbols identified
4. LaTeX generated
5. AI validates against expected

**Latency:**
- Symbol recognition: ~500ms after stroke
- AI validation: ~1-2 seconds
- Total lag: Barely noticeable

### AI Model

**Current (v2.0):**
- Pattern-based validation
- Rule-based hint generation
- Local processing only
- No internet needed after setup

**Future (v3.0+):**
- Gemini Nano on-device LLM
- Symbolic math solver
- Cloud-enhanced (optional)
- Continuous learning

## Privacy & Data

### What We Collect
- **Nothing!** All processing is on-device
- Photos stay in app cache
- No telemetry or analytics
- No internet after initial setup

### Permissions Required
- **Camera**: For capturing problems
- **Internet**: Only for initial model download
- **Storage**: Temporary photo cache

### Data Storage
- Photos: Cached temporarily, auto-deleted
- Work: Only in app memory (not saved)
- No cloud sync
- No external sharing

## Troubleshooting

### OCR Not Working
- Check camera permission granted
- Ensure good lighting
- Hold camera steady
- Try retaking photo

### Hints Not Appearing
- Ensure you completed a step (ends with =)
- Check internet on first launch
- AI may be loading - wait 2-3 seconds
- Try manual "Recognize" if available

### Wrong Feedback
- AI is learning-based, not perfect
- Report issues (planned feedback feature)
- Continue based on your judgment
- Use as guidance, not gospel

## Learning Tips

### Maximize Learning

1. **Try Before Hinting**
   - Attempt each step yourself first
   - Only check hint if stuck

2. **Understand Mistakes**
   - When AI shows error, understand why
   - Don't just correct, learn the concept

3. **Practice Similar Problems**
   - If you struggle, find more practice
   - Repetition builds mastery

4. **Use for Homework, Not Tests**
   - Great for learning and practice
   - Don't use to cheat on exams
   - Academic integrity matters

### Study Strategies

**Problem Sets:**
1. Photo all problems first
2. Solve without hints
3. Check with AI after
4. Review mistakes

**Concept Mastery:**
1. Start with easy problems
2. Work up to harder ones
3. Track which topics need work
4. Focus practice accordingly

**Exam Prep:**
1. Practice with AI hints off
2. Time yourself
3. Review missed problems with AI
4. Understand every mistake

## Support & Feedback

### Getting Help
- Check [QUICKSTART.md](QUICKSTART.md) for setup
- See [SYMBOLS.md](SYMBOLS.md) for symbol reference
- Review this guide for features

### Contributing
- Report bugs via GitHub Issues
- Suggest features
- Contribute code (open source)
- Share your experience

## Roadmap

**v2.0 (Current)**
- ✅ Problem capture (OCR)
- ✅ Handwriting recognition
- ✅ Real-time checking
- ✅ Basic hints

**v2.5 (Next)**
- ⏳ Solution history
- ⏳ Problem library
- ⏳ Offline mode improvements
- ⏳ UI enhancements

**v3.0 (Future)**
- 🔮 Gemini Nano integration
- 🔮 Natural language explanations
- 🔮 Adaptive learning
- 🔮 Multi-approach solutions

**v4.0 (Vision)**
- 🔮 Voice input
- 🔮 Collaborative solving
- 🔮 Teacher mode
- 🔮 Problem generator
