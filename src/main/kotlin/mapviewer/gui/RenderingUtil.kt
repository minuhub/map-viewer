package mapviewer.gui

import mapviewer.gui.domain.MapParams
import mapviewer.gui.domain.PointArray
import mapviewer.parser.domain.BBox
import mapviewer.parser.domain.Point
import kotlin.math.pow
class RenderingUtil {
    companion object {
        //below functions : used for highlighting the selected area
        fun canvasToShpX(windowPointX: Double, mapParams: MapParams): Double {
            return RenderingManager.leftTopTileIndexInShp.x + (windowPointX - RenderingManager.leftTopTileIndexInCanvas.x) / mapParams.tileSizeInCanvas * mapParams.shpBBox.width
        }

        fun canvasToShpY(windowPointY: Double, mapParams: MapParams): Double {
            return RenderingManager.leftTopTileIndexInShp.y - (windowPointY - RenderingManager.leftTopTileIndexInCanvas.y) / mapParams.tileSizeInCanvas * mapParams.shpBBox.height
        }

        fun translatePointsToWholeCanvas(pointList: MutableList<Point>, mapParams: MapParams, numPoints: Int, tileIndexXinCanvas: Double, tileIndexYinCanvas: Double): PointArray {
            val pointXArray = DoubleArray(numPoints)
            val pointYArray = DoubleArray(numPoints)
            for (index in 0 until numPoints) {
                pointXArray[index] = shpToWholeCanvasX(pointList[index].x, mapParams)
                pointYArray[index] = shpToWholeCanvasY(pointList[index].y, mapParams)
            }
            return PointArray(pointXArray, pointYArray)
        }

        private fun shpToWholeCanvasX(shpPointX: Double, mapParams: MapParams): Double {
            val adjustedShpPointX = (shpPointX - RenderingManager.leftTopTileIndexInShp.x) * mapParams.shpBBox.bBoxToCanvasRatioX
            return RenderingManager.leftTopTileIndexInCanvas.x + adjustedShpPointX
        }

        private fun shpToWholeCanvasY(shpPointY: Double, mapParams: MapParams): Double {
            val adjustedShpPointY = (shpPointY - RenderingManager.leftTopTileIndexInShp.y) * mapParams.shpBBox.bBoxToCanvasRatioY
            return RenderingManager.leftTopTileIndexInCanvas.y - adjustedShpPointY
        }

        //below functions : used for normal rendering
        fun translatePointsToTileInCanvas(pointList: MutableList<Point>, mapParams: MapParams, numPoints: Int, tileIndexXinCanvas: Double, tileIndexYinCanvas: Double): PointArray {
            val pointXArray = DoubleArray(numPoints)
            val pointYArray = DoubleArray(numPoints)
            for (index in 0 until numPoints) {
                pointXArray[index] = shpToCanvasX(pointList[index].x, mapParams, tileIndexXinCanvas)
                pointYArray[index] = shpToCanvasY(pointList[index].y, mapParams, tileIndexYinCanvas)
            }
            return PointArray(pointXArray, pointYArray)
        }

        private fun shpToCanvasX(shpPointX: Double, mapParams: MapParams, tileIndexXinCanvas: Double): Double {
            val adjustedShpPointX = (shpPointX - mapParams.shpBBox.centerX) * mapParams.shpBBox.bBoxToCanvasRatioX
            return adjustedShpPointX + (mapParams.tileSizeInCanvas / 2) + tileIndexXinCanvas
        }

        private fun shpToCanvasY(shpPointY: Double, mapParams: MapParams, tileIndexYinCanvas: Double): Double {
            val adjustedShpPointY = mapParams.tileSizeInCanvas - (shpPointY - mapParams.shpBBox.centerY) * mapParams.shpBBox.bBoxToCanvasRatioY
            return adjustedShpPointY - (mapParams.tileSizeInCanvas / 2) + tileIndexYinCanvas
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
            } else !(aBBox.minY > bBBox.maxY || bBBox.minY > aBBox.maxY)
        }
    }
}


