package mapviewer.gui.event

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import mapviewer.gui.domain.MapParams
import mapviewer.gui.domain.SelectedFeatureParams
import mapviewer.parser.domain.Point
import mapviewer.parser.domain.Shp

class LeftMouseEventHandler(canvas: Canvas, highlightCanvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams, selectedFeatureParams: SelectedFeatureParams) {
    private val startPoints = Point(0.0, 0.0)
    private var draggedLengthX = 0.0
    private var draggedLengthY = 0.0
    private var primaryButtonFlag = false

    val onMousePressedEventHandler =
            EventHandler<MouseEvent> { event ->
                if (event.isPrimaryButtonDown) {
                    primaryButtonFlag = true
                    startPoints.x = event.sceneX
                    startPoints.y = event.sceneY
                    draggedLengthX = 0.0
                    draggedLengthY = 0.0
                }
            }
    val onMouseDraggedEventHandler =
            EventHandler<MouseEvent> { event ->
                if (primaryButtonFlag) {
                    draggedLengthX = event.sceneX - startPoints.x
                    draggedLengthY = event.sceneY - startPoints.y
                    canvas.translateX = draggedLengthX
                    canvas.translateY = draggedLengthY
                    highlightCanvas.translateX = draggedLengthX
                    highlightCanvas.translateY = draggedLengthY
                }
            }
    val onMouseReleasedEventHandler =
            EventHandler<MouseEvent> { event ->
                if (primaryButtonFlag) {
                    primaryButtonFlag = false
                    highlightCanvas.translateX = 0.0
                    highlightCanvas.translateY = 0.0

                    canvas.translateX = 0.0
                    canvas.translateY = 0.0
                    mapParams.tileBBox.translateBBoxX(-draggedLengthX / mapParams.tileSizeInCanvas)
                    mapParams.tileBBox.translateBBoxY(-draggedLengthY / mapParams.tileSizeInCanvas)

                    EventUtil.reRenderShpCanvas(canvas, shpList, mapParams)
                    EventUtil.reRenderHighlightCanvas(highlightCanvas, shpList, mapParams, selectedFeatureParams)
                    event.consume()
                }
            }

    companion object {
        fun addAllEventHandler(scene: Scene, canvas: Canvas, highlightCanvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams, selectedFeatureParams: SelectedFeatureParams) {
            val eventHandler = LeftMouseEventHandler(canvas, highlightCanvas, shpList, mapParams, selectedFeatureParams)
            scene.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler.onMousePressedEventHandler)
            scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler.onMouseDraggedEventHandler)
            scene.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler.onMouseReleasedEventHandler)
        }
    }
}