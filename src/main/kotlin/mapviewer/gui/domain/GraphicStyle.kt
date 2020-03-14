package mapviewer.gui.domain

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font


enum class GraphicStyle(var color: Color, var lineWidth: Double, var fontSize: Double?) {
    Text(Color.BLUE, 1.0, 30.0),
    Tile(Color.RED, 2.0, null),
    Shp(Color.BLACK, 1.0, null);

    companion object {
        fun set(gc: GraphicsContext, graphicStyle: GraphicStyle) {
            gc.stroke = graphicStyle.color
            gc.lineWidth = graphicStyle.lineWidth
            gc.font = graphicStyle.fontSize?.let { Font(it) }
        }
    }
}