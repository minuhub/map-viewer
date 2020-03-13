package mapviewer.parser.shp

import mapviewer.parser.domain.RecordContent
import mapviewer.parser.domain.ShapeType
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RecordContentReader {

    private val pointRecordReader = PointRecordReader()
    private val polyLineRecordReader = PolylineRecordReader()
    private val polygonRecordReader = PolygonRecordReader()
    private val multiPointRecordReader = MultiPointRecordReader()

    fun readRecordContent(buf: ByteBuffer, type: ShapeType): RecordContent {

        buf.order(ByteOrder.LITTLE_ENDIAN)
        return when (type) {
            ShapeType.Point -> pointRecordReader.readPoint(buf)
            ShapeType.PolyLine -> polyLineRecordReader.readPolyline(buf)
            ShapeType.Polygon -> polygonRecordReader.readPolygon(buf)
            ShapeType.MultiPoint -> multiPointRecordReader.readMultiPoint(buf)
        }
    }
}