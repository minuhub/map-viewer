package mapviewer.parser.shp

import mapviewer.parser.domain.ShapeType
import mapviewer.parser.domain.ShpHeader
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HeaderReader {

    fun read(file: File): ShpHeader {

        val fileInputStream = FileInputStream(file)
        val fileSize = file.length().toInt()
        val buf = ByteBuffer.allocate(fileSize)
        val channel = fileInputStream.channel
        channel.read(buf)
        buf.flip()
        val fileCode = buf.int
        unused(buf)
        val fileLength = buf.int

        buf.order(ByteOrder.LITTLE_ENDIAN)
        val version = buf.int
        val shapeType = ShapeType.fromType(buf.int)
        val minX = buf.double
        val minY = buf.double
        val maxX = buf.double
        val maxY = buf.double
        val minZ = buf.double
        val maxZ = buf.double
        val minM = buf.double
        val maxM = buf.double

        return ShpHeader(fileCode, fileLength, version, shapeType, minX, minY, maxX, maxY, minZ, maxZ, minM, maxM)
    }

    private fun unused(buf: ByteBuffer) {
        for (i in 1..5) {
            buf.int
        }
    }
}