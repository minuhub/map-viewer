package mapviewer.gui.event

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.ScrollEvent
import mapviewer.gui.domain.MapParams
import mapviewer.gui.domain.SelectedFeatureParams
import mapviewer.parser.domain.Shp

class ScrollEventHandler(canvas: Canvas, highlightCanvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams, selectedFeatureParams: SelectedFeatureParams) {

    val onScrollEventHandler =
            EventHandler<ScrollEvent> { event ->
                val allTileWidth = mapParams.canvasBBox.width / mapParams.tileSizeInCanvas
                val allTileHeight = mapParams.canvasBBox.height / mapParams.tileSizeInCanvas

                val diffInCanvasX = event.sceneX - mapParams.canvasBBox.centerX
                val diffInCanvasY = event.sceneY - mapParams.canvasBBox.centerY

                val diffInTileX = diffInCanvasX * allTileWidth / mapParams.canvasBBox.width
                val diffInTileY = diffInCanvasY * allTileHeight / mapParams.canvasBBox.height

                var allTileCenterX = mapParams.tileBBox.minX + allTileWidth / 2
                var allTileCenterY = mapParams.tileBBox.minY + allTileHeight / 2

                val gapX = mapParams.tileBBox.centerX - allTileCenterX
                val gapY = mapParams.tileBBox.centerY - allTileCenterY

                var nextLevelAllTileCenterX = 0.0
                var nextLevelAllTileCenterY = 0.0

                if (event.deltaY < 0) {
                    mapParams.zoomLevel--
                    allTileCenterX -= diffInTileX
                    allTileCenterY -= diffInTileY
                    nextLevelAllTileCenterX = allTileCenterX / 2
                    nextLevelAllTileCenterY = allTileCenterY / 2
                } else {
                    mapParams.zoomLevel++
                    allTileCenterX += diffInTileX / 2
                    allTileCenterY += diffInTileY / 2
                    nextLevelAllTileCenterX = allTileCenterX * 2
                    nextLevelAllTileCenterY = allTileCenterY * 2
                }

                mapParams.tileBBox.centerX = nextLevelAllTileCenterX + gapX
                mapParams.tileBBox.centerY = nextLevelAllTileCenterY + gapY
                mapParams.tileBBox.updateBBoxByCenter(0.5)

                EventUtil.reRenderShpCanvas(canvas, shpList, mapParams)
                EventUtil.reRenderHighlightCanvas(highlightCanvas, shpList, mapParams, selectedFeatureParams)

                event.consume()
            }

    companion object {
        fun addAllEventHandler(scene: Scene, canvas: Canvas, highlightCanvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams, selectedFeatureParams: SelectedFeatureParams) {
            val eventHandler = ScrollEventHandler(canvas, highlightCanvas, shpList, mapParams, selectedFeatureParams)
            scene.addEventFilter(ScrollEvent.ANY, eventHandler.onScrollEventHandler)
        }
    }
}