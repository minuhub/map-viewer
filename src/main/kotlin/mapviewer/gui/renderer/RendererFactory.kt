package mapviewer.gui.renderer

import mapviewer.parser.domain.ShapeType
import mapviewer.parser.domain.ShpHeader

class RendererFactory {
    companion object {
        private val polygonRenderer = PolygonRenderer()
        private val polylineRenderer = PolylineRenderer()
        private val pointRenderer = PointRenderer()
        fun chooseRenderer(shpHeader: ShpHeader): Renderer {
            return when (shpHeader.shapeType) {
                ShapeType.Polygon -> polygonRenderer
                ShapeType.PolyLine -> polylineRenderer
                ShapeType.Point -> pointRenderer
                ShapeType.MultiPoint -> TODO()
            }
        }
    }
}