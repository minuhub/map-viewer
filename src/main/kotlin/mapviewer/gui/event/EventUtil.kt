package mapviewer.gui.event

import javafx.scene.canvas.Canvas
import mapviewer.gui.RenderingManager
import mapviewer.gui.domain.MapParams
import mapviewer.gui.domain.SelectedFeatureParams
import mapviewer.gui.highlight.HighlightManager
import mapviewer.parser.domain.Shp

object EventUtil {
    fun reRenderShpCanvas(canvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams) {
        RenderingManager.clearCanvas(canvas, mapParams)
        for (shp in shpList) {
            RenderingManager.renderAllOnCanvas(canvas, shp, mapParams)
        }
    }

    fun reRenderHighlightCanvas(highlightCanvas: Canvas, shpList: MutableList<Shp>, mapParams: MapParams, selectedFeatureParams: SelectedFeatureParams) {
        RenderingManager.clearCanvas(highlightCanvas, mapParams)
        for (index in selectedFeatureParams.featureList.indices) {
            HighlightManager.highlightRecord(highlightCanvas, shpList[index].shpHeader, selectedFeatureParams.featureList[index], mapParams)
        }
    }
}