package mapviewer.parser.shp

import mapviewer.parser.domain.ShapeType
import mapviewer.parser.domain.Shp
import mapviewer.parser.domain.ShpHeader
import mapviewer.parser.domain.ShpRecord
import java.io.File

class Parser {

    fun parse(path: String): Shp {
        val shpHeader = parseHeader(path)
        val shpRecordList = parseRecord(path, shpHeader.shapeType)
        return Shp(shpHeader, shpRecordList)
    }

    private fun parseHeader(path: String): ShpHeader {
        val headerReader = HeaderReader()
        val file = File(path)
        return headerReader.read(file)
    }

    private fun parseRecord(path: String, type: ShapeType): MutableList<ShpRecord> {
        val recordReader = RecordReader()
        val file = File(path)
        return recordReader.read(file, type)
    }
}