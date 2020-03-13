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
            if ((pointList[index].pointY <= eventY) && (eventY < pointList[index + 1].pointY)
                || (pointList[index + 1].pointY <= eventY) && (eventY < pointList[index].pointY)
            ) {
                val crossPointX =
                    (eventY - pointList[index].pointY) * (pointList[index + 1].pointX - pointList[index].pointX) /
                            (pointList[index + 1].pointY - pointList[index].pointY) + pointList[index].pointX
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
            if ((pointList[index].pointY <= eventY) && (eventY < pointList[index + 1].pointY)
                || (pointList[index + 1].pointY <= eventY) && (eventY < pointList[index].pointY)
            ) {
                val crossPointX =
                    (eventY - pointList[index].pointY) * (pointList[index + 1].pointX - pointList[index].pointX) /
                            (pointList[index + 1].pointY - pointList[index].pointY) + pointList[index].pointX
                if (crossPointX - translatedBuf <= eventX && eventX <= crossPointX + translatedBuf) {
                    return true
                }
            }
            if ((pointList[index].pointX <= eventX) && (eventX < pointList[index + 1].pointX)
                || (pointList[index + 1].pointX <= eventX) && (eventX < pointList[index].pointX)
            ) {
                val crossPointY =
                    (eventX - pointList[index].pointX) * (pointList[index + 1].pointY - pointList[index].pointY) /
                            (pointList[index + 1].pointX - pointList[index].pointX) + pointList[index].pointY
                if (crossPointY - translatedBuf <= eventY && eventY <= crossPointY + translatedBuf) {
                    return true
                }
            }
        }
        return false
    }

    fun isInsideSelectedBBox(selectedBBox: BBox, pointList: MutableList<Point>, numPoints: Int): Boolean {
        for (index in 0 until numPoints) {
            if (selectedBBox.minX < pointList[index].pointX &&  pointList[index].pointX < selectedBBox.maxX &&
                selectedBBox.minY < pointList[index].pointY &&  pointList[index].pointY < selectedBBox.maxY)
                return true
        }
        return false
    }
}