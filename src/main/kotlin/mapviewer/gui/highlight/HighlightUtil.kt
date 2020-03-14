package mapviewer.gui.highlight

import mapviewer.gui.domain.MapParams
import mapviewer.parser.domain.BBox
import mapviewer.parser.domain.Point

object HighlightUtil {
    private const val highlightBuffer = 15.0

    fun isInsideRecordBBox(recordBBox: BBox, eventX: Double, eventY: Double, mapParams: MapParams): Boolean {
        val translatedBuf = highlightBuffer / mapParams.shpBBox.bBoxToCanvasRatioX
        return if (recordBBox.minX - translatedBuf / 2 > eventX || eventX > recordBBox.maxX + translatedBuf / 2) {
            false
        } else !(recordBBox.minY - translatedBuf / 2 > eventY || eventY > recordBBox.maxY + translatedBuf / 2)
    }

    fun isInsidePolygon(pointList: MutableList<Point>, numPoints: Int, eventX: Double, eventY: Double): Boolean {
        var isInnerPoint = false
        for (index in 0 until numPoints - 1) {
            if ((pointList[index].y <= eventY) && (eventY < pointList[index + 1].y)
                || (pointList[index + 1].y <= eventY) && (eventY < pointList[index].y)
            ) {
                val crossPointX =
                    (eventY - pointList[index].y) * (pointList[index + 1].x - pointList[index].x) /
                            (pointList[index + 1].y - pointList[index].y) + pointList[index].x
                if (eventX < crossPointX) {
                    isInnerPoint = !isInnerPoint
                } else if (eventX == crossPointX) {
                    return true
                }
            }
        }
        return isInnerPoint
    }

    fun isOnPolyline(pointList: MutableList<Point>, numPoints: Int, eventX: Double, eventY: Double, mapParams: MapParams): Boolean {
        val translatedBuf = highlightBuffer / mapParams.shpBBox.bBoxToCanvasRatioX
        for (index in 0 until numPoints - 1) {
            if ((pointList[index].y <= eventY) && (eventY < pointList[index + 1].y)
                || (pointList[index + 1].y <= eventY) && (eventY < pointList[index].y)
            ) {
                val crossPointX =
                    (eventY - pointList[index].y) * (pointList[index + 1].x - pointList[index].x) /
                            (pointList[index + 1].y - pointList[index].y) + pointList[index].x
                if (crossPointX - translatedBuf <= eventX && eventX <= crossPointX + translatedBuf) {
                    return true
                }
            }
            if ((pointList[index].x <= eventX) && (eventX < pointList[index + 1].x)
                || (pointList[index + 1].x <= eventX) && (eventX < pointList[index].x)
            ) {
                val crossPointY =
                    (eventX - pointList[index].x) * (pointList[index + 1].y - pointList[index].y) /
                            (pointList[index + 1].x - pointList[index].x) + pointList[index].y
                if (crossPointY - translatedBuf <= eventY && eventY <= crossPointY + translatedBuf) {
                    return true
                }
            }
        }
        return false
    }

    fun isInsideSelectedBBox(selectedBBox: BBox, pointList: MutableList<Point>, numPoints: Int): Boolean {
        for (index in 0 until numPoints) {
            if (selectedBBox.minX < pointList[index].x &&  pointList[index].x < selectedBBox.maxX &&
                selectedBBox.minY < pointList[index].y &&  pointList[index].y < selectedBBox.maxY)
                return true
        }
        return false
    }
}