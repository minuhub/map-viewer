package mapviewer.gui.renderer

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import mapviewer.gui.RenderingUtil
import mapviewer.gui.domain.GraphicStyle
import mapviewer.gui.domain.MapParams
import mapviewer.parser.domain.ShpRecord

class PolygonRenderer : Renderer {
    override fun renderShpOnCanvas(canvas: Canvas, shpRecordList: MutableList<ShpRecord>, mapParams: MapParams, tileIndexXinCanvas: Double, tileIndexYinCanvas: Double){
        val gc = canvas.graphicsContext2D
//        GraphicStyle.set(gc, GraphicStyle.Shp)

        for (shpRecord in shpRecordList) {
            if (!RenderingUtil.isOverlap(shpRecord.recordContent.recordBBox, mapParams.shpBBox)) continue

            for (partIndex in 0 until shpRecord.recordContent.numParts) {
                val translatedPoints = RenderingUtil.translatePoints(
                        shpRecord.recordContent.points[partIndex], mapParams,
                        shpRecord.recordContent.parts[partIndex + 1] - shpRecord.recordContent.parts[partIndex],
                        tileIndexXinCanvas, tileIndexYinCanvas
                )
                gc.stroke = Color.BLACK
                gc.lineWidth = 1.0
                gc.strokePolygon(translatedPoints.pointXArray, translatedPoints.pointYArray, translatedPoints.pointXArray.size)
            }
        }
    }
}