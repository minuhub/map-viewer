package mapviewer.parser.shp

import mapviewer.parser.domain.RecordHeader
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RecordHeaderReader {

    fun readRecordHeader(buf: ByteBuffer): RecordHeader {

        buf.order(ByteOrder.BIG_ENDIAN)
        val recordNumber = buf.int
        val contentLength = buf.int

        return RecordHeader(recordNumber, contentLength)
    }
}