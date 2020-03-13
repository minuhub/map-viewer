package mapviewer.gui.domain

import mapviewer.parser.domain.BBox
import kotlin.math.max

class FunctionalBBox(minX: Double, minY: Double, maxX: Double, maxY: Double) : BBox(minX, minY, maxX, maxY) {
    var width: Double = maxX - minX
    var height: Double = maxY - minY
    var longerEdge: Double = max(width, height)
    var centerX: Double = (maxX + minX) / 2
    var centerY: Double = (maxY + minY) / 2

    var bBoxToCanvasRatioX: Double = 1.0
    var bBoxToCanvasRatioY: Double = 1.0

    fun translateBBoxX(moveX: Double) {
        this.minX += moveX
        this.maxX += moveX
        updateCenterX()
    }

    fun translateBBoxY(moveY: Double) {
        this.minY += moveY
        this.maxY += moveY
        updateCenterY()
    }

    fun updateWidth() {
        this.width = maxX - minX
    }

    fun updateHeight() {
        this.height = maxY - minY
    }

    fun updateCenterX() {
        this.centerX = (maxX + minX) / 2
    }

    fun updateCenterY() {
        this.centerY = (maxY + minY) / 2
    }

    fun updateBBoxByCenter(distance: Double) {
        this.minX = centerX - distance
        this.minY = centerY - distance
        this.maxX = centerX + distance
        this.maxY = centerY + distance
    }

    fun updateRatioX(tileSizeInCanvas: Double) {
        updateWidth()
        this.bBoxToCanvasRatioX = tileSizeInCanvas / width
    }

    fun updateRatioY(tileSizeInCanvas: Double) {
        updateHeight()
        this.bBoxToCanvasRatioY = tileSizeInCanvas / height
    }
}

val emptyBBox = FunctionalBBox(0.0, 0.0, 0.0, 0.0)
