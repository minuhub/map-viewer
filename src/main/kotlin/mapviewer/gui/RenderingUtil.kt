package mapviewer.gui

import mapviewer.gui.domain.MapParams
import mapviewer.gui.domain.PointArray
import mapviewer.parser.domain.BBox
import mapviewer.parser.domain.Point
import kotlin.math.pow

class RenderingUtil {
    companion object {

        fun translatePoints(pointList: MutableList<Point>, mapParams: MapParams, numPoints: Int): PointArray {
            val pointXArray = DoubleArray(numPoints)
            val pointYArray = DoubleArray(numPoints)
            for (index in 0 until numPoints) {
                pointXArray[index] = translateX(pointList[index].pointX, mapParams)
                pointYArray[index] = translateY(pointList[index].pointY, mapParams)
            }
            return PointArray(pointXArray, pointYArray)
        }
        fun translateX(shpPointX: Double, mapParams: MapParams): Double {
            return (shpPointX - mapParams.shpBBox.centerX) * mapParams.shpBBox.bBoxToCanvasRatioX + mapParams.canvasBBox.width / 2
        }

        fun reverseTranslateX(windowPointX: Double, mapParams: MapParams): Double {
            return ((windowPointX - (mapParams.canvasBBox.width / 2)) / mapParams.shpBBox.bBoxToCanvasRatioX + mapParams.shpBBox.centerX)
        }

        fun translateY(shpPointY: Double, mapParams: MapParams): Double {
            return (mapParams.canvasBBox.height - (shpPointY - mapParams.shpBBox.centerX) * mapParams.shpBBox.bBoxToCanvasRatioY - (mapParams.canvasBBox.height / 2))
        }

        fun reverseTranslateY(windowPointY: Double, mapParams: MapParams): Double {
            return (mapParams.canvasBBox.height - (mapParams.canvasBBox.height / 2) - windowPointY) / mapParams.shpBBox.bBoxToCanvasRatioY + mapParams.shpBBox.centerX
        }
        fun translatePoints(pointList: MutableList<Point>, mapParams: MapParams, numPoints: Int, tileIndexXinCanvas: Double, tileIndexYinCanvas: Double): PointArray {
            val pointXArray = DoubleArray(numPoints)
            val pointYArray = DoubleArray(numPoints)
            for (index in 0 until numPoints) {
                pointXArray[index] = shpToCanvasX(pointList[index].pointX, mapParams, tileIndexXinCanvas)
                pointYArray[index] = shpToCanvasY(pointList[index].pointY, mapParams, tileIndexYinCanvas)
            }
            return PointArray(pointXArray, pointYArray)
        }

        fun shpToCanvasX(shpPointX: Double, mapParams: MapParams, tileIndexXinCanvas: Double): Double {
            val adjustedShpPointX = (shpPointX - mapParams.shpBBox.centerX) * mapParams.shpBBox.bBoxToCanvasRatioX
            return  adjustedShpPointX + (mapParams.tileSizeInCanvas / 2) + tileIndexXinCanvas
        }

        fun shpToCanvasY(shpPointY: Double, mapParams: MapParams, tileIndexYinCanvas: Double): Double {
            val adjustedShpPointY = mapParams.tileSizeInCanvas - (shpPointY - mapParams.shpBBox.centerY) * mapParams.shpBBox.bBoxToCanvasRatioY
            return  adjustedShpPointY - (mapParams.tileSizeInCanvas / 2) + tileIndexYinCanvas
        }

        fun calculateShpBBoxEdgeLength(mapParams: MapParams): Double {
            return (mapParams.standardBBox.longerEdge / ((2.0).pow(mapParams.zoomLevel)))
        }

        fun tileToCanvasX(tileIndexX: Double, x: Double, canvasBBoxX: Double, mapParams: MapParams): Double {
            return canvasBBoxX - (x - tileIndexX) * mapParams.tileSizeInCanvas
        }

        fun tileToCanvasY(tileIndexY: Double, y: Double, canvasBBoxY: Double, mapParams: MapParams): Double {
            return canvasBBoxY - (y - tileIndexY) * mapParams.tileSizeInCanvas
        }

        fun tileToShpX(x: Double, mapParams: MapParams): Double {
            return mapParams.standardBBox.minX + x * mapParams.standardBBox.longerEdge / ((2.0).pow(mapParams.zoomLevel))
        }

        fun tileToShpY(y: Double, mapParams: MapParams): Double {
            return mapParams.standardBBox.maxY - y * mapParams.standardBBox.longerEdge / ((2.0).pow(mapParams.zoomLevel))
        }

//        fun isInsideBBox(recordContent: RecordContent, viewBBox: FunctionalBBox): Boolean {
//            return if (!(viewBBox.minX <= recordContent.minX && recordContent.minX <= viewBBox.maxX) ||
//                    !(viewBBox.minY <= recordContent.minY && recordContent.minY <= viewBBox.maxY)

        fun isaBBoxInbBBox(aBBox: BBox, bBBox: BBox): Boolean {
            return if (!(bBBox.minX <= aBBox.minX && aBBox.minX <= bBBox.maxX) ||
                !(bBBox.minY <= aBBox.minY && aBBox.minY <= bBBox.maxY)
            ) {
                false
            } else if (!(bBBox.minX <= aBBox.maxX && aBBox.maxX <= bBBox.maxX) ||
                !(bBBox.minY <= aBBox.minY && aBBox.minY <= bBBox.maxY)
            ) {
                false
            } else if (!(bBBox.minX <= aBBox.minX && aBBox.minX <= bBBox.maxX) ||
                !(bBBox.minY <= aBBox.maxY && aBBox.maxY <= bBBox.maxY)
            ) {
                false
            } else !(!(bBBox.minX <= aBBox.maxX && aBBox.maxX <= bBBox.maxX) ||
                    !(bBBox.minY <= aBBox.maxY && aBBox.maxY <= bBBox.maxY))
        }

        fun isOverlap(aBBox: BBox, bBBox: BBox): Boolean {
            return if (aBBox.minX > bBBox.maxX || bBBox.minX > aBBox.maxX) {
                false
            } else if (aBBox.minY > bBBox.maxY || bBBox.minY > aBBox.maxY) {
                false
            } else true
        }
    }
}


