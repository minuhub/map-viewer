package mapviewer

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.canvas.Canvas
import javafx.stage.Stage
import mapviewer.gui.SceneManager
import mapviewer.gui.domain.FunctionalBBox
import mapviewer.gui.domain.MapParams
import mapviewer.gui.domain.emptyBBox
import mapviewer.gui.event.SceneEventHandler
import mapviewer.parser.domain.Shp

class MainApp : Application() {
    private val windowWidth = 1200.0
    private val windowHeight = 800.0
    private val zoomLevel = 0
    private val tileSizeInCanvas = 300.0

    override fun start(primaryStage: Stage) {
        val shpList = mutableListOf<Shp>()
        val group = Group()
        val canvas = Canvas(windowWidth, windowHeight)
        val highlightCanvas = Canvas(windowWidth, windowHeight)

        val canvasBBox = FunctionalBBox(0.0, 0.0, windowWidth, windowHeight)
        val tileBBox = FunctionalBBox(0.0, 0.0, 1.0, 1.0)
        val shpBBox = emptyBBox
        val mapParams = MapParams(zoomLevel, tileSizeInCanvas, canvasBBox, tileBBox, shpBBox)
        val scene = SceneManager.makeScene(group, canvas, highlightCanvas, mapParams)
        SceneEventHandler.dragAndDropWithAllHandler(group, scene, canvas, highlightCanvas, shpList, mapParams)

        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    launch(MainApp::class.java)
}