package mapviewer.gui.renderer

import javafx.scene.canvas.Canvas
import mapviewer.gui.domain.MapParams
import mapviewer.parser.domain.ShpRecord

interface Renderer {
    fun renderShpOnCanvas(canvas: Canvas, shpRecordList: MutableList<ShpRecord>, mapParams: MapParams, tileIndexXinCanvas: Double, tileIndexYinCanvas: Double)
}
