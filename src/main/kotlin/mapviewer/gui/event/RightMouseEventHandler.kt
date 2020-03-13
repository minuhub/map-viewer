package mapviewer.gui.event

import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import mapviewer.gui.RenderingUtil
import mapviewer.gui.domain.MapParams
import mapviewer.gui.domain.SelectedFeatureParams
import mapviewer.gui.highlight.HighlightManager
import mapviewer.parser.domain.BBox
import mapviewer.parser.domain.Point
import mapviewer.parser.domain.Shp
import kotlin.math.abs
import kotlin.math.min

class RightMouseEventHandler(group: Group, highlightCanvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams, selectedFeatureParams: SelectedFeatureParams) {
    private var secondaryButtonFlag = false
    private val selection = Rectangle()
    private val vertex = Point(0.0, 0.0)

    val onRightMousePressedEventHandler =
        EventHandler<MouseEvent> { event ->
            if (event.isSecondaryButtonDown) {
                secondaryButtonFlag = true
                vertex.pointX = event.x
                vertex.pointY = event.y
                selection.x = event.x
                selection.y = event.y
                selection.fill = Color.TRANSPARENT
                selection.stroke = Color.BLACK
                selection.strokeDashArray.add(10.0)
                group.children.add(selection)
            }
        }

    val onRightMouseDraggedEventHandler =
        EventHandler<MouseEvent> { event ->
            if (secondaryButtonFlag) {
                selection.width = abs(event.x - vertex.pointX)
                selection.height = abs(event.y - vertex.pointY)
                selection.x = min(vertex.pointX, event.x)
                selection.y = min(vertex.pointY, event.y)
            }
        }

    val onRightMouseReleasedEventHandler =
        EventHandler<MouseEvent> { event ->
            if (secondaryButtonFlag) {
                secondaryButtonFlag = false
                selectedFeatureParams.featureList.clear()

                if (selection.width == 0.0 || selection.height == 0.0) {
                    val eventX = RenderingUtil.reverseTranslateX(event.sceneX, mapParams)
                    val eventY = RenderingUtil.reverseTranslateY(event.sceneY, mapParams)

                    for (index in shpList.indices) {
                        selectedFeatureParams.featureList.add(HighlightManager.selectOneFeature(shpList[index], eventX, eventY, mapParams))
                        if (selectedFeatureParams.featureList[index].isNotEmpty()) {
                            println("The ${shpList[index].shpHeader.shapeType} number is ${selectedFeatureParams.featureList[index]}")
                        }
                    }
                } else {
                    val selectedBBox = makeSelectedBBox(selection, mapParams)
                    for (index in shpList.indices) {
                        selectedFeatureParams.featureList.add(HighlightManager.selectFeatures(shpList[index], selectedBBox))
                        if (selectedFeatureParams.featureList[index].isNotEmpty()) {
                            println("The ${shpList[index].shpHeader.shapeType} number is ${selectedFeatureParams.featureList[index]}")
                        }
                    }
                }

                EventUtil.reRenderHighlightCanvas(highlightCanvas, shpList, mapParams, selectedFeatureParams)
                selection.width = 0.0
                selection.height = 0.0
                group.children.remove(selection)
            }
        }

    private fun makeSelectedBBox(selection: Rectangle, mapParams: MapParams): BBox {
        val minX = RenderingUtil.reverseTranslateX(selection.x, mapParams)
        val minY = RenderingUtil.reverseTranslateY(selection.y + selection.height, mapParams)
        val maxX = RenderingUtil.reverseTranslateX(selection.x + selection.width, mapParams)
        val maxY = RenderingUtil.reverseTranslateY(selection.y, mapParams)
        return BBox(minX, minY, maxX, maxY)
    }

    companion object {
        fun addAllEventHandler(group: Group, scene: Scene, highlightCanvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams, selectedFeatureParams: SelectedFeatureParams) {
            val eventHandler = RightMouseEventHandler(group, highlightCanvas, shpList, mapParams, selectedFeatureParams)
            scene.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler.onRightMousePressedEventHandler)
            scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler.onRightMouseDraggedEventHandler)
            scene.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler.onRightMouseReleasedEventHandler)
        }
    }
}