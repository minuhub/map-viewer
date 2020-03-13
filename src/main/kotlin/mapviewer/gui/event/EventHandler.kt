package mapviewer.gui.event

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import mapviewer.gui.RenderingManager
import mapviewer.gui.domain.MapParams
import mapviewer.parser.domain.Point
import mapviewer.parser.domain.Shp

class EventHandler(var canvas: Canvas, shp: Shp, mapParams: MapParams) {
    private val startPoints = Point(0.0, 0.0)
    private var draggedLengthX = 0.0
    private var draggedLengthY = 0.0

    private val gc = canvas.graphicsContext2D

    val onMousePressedEventHandler =
            EventHandler<MouseEvent> { event ->
                if (!event.isPrimaryButtonDown) return@EventHandler
                startPoints.pointX = event.sceneX
                startPoints.pointY = event.sceneY
                draggedLengthX = 0.0
                draggedLengthY = 0.0
            }
    val onMouseDraggedEventHandler =
            EventHandler<MouseEvent> { event ->
                if (!event.isPrimaryButtonDown) return@EventHandler
                draggedLengthX = event.sceneX - startPoints.pointX
                draggedLengthY = event.sceneY - startPoints.pointY
                canvas.translateX = draggedLengthX
                canvas.translateY = draggedLengthY
            }
    val onMouseReleasedEventHandler =
            EventHandler<MouseEvent> { event ->
                if (event.isPrimaryButtonDown) return@EventHandler
                canvas.translateX = 0.0
                canvas.translateY = 0.0
                mapParams.tileBBox.translateBBoxX(-draggedLengthX / mapParams.tileSizeInCanvas)
                mapParams.tileBBox.translateBBoxY(-draggedLengthY / mapParams.tileSizeInCanvas)
                RenderingManager.clearCanvas(canvas, mapParams)
//                gc.isImageSmoothing = false
                RenderingManager.renderAllOnCanvas(canvas, shp, mapParams, true)
                event.consume()
            }


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
                    mapParams.zoomLevel++
                    allTileCenterX += diffInTileX / 2
                    allTileCenterY += diffInTileY / 2
                    nextLevelAllTileCenterX = allTileCenterX * 2
                    nextLevelAllTileCenterY = allTileCenterY * 2
                } else {
                    mapParams.zoomLevel--
                    allTileCenterX -= diffInTileX
                    allTileCenterY -= diffInTileY
                    nextLevelAllTileCenterX = allTileCenterX / 2
                    nextLevelAllTileCenterY = allTileCenterY / 2
                }

                mapParams.tileBBox.centerX = nextLevelAllTileCenterX + gapX
                mapParams.tileBBox.centerY = nextLevelAllTileCenterY + gapY
                mapParams.tileBBox.updateBBoxByCenter(0.5)

                RenderingManager.clearCanvas(canvas, mapParams)
                RenderingManager.renderAllOnCanvas(canvas, shp, mapParams, true)
                event.consume()
            }

    companion object {
        fun addAllEventHandler(scene: Scene, canvas: Canvas, shp: Shp, mapParams: MapParams) {
            val eventHandler = EventHandler(canvas, shp, mapParams)
            scene.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler.onMousePressedEventHandler)
            scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler.onMouseDraggedEventHandler)
            scene.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler.onMouseReleasedEventHandler)
            scene.addEventFilter(ScrollEvent.ANY, eventHandler.onScrollEventHandler)
        }
    }
}