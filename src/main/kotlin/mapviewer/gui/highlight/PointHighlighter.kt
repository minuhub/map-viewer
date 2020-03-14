package mapviewer.gui.highlight

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import mapviewer.gui.RenderingManager
import mapviewer.gui.RenderingUtil
import mapviewer.gui.domain.MapParams
import mapviewer.gui.domain.DrawingPointParams.diameter
import mapviewer.gui.domain.DrawingPointParams.radius
import mapviewer.parser.domain.ShpRecord

class PointHighlighter {
    fun highlight(highlightCanvas: Canvas, selectedFeatureList: MutableList<ShpRecord>, mapParams: MapParams) {
        val gc = highlightCanvas.graphicsContext2D
        for (shpRecord in selectedFeatureList) {
            val translatedPoints = RenderingUtil.translatePointsToWholeCanvas(
                    shpRecord.recordContent.points[0], mapParams, 1,
                    RenderingManager.leftTopTileIndexInCanvas.x,
                    RenderingManager.leftTopTileIndexInCanvas.y
            )
            gc.fill = Color.GREEN
            gc.fillOval(translatedPoints.pointXArray[0] - radius, translatedPoints.pointYArray[0] - radius, diameter, diameter)
        }
    }
}