package com.mathdraw.app

data class Stroke(
    val points: List<StrokePoint>
)

data class StrokePoint(
    val x: Float,
    val y: Float,
    val timestamp: Long
)
