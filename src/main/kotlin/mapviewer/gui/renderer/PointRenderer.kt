package mapviewer.gui.renderer

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import mapviewer.gui.RenderingUtil
import mapviewer.gui.domain.DrawingPointParams.diameter
import mapviewer.gui.domain.DrawingPointParams.radius
import mapviewer.gui.domain.MapParams
import mapviewer.parser.domain.ShpRecord

class PointRenderer : Renderer {
    override fun renderShpOnCanvas(canvas: Canvas, shpRecordList: MutableList<ShpRecord>, mapParams: MapParams, tileIndexXinCanvas: Double, tileIndexYinCanvas: Double){
        val gc = canvas.graphicsContext2D
//        GraphicStyle.set(gc, GraphicStyle.Shp)

        for (shpRecord in shpRecordList) {
            if (!RenderingUtil.isaBBoxInbBBox(shpRecord.recordContent.recordBBox, mapParams.shpBBox)) continue

            val translatedPoints = RenderingUtil.translatePointsToTileInCanvas(
                shpRecord.recordContent.points[0], mapParams, 1, tileIndexXinCanvas, tileIndexYinCanvas
            )
            gc.fill = Color.YELLOWGREEN
            gc.fillOval(translatedPoints.pointXArray[0] - radius, translatedPoints.pointYArray[0] - radius, diameter, diameter)
        }
    }
}