package mapviewer.gui

import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import mapviewer.gui.domain.MapParams
import tornadofx.*

object SceneManager {
    fun makeScene(group: Group, canvas: Canvas, highlightCanvas: Canvas, mapParams: MapParams): Scene {
        group.addChildIfPossible(canvas)
        group.addChildIfPossible(highlightCanvas)
        return Scene(group, mapParams.canvasBBox.width, mapParams.canvasBBox.height)
    }
}