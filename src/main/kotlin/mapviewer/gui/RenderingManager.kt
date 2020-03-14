package mapviewer.gui

import javafx.scene.canvas.Canvas
import mapviewer.gui.RenderingManager.tileIndex
import mapviewer.gui.RenderingManager.tileIndexInCanvas
import mapviewer.gui.domain.FunctionalBBox
import mapviewer.gui.domain.GraphicStyle
import mapviewer.gui.domain.MapParams
import mapviewer.gui.renderer.RendererFactory
import mapviewer.parser.domain.Point
import mapviewer.parser.domain.Shp

object RenderingManager {
    var tileIndex = Point(0.0, 0.0)
    var tileIndexInCanvas = Point(0.0, 0.0)
    var leftTopTileIndexInShp = Point(0.0, 0.0)
    var leftTopTileIndexInCanvas = Point(0.0, 0.0)

    fun renderAllOnCanvas(canvas: Canvas, shp: Shp, mapParams: MapParams) {
        renderAllOnCanvas(canvas, shp, mapParams, true)
    }

    fun renderAllOnCanvas(canvas: Canvas, shp: Shp, mapParams: MapParams, debug: Boolean) {
        val renderer = RendererFactory.chooseRenderer(shp.shpHeader)

        val row = (mapParams.canvasBBox.width - 1) / mapParams.tileSizeInCanvas + 1
        val column = (mapParams.canvasBBox.height - 1) / mapParams.tileSizeInCanvas + 1

        tileIndex.x = kotlin.math.floor(mapParams.tileBBox.minX)
        tileIndex.y = kotlin.math.floor(mapParams.tileBBox.minY)
        leftTopTileIndexInCanvas.x = RenderingUtil.tileToCanvasX(tileIndex.x, mapParams.tileBBox.minX, 0.0, mapParams)
        leftTopTileIndexInCanvas.y = RenderingUtil.tileToCanvasY(tileIndex.y, mapParams.tileBBox.minY, 0.0, mapParams)

        updateShpBBox(mapParams)
        leftTopTileIndexInShp.x = mapParams.shpBBox.minX
        leftTopTileIndexInShp.y = mapParams.shpBBox.maxY

        for (rowIndex in 0 until row.toInt() + 1) {
            for (columnIndex in 0 until column.toInt() + 1) {
                calculateIndex(mapParams, mapParams.tileBBox.minX + rowIndex, mapParams.tileBBox.minY + columnIndex, rowIndex * mapParams.tileSizeInCanvas, columnIndex * mapParams.tileSizeInCanvas)
                if (debug) renderTileOnCanvas(canvas, mapParams)
                updateShpBBox(mapParams)

                renderer.renderShpOnCanvas(canvas, shp.shpRecordList, mapParams, tileIndexInCanvas.x, tileIndexInCanvas.y)
            }
        }
    }

    fun clearCanvas(canvas: Canvas, mapParams: MapParams) {
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, mapParams.canvasBBox.width, mapParams.canvasBBox.height)
    }
}

fun calculateIndex(mapParams: MapParams, tileBBoxX: Double, tileBBoxY: Double, canvasBBoxX: Double, canvasBBoxY: Double) {
    tileIndex.x = kotlin.math.floor(tileBBoxX)
    tileIndex.y = kotlin.math.floor(tileBBoxY)
    tileIndexInCanvas.x = RenderingUtil.tileToCanvasX(tileIndex.x, tileBBoxX, canvasBBoxX, mapParams)
    tileIndexInCanvas.y = RenderingUtil.tileToCanvasY(tileIndex.y, tileBBoxY, canvasBBoxY, mapParams)
}

fun updateShpBBox(mapParams: MapParams) {
    val shpBBoxEdgeLength = RenderingUtil.calculateShpBBoxEdgeLength(mapParams)
    val shpBBoxMinX = RenderingUtil.tileToShpX(tileIndex.x, mapParams)
    val shpBBoxMaxY = RenderingUtil.tileToShpY(tileIndex.y, mapParams)
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


    GraphicStyle.set(gc, GraphicStyle.Tile)
    gc.strokeRect(tileIndexInCanvas.x, tileIndexInCanvas.y, mapParams.tileSizeInCanvas, mapParams.tileSizeInCanvas)

    GraphicStyle.set(gc, GraphicStyle.Text)
    gc.strokeText("(${mapParams.zoomLevel},${tileIndex.x.toInt()},${tileIndex.y.toInt()})", tileIndexInCanvas.x, tileIndexInCanvas.y + 30.0)
}


