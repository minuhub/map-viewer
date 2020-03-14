package mapviewer.gui.event

import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.TransferMode
import mapviewer.gui.RenderingManager
import mapviewer.gui.domain.FunctionalBBox
import mapviewer.gui.domain.MapParams
import mapviewer.parser.domain.BBox
import mapviewer.gui.domain.SelectedFeatureParams
import mapviewer.parser.domain.Shp
import mapviewer.parser.shp.Parser

object SceneEventHandler {
    private val parser = Parser()
    private val pathList = mutableListOf<String>()
    private var firstFlag = false
    private val windowShortenFactor = 10000.0

    fun dragAndDropWithAllHandler(group: Group, scene: Scene, canvas: Canvas, highlightCanvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams) {
        scene.onDragOver = EventHandler { event ->
            val db = event.dragboard
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY)
            } else {
                event.consume()
            }
        }
        scene.onDragDropped = EventHandler { event ->
            val db = event.dragboard
            var success = false
            if (db.hasFiles()) {
                success = true
                for (file in db.files) {
                    if (file.absolutePath in pathList) continue
                    println(file.absolutePath)
                    pathList.add(file.absolutePath)
                    shpList.add(parser.parse(file.absolutePath))
                    if (!firstFlag) {
                        firstFlag = true
                        initialize(group, scene, canvas, highlightCanvas, shpList, mapParams)
                    }
                    RenderingManager.renderAllOnCanvas(canvas, shpList.last(), mapParams)
                }
            }
            event.isDropCompleted = success
            event.consume()
        }
    }

    private fun initialize(group: Group, scene: Scene, canvas: Canvas, highlightCanvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams) {
        mapParams.shpBBox = FunctionalBBox(
                shpList.first().shpHeader.minX + windowShortenFactor,
                shpList.first().shpHeader.minY + windowShortenFactor,
                shpList.first().shpHeader.maxX - windowShortenFactor,
                shpList.first().shpHeader.maxY - windowShortenFactor
        )
        mapParams.shpBBox.updateWidth()
        mapParams.shpBBox.updateHeight()
        mapParams.shpBBox.updateCenterX()
        mapParams.shpBBox.updateCenterY()

        val selectedFeatureParams = SelectedFeatureParams()
        LeftMouseEventHandler.addAllEventHandler(scene, canvas, highlightCanvas, shpList, mapParams, selectedFeatureParams)
        ScrollEventHandler.addAllEventHandler(scene, canvas, highlightCanvas, shpList, mapParams, selectedFeatureParams)
        RightMouseEventHandler.addAllEventHandler(group, scene, highlightCanvas, shpList, mapParams, selectedFeatureParams)
    }
}