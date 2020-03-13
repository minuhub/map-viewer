package mapviewer.gui.highlight

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import mapviewer.gui.RenderingUtil
import mapviewer.gui.domain.MapParams
import mapviewer.parser.domain.ShpRecord

class PolygonHighlighter {
    fun highlight(highlightCanvas: Canvas, selectedFeatureList: MutableList<ShpRecord>, mapParams: MapParams) {
        val gc = highlightCanvas.graphicsContext2D
        for (shpRecord in selectedFeatureList) {
            for (partIndex in 0 until shpRecord.recordContent.numParts) {
                val translatedPoints = RenderingUtil.translatePoints(
                    shpRecord.recordContent.points[partIndex], mapParams,
                    shpRecord.recordContent.parts[partIndex + 1] - shpRecord.recordContent.parts[partIndex]
                )
                gc.fill = Color(1.0, 0.0, 0.0, 0.5)
                gc.fillPolygon(translatedPoints.pointXArray, translatedPoints.pointYArray, translatedPoints.pointXArray.size)
                gc.stroke = Color.RED
                gc.strokePolygon(translatedPoints.pointXArray, translatedPoints.pointYArray, translatedPoints.pointXArray.size)
            }
        }
    }
}