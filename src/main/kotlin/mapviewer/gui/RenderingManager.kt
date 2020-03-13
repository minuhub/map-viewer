package mapviewer.gui

import javafx.scene.canvas.Canvas
import mapviewer.gui.RenderingManager.tileIndexX
import mapviewer.gui.RenderingManager.tileIndexXinCanvas
import mapviewer.gui.RenderingManager.tileIndexY
import mapviewer.gui.RenderingManager.tileIndexYinCanvas
import mapviewer.gui.domain.FunctionalBBox
import mapviewer.gui.domain.GraphicStyle
import mapviewer.gui.domain.MapParams
import mapviewer.gui.renderer.RendererFactory
import mapviewer.parser.domain.Shp

object RenderingManager {
    var tileIndexX = 0.0
    var tileIndexY = 0.0
    var tileIndexXinCanvas = 0.0
    var tileIndexYinCanvas = 0.0

    fun renderAllOnCanvas(canvas: Canvas, shp: Shp, mapParams: MapParams) {
        renderAllOnCanvas(canvas, shp, mapParams, true)
    }

    fun renderAllOnCanvas(canvas: Canvas, shp: Shp, mapParams: MapParams, debug: Boolean) {
        val renderer = RendererFactory.chooseRenderer(shp.shpHeader)

        val row = (mapParams.canvasBBox.width - 1) / mapParams.tileSizeInCanvas + 1
        val column = (mapParams.canvasBBox.height - 1) / mapParams.tileSizeInCanvas + 1

        for (rowIndex in 0 until row.toInt() + 1) {
            for (columnIndex in 0 until column.toInt() + 1) {
                calculateIndex(mapParams, mapParams.tileBBox.minX + rowIndex, mapParams.tileBBox.minY + columnIndex, rowIndex * mapParams.tileSizeInCanvas, columnIndex * mapParams.tileSizeInCanvas)
                if (debug) renderTileOnCanvas(canvas, mapParams)
                updateShpBBox(mapParams)

                renderer.renderShpOnCanvas(canvas, shp.shpRecordList, mapParams, tileIndexXinCanvas, tileIndexYinCanvas)
            }
        }
    }

    fun clearCanvas(canvas: Canvas, mapParams: MapParams) {
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, mapParams.canvasBBox.width, mapParams.canvasBBox.height)
    }
}

fun calculateIndex(mapParams: MapParams, tileBBoxX: Double, tileBBoxY: Double, canvasBBoxX: Double, canvasBBoxY: Double) {
    tileIndexX = kotlin.math.floor(tileBBoxX)
    tileIndexY = kotlin.math.floor(tileBBoxY)
    tileIndexXinCanvas = RenderingUtil.tileToCanvasX(tileIndexX, tileBBoxX, canvasBBoxX, mapParams)
    tileIndexYinCanvas = RenderingUtil.tileToCanvasY(tileIndexY, tileBBoxY, canvasBBoxY, mapParams)
}

fun updateShpBBox(mapParams: MapParams) {
    val shpBBoxEdgeLength = RenderingUtil.calculateShpBBoxEdgeLength(mapParams)
    val shpBBoxMinX = RenderingUtil.tileToShpX(tileIndexX, mapParams)
    val shpBBoxMaxY = RenderingUtil.tileToShpY(tileIndexY, mapParams)
    val shpBBox = FunctionalBBox(shpBBoxMinX,
            shpBBoxMaxY - shpBBoxEdgeLength,
            shpBBoxMinX + shpBBoxEdgeLength,
            shpBBoxMaxY
    )
    mapParams.shpBBox = shpBBox
    mapParams.shpBBox.updateRatioX(mapParams.tileSizeInCanvas)
    mapParams.shpBBox.updateRatioY(mapParams.tileSizeInCanvas)
}

fun renderTileOnCanvas(canvas: Canvas, mapParams: MapParams) {
    val gc = canvas.graphicsContext2D


    GraphicStyle.set(gc,GraphicStyle.Tile)
    gc.strokeRect(tileIndexXinCanvas, tileIndexYinCanvas, mapParams.tileSizeInCanvas, mapParams.tileSizeInCanvas)

    GraphicStyle.set(gc,GraphicStyle.Text)
    gc.strokeText("(${mapParams.zoomLevel},${tileIndexX.toInt()},${tileIndexY.toInt()})", tileIndexXinCanvas, tileIndexYinCanvas + 30.0)
}


