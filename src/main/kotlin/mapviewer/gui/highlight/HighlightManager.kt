package mapviewer.gui.highlight

import javafx.scene.canvas.Canvas
import mapviewer.gui.RenderingUtil
import mapviewer.gui.domain.MapParams
import mapviewer.parser.domain.*

object HighlightManager {
    private val polygonHighlighter = PolygonHighlighter()
    private val polylineHighlighter = PolylineHighlighter()
    private val pointHighlighter = PointHighlighter()

    fun selectOneFeature(shp: Shp, eventX: Double, eventY: Double, mapParams: MapParams): MutableList<ShpRecord> {
        val selectedFeatureIdList = mutableListOf<ShpRecord>()
        for (shpRecord in shp.shpRecordList) {
            if (!HighlightUtil.isInsideRecordBBox(shpRecord.recordContent.recordBBox, eventX, eventY, mapParams)) continue
            when (shp.shpHeader.shapeType) {
                ShapeType.Polygon -> {
                    for (partIndex in 0 until shpRecord.recordContent.numParts) {
                        if (HighlightUtil.isInsidePolygon(shpRecord.recordContent.points[partIndex],
                                        shpRecord.recordContent.parts[partIndex + 1] - shpRecord.recordContent.parts[partIndex], eventX, eventY)) {
                            selectedFeatureIdList.add(shpRecord)
                            break
                        }
                    }
                }
                ShapeType.PolyLine -> {
                    for (partIndex in 0 until shpRecord.recordContent.numParts) {
                        if (HighlightUtil.isOnPolyline(shpRecord.recordContent.points[partIndex],
                                        shpRecord.recordContent.parts[partIndex + 1] - shpRecord.recordContent.parts[partIndex], eventX, eventY, mapParams)) {
                            selectedFeatureIdList.add(shpRecord)
                            break
                        }
                    }
                }
                ShapeType.Point -> selectedFeatureIdList.add(shpRecord)
                ShapeType.MultiPoint -> TODO()
            }
        }
        return selectedFeatureIdList
    }

    fun selectFeatures(shp: Shp, selectedBBox: BBox): MutableList<ShpRecord> {
        val selectedFeatureIdList = mutableListOf<ShpRecord>()
        for (shpRecord in shp.shpRecordList) {
            if (!RenderingUtil.isOverlap(shpRecord.recordContent.recordBBox, selectedBBox)) continue
            when (shp.shpHeader.shapeType) {
                ShapeType.Polygon, ShapeType.PolyLine -> {
                    for (partIndex in 0 until shpRecord.recordContent.numParts) {
                        if (HighlightUtil.isInsideSelectedBBox(selectedBBox, shpRecord.recordContent.points[partIndex],
                                        shpRecord.recordContent.parts[partIndex + 1] - shpRecord.recordContent.parts[partIndex])) {
                            selectedFeatureIdList.add(shpRecord)
                            break
                        }
                    }
                }
                ShapeType.Point -> selectedFeatureIdList.add(shpRecord)
                ShapeType.MultiPoint -> TODO()
            }
        }
        return selectedFeatureIdList
    }

    fun highlightRecord(highlightCanvas: Canvas, shpHeader: ShpHeader, selectedFeatureList: MutableList<ShpRecord>, mapParams: MapParams) {
        if (selectedFeatureList.isNotEmpty()) {
            when (shpHeader.shapeType) {
                ShapeType.Polygon -> polygonHighlighter.highlight(highlightCanvas, selectedFeatureList, mapParams)
                ShapeType.PolyLine -> polylineHighlighter.highlight(highlightCanvas, selectedFeatureList, mapParams)
                ShapeType.Point -> pointHighlighter.highlight(highlightCanvas, selectedFeatureList, mapParams)
                ShapeType.MultiPoint -> TODO()
            }
        }
    }
}