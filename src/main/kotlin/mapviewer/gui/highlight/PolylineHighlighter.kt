package mapviewer.gui.highlight

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import mapviewer.gui.RenderingManager
import mapviewer.gui.RenderingUtil
import mapviewer.gui.domain.MapParams
import mapviewer.parser.domain.ShpRecord

class PolylineHighlighter {
    fun highlight(highlightCanvas: Canvas, selectedFeatureList: MutableList<ShpRecord>, mapParams: MapParams) {
        val gc = highlightCanvas.graphicsContext2D
        for (shpRecord in selectedFeatureList) {
            for (partIndex in 0 until shpRecord.recordContent.numParts) {
                val translatedPoints = RenderingUtil.translatePointsToWholeCanvas(
                        shpRecord.recordContent.points[partIndex], mapParams,
                        shpRecord.recordContent.parts[partIndex + 1] - shpRecord.recordContent.parts[partIndex],
                        RenderingManager.leftTopTileIndexInCanvas.x,
                        RenderingManager.leftTopTileIndexInCanvas.y
                )
                gc.stroke = Color.BLUE
                gc.strokePolyline(translatedPoints.pointXArray, translatedPoints.pointYArray, translatedPoints.pointXArray.size)
            }
        }
    }
}