package mapviewer.parser.shp

import mapviewer.parser.domain.BBox
import mapviewer.parser.domain.Point
import mapviewer.parser.domain.PointRecord
import mapviewer.parser.domain.ShapeType
import java.nio.ByteBuffer

class PointRecordReader {

    fun readPoint(buf: ByteBuffer): PointRecord {

        val shapeType = ShapeType.fromType(buf.int)
        val points = mutableListOf(mutableListOf(Point(buf.double, buf.double)))
        val recordBBox = BBox(points[0][0].pointX, points[0][0].pointY, points[0][0].pointX, points[0][0].pointY)

        return PointRecord(shapeType, recordBBox, points = points)
    }
}