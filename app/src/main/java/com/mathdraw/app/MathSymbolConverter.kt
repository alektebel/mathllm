package com.mathdraw.app

/**
 * Comprehensive LaTeX converter for mathematical symbols and expressions
 */
object MathSymbolConverter {
    
    /**
     * All mathematical symbols mapped to their LaTeX equivalents
     */
    private val symbolMap = mapOf(
        // Basic operators
        "×" to "\\times",
        "÷" to "\\div",
        "·" to "\\cdot",
        "∗" to "\\ast",
        "⊕" to "\\oplus",
        "⊗" to "\\otimes",
        "⊙" to "\\odot",
        
        // Relations
        "≠" to "\\neq",
        "≤" to "\\leq",
        "≥" to "\\geq",
        "≪" to "\\ll",
        "≫" to "\\gg",
        "≈" to "\\approx",
        "≡" to "\\equiv",
        "∼" to "\\sim",
        "≃" to "\\simeq",
        "∝" to "\\propto",
        "∈" to "\\in",
        "∉" to "\\notin",
        "⊂" to "\\subset",
        "⊃" to "\\supset",
        "⊆" to "\\subseteq",
        "⊇" to "\\supseteq",
        "∪" to "\\cup",
        "∩" to "\\cap",
        "⊥" to "\\perp",
        "∥" to "\\parallel",
        "∠" to "\\angle",
        "∴" to "\\therefore",
        "∵" to "\\because",
        
        // Greek letters (lowercase)
        "α" to "\\alpha",
        "β" to "\\beta",
        "γ" to "\\gamma",
        "δ" to "\\delta",
        "ε" to "\\epsilon",
        "ζ" to "\\zeta",
        "η" to "\\eta",
        "θ" to "\\theta",
        "ι" to "\\iota",
        "κ" to "\\kappa",
        "λ" to "\\lambda",
        "μ" to "\\mu",
        "ν" to "\\nu",
        "ξ" to "\\xi",
        "π" to "\\pi",
        "ρ" to "\\rho",
        "σ" to "\\sigma",
        "τ" to "\\tau",
        "υ" to "\\upsilon",
        "φ" to "\\phi",
        "χ" to "\\chi",
        "ψ" to "\\psi",
        "ω" to "\\omega",
        
        // Greek letters (uppercase)
        "Γ" to "\\Gamma",
        "Δ" to "\\Delta",
        "Θ" to "\\Theta",
        "Λ" to "\\Lambda",
        "Ξ" to "\\Xi",
        "Π" to "\\Pi",
        "Σ" to "\\Sigma",
        "Φ" to "\\Phi",
        "Ψ" to "\\Psi",
        "Ω" to "\\Omega",
        
        // Special symbols
        "±" to "\\pm",
        "∓" to "\\mp",
        "∞" to "\\infty",
        "∅" to "\\emptyset",
        "∇" to "\\nabla",
        "∂" to "\\partial",
        "ℏ" to "\\hbar",
        "ℓ" to "\\ell",
        "℘" to "\\wp",
        "ℜ" to "\\Re",
        "ℑ" to "\\Im",
        "ℵ" to "\\aleph",
        
        // Arrows
        "→" to "\\to",
        "←" to "\\leftarrow",
        "↔" to "\\leftrightarrow",
        "⇒" to "\\Rightarrow",
        "⇐" to "\\Leftarrow",
        "⇔" to "\\Leftrightarrow",
        "↦" to "\\mapsto",
        "↗" to "\\nearrow",
        "↘" to "\\searrow",
        "↙" to "\\swarrow",
        "↖" to "\\nwarrow",
        
        // Calculus & Analysis
        "√" to "\\sqrt",
        "∛" to "\\sqrt[3]",
        "∜" to "\\sqrt[4]",
        "∑" to "\\sum",
        "∏" to "\\prod",
        "∫" to "\\int",
        "∬" to "\\iint",
        "∭" to "\\iiint",
        "∮" to "\\oint",
        "∂" to "\\partial",
        "∇" to "\\nabla",
        "lim" to "\\lim",
        
        // Logic
        "∀" to "\\forall",
        "∃" to "\\exists",
        "∄" to "\\nexists",
        "¬" to "\\neg",
        "∧" to "\\land",
        "∨" to "\\lor",
        "⊤" to "\\top",
        "⊥" to "\\bot",
        "⊢" to "\\vdash",
        "⊨" to "\\models",
        
        // Sets
        "ℕ" to "\\mathbb{N}",
        "ℤ" to "\\mathbb{Z}",
        "ℚ" to "\\mathbb{Q}",
        "ℝ" to "\\mathbb{R}",
        "ℂ" to "\\mathbb{C}",
        
        // Trigonometric
        "sin" to "\\sin",
        "cos" to "\\cos",
        "tan" to "\\tan",
        "cot" to "\\cot",
        "sec" to "\\sec",
        "csc" to "\\csc",
        "arcsin" to "\\arcsin",
        "arccos" to "\\arccos",
        "arctan" to "\\arctan",
        "sinh" to "\\sinh",
        "cosh" to "\\cosh",
        "tanh" to "\\tanh",
        
        // Logarithms
        "log" to "\\log",
        "ln" to "\\ln",
        "lg" to "\\lg",
        
        // Misc
        "°" to "^\\circ",
        "′" to "'",
        "″" to "''",
        "‴" to "'''",
        "†" to "\\dagger",
        "‡" to "\\ddagger",
        "..." to "\\ldots",
        "⋯" to "\\cdots",
        "⋮" to "\\vdots",
        "⋱" to "\\ddots",
        "⊞" to "\\boxplus",
        "⊟" to "\\boxminus",
        "⊠" to "\\boxtimes",
        "∘" to "\\circ",
        "∙" to "\\bullet",
        "★" to "\\star",
        "◦" to "\\circ",
    )
    
    /**
     * Convert recognized text to LaTeX with comprehensive symbol support
     */
    fun toLatex(text: String): String {
        var latex = text
        
        // Apply symbol replacements (longer patterns first to avoid conflicts)
        symbolMap.entries
            .sortedByDescending { it.key.length }
            .forEach { (symbol, latexSymbol) ->
                latex = latex.replace(symbol, latexSymbol)
            }
        
        // Pattern-based conversions
        latex = applyPatternConversions(latex)
        
        // Wrap in LaTeX delimiters
        return "$$latex$"
    }
    
    private fun applyPatternConversions(text: String): String {
        var result = text
        
        // Fractions: "1/2" -> "\frac{1}{2}"
        result = Regex("""(\d+)/(\d+)""").replace(result) { 
            "\\frac{${it.groupValues[1]}}{${it.groupValues[2]}}"
        }
        
        // Variable fractions: "x/y" -> "\frac{x}{y}"
        result = Regex("""([a-zA-Z]\w*)/([a-zA-Z]\w*)""").replace(result) { 
            "\\frac{${it.groupValues[1]}}{${it.groupValues[2]}}"
        }
        
        // Exponents: "x^2" -> "x^{2}"
        result = Regex("""(\w)\^(\w+)""").replace(result) { 
            "${it.groupValues[1]}^{${it.groupValues[2]}}"
        }
        
        // Subscripts: "x_1" -> "x_{1}"
        result = Regex("""(\w)_(\w+)""").replace(result) { 
            "${it.groupValues[1]}_{${it.groupValues[2]}}"
        }
        
        // Square roots with arguments: "√2" -> "\sqrt{2}"
        result = Regex("""\\sqrt\s*(\w+)""").replace(result) { 
            "\\sqrt{${it.groupValues[1]}}"
        }
        
        // Absolute value: "|x|" -> "\left|x\right|"
        result = Regex("""\|([^|]+)\|""").replace(result) { 
            "\\left|${it.groupValues[1]}\\right|"
        }
        
        // Auto-sizing delimiters
        result = result.replace("(", "\\left(").replace(")", "\\right)")
        result = result.replace("[", "\\left[").replace("]", "\\right]")
        
        // Handle braces carefully (they might be LaTeX syntax)
        if (!result.contains("\\frac") && !result.contains("^{") && !result.contains("_{")) {
            result = result.replace("{", "\\left\\{").replace("}", "\\right\\}")
        }
        
        return result
    }
    
    /**
     * Get list of all supported symbols for reference
     */
    fun getSupportedSymbols(): Map<String, String> = symbolMap
}
