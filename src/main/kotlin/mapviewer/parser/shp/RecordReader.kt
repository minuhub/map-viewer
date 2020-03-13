package mapviewer.parser.shp

import mapviewer.parser.domain.ShapeType
import mapviewer.parser.domain.ShpRecord
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer

class RecordReader {

    private val recordStart = 100

    fun read(file: File, type: ShapeType): MutableList<ShpRecord> {

        var recordList = mutableListOf<ShpRecord>()

        val fileInputStream = FileInputStream(file)
        val fileSize = file.length().toInt()
        val buf = ByteBuffer.allocate(fileSize)
        val channel = fileInputStream.channel
        channel.read(buf)
        buf.flip()
        buf.position(recordStart)

        var recordOffset = recordStart
        val recordHeaderReader = RecordHeaderReader()
        val recordContentReader = RecordContentReader()

        while (recordOffset < fileSize) {
            val recordHeader = recordHeaderReader.readRecordHeader(buf)
            recordOffset += 8

            val recordContent = recordContentReader.readRecordContent(buf, type)
            recordOffset += convertWordToByte(recordHeader.contentLength)

            recordList.add(ShpRecord(recordHeader, recordContent))
        }

        return recordList
    }

    private fun convertWordToByte(word: Int) = word * 2
}