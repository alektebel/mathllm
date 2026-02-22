package com.mathdraw.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MathDrawScreen(viewModel: MathDrawViewModel = viewModel()) {
    val strokes by viewModel.strokes.collectAsState()
    val recognizedText by viewModel.recognizedText.collectAsState()
    val latexEquation by viewModel.latexEquation.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.initializeRecognizer(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "MathDraw",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // LaTeX Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "LaTeX:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    if (strokes.isNotEmpty()) {
                        Text(
                            text = "✓ Recognizing...",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    text = latexEquation.ifEmpty { "Write any math symbol below..." },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (recognizedText.isNotEmpty()) {
                    Text(
                        text = "Raw: $recognizedText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Drawing Canvas
        DrawingCanvas(
            strokes = strokes,
            onStrokeStart = { point -> viewModel.startStroke(point) },
            onStrokeUpdate = { point -> viewModel.addPointToStroke(point) },
            onStrokeEnd = { viewModel.endStroke() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
        )

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.clearCanvas() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear")
            }
            Button(
                onClick = { viewModel.undo() },
                modifier = Modifier.weight(1f),
                enabled = strokes.isNotEmpty()
            ) {
                Text("Undo")
            }
            Button(
                onClick = { viewModel.recognizeNow() },
                modifier = Modifier.weight(1f),
                enabled = strokes.isNotEmpty()
            ) {
                Text("Recognize")
            }
        }
    }
}

@Composable
fun DrawingCanvas(
    strokes: List<Stroke>,
    onStrokeStart: (Offset) -> Unit,
    onStrokeUpdate: (Offset) -> Unit,
    onStrokeEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPath by remember { mutableStateOf<Path?>(null) }
    
    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        currentPath = Path().apply {
                            moveTo(offset.x, offset.y)
                        }
                        onStrokeStart(offset)
                    },
                    onDrag = { change, _ ->
                        currentPath?.lineTo(change.position.x, change.position.y)
                        onStrokeUpdate(change.position)
                    },
                    onDragEnd = {
                        currentPath = null
                        onStrokeEnd()
                    }
                )
            }
    ) {
        // Draw completed strokes
        strokes.forEach { stroke ->
            if (stroke.points.isNotEmpty()) {
                val path = Path()
                val firstPoint = stroke.points.first()
                path.moveTo(firstPoint.x, firstPoint.y)
                
                stroke.points.drop(1).forEach { point ->
                    path.lineTo(point.x, point.y)
                }
                
                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
        
        // Draw current stroke
        currentPath?.let { path ->
            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(
                    width = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}
