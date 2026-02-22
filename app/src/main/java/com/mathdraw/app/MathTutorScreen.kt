package com.mathdraw.app

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathTutorScreen(
    tutorViewModel: MathTutorViewModel = viewModel(),
    drawViewModel: MathDrawViewModel = viewModel()
) {
    val currentProblem by tutorViewModel.currentProblem.collectAsState()
    val solutionState by tutorViewModel.solutionState.collectAsState()
    val aiFeedback by tutorViewModel.aiFeedback.collectAsState()
    val isProcessing by tutorViewModel.isProcessing.collectAsState()
    
    val recognizedText by drawViewModel.recognizedText.collectAsState()
    val latexEquation by drawViewModel.latexEquation.collectAsState()
    val strokes by drawViewModel.strokes.collectAsState()
    
    val context = LocalContext.current
    
    // Camera permission
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }
    
    // Image picker
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            tutorViewModel.processProblemImage(context, photoUri!!)
        }
    }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            tutorViewModel.processProblemImage(context, it)
        }
    }
    
    // Monitor user's work
    LaunchedEffect(latexEquation) {
        if (latexEquation.isNotEmpty() && currentProblem != null) {
            tutorViewModel.processUserWork(recognizedText, latexEquation)
        }
    }
    
    LaunchedEffect(Unit) {
        drawViewModel.initializeRecognizer(context)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Math Tutor AI") },
                actions = {
                    if (currentProblem != null) {
                        IconButton(onClick = { tutorViewModel.clearProblem() }) {
                            Icon(Icons.Default.Close, "Clear problem")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentProblem == null) {
                FloatingActionButton(
                    onClick = {
                        if (hasCameraPermission) {
                            photoUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                File(context.cacheDir, "problem_${System.currentTimeMillis()}.jpg")
                            )
                            cameraLauncher.launch(photoUri!!)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Icon(Icons.Default.CameraAlt, "Capture problem")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (currentProblem == null) {
                // Welcome screen
                WelcomeScreen(
                    onCameraClick = {
                        if (hasCameraPermission) {
                            photoUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                File(context.cacheDir, "problem_${System.currentTimeMillis()}.jpg")
                            )
                            cameraLauncher.launch(photoUri!!)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    onGalleryClick = {
                        galleryLauncher.launch("image/*")
                    }
                )
            } else {
                // Problem solving screen
                ProblemSolvingScreen(
                    problem = currentProblem!!,
                    solutionState = solutionState,
                    aiFeedback = aiFeedback,
                    strokes = strokes,
                    recognizedText = recognizedText,
                    latexEquation = latexEquation,
                    onStrokeStart = drawViewModel::startStroke,
                    onStrokeUpdate = drawViewModel::addPointToStroke,
                    onStrokeEnd = drawViewModel::endStroke,
                    onClear = drawViewModel::clearCanvas,
                    onUndo = drawViewModel::undo
                )
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Math Tutor AI",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Solve math problems with real-time AI guidance",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onCameraClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.CameraAlt, "Camera", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Take Photo of Problem")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = onGalleryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Choose from Gallery")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "How it works:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Take a photo of a math problem from your book")
                Text("2. Start solving by writing on the canvas")
                Text("3. Get real-time hints if you make a mistake")
                Text("4. Learn step-by-step with AI guidance")
            }
        }
    }
}

@Composable
fun ProblemSolvingScreen(
    problem: MathProblem,
    solutionState: SolutionState?,
    aiFeedback: AIFeedback?,
    strokes: List<Stroke>,
    recognizedText: String,
    latexEquation: String,
    onStrokeStart: (androidx.compose.ui.geometry.Offset) -> Unit,
    onStrokeUpdate: (androidx.compose.ui.geometry.Offset) -> Unit,
    onStrokeEnd: () -> Unit,
    onClear: () -> Unit,
    onUndo: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Problem Display (top 30%)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Problem:",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Show captured image if available
                problem.imageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Problem image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Show extracted text
                if (problem.extractedText.isNotEmpty()) {
                    Text(
                        text = problem.extractedText,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                // Show LaTeX equation
                if (problem.latexEquation.isNotEmpty()) {
                    Text(
                        text = problem.latexEquation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // AI Feedback Bar
        aiFeedback?.let { feedback ->
            AIFeedbackBar(feedback)
        }
        
        // Working Area (bottom 60%)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header showing current work
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Your Work:",
                                style = MaterialTheme.typography.labelMedium
                            )
                            if (solutionState != null) {
                                Text(
                                    text = "Step ${solutionState.currentStep + 1}",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                        
                        Text(
                            text = latexEquation.ifEmpty { "Start writing your solution..." },
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (latexEquation.isEmpty()) 
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Drawing Canvas
                DrawingCanvas(
                    strokes = strokes,
                    onStrokeStart = onStrokeStart,
                    onStrokeUpdate = onStrokeUpdate,
                    onStrokeEnd = onStrokeEnd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.White)
                )
                
                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onClear,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear")
                    }
                    Button(
                        onClick = onUndo,
                        modifier = Modifier.weight(1f),
                        enabled = strokes.isNotEmpty()
                    ) {
                        Text("Undo")
                    }
                }
            }
        }
    }
}

@Composable
fun AIFeedbackBar(feedback: AIFeedback) {
    val backgroundColor = when {
        !feedback.isOnTrack -> MaterialTheme.colorScheme.errorContainer
        feedback.subtleHint != null -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    
    val textColor = when {
        !feedback.isOnTrack -> MaterialTheme.colorScheme.onErrorContainer
        feedback.subtleHint != null -> MaterialTheme.colorScheme.onTertiaryContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        color = backgroundColor,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Help,
                contentDescription = "Hint",
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when {
                        feedback.isOnTrack && feedback.subtleHint == null -> "Looking good! Keep going..."
                        !feedback.isOnTrack -> "Hmm, check your work"
                        else -> "Hint:"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor
                )
                
                feedback.subtleHint?.let { hint ->
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
            }
            
            // Confidence indicator
            Text(
                text = "${(feedback.confidence * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
}
