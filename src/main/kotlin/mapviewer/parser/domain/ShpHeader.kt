package mapviewer.parser.domain

data class ShpHeader(
        val fileCode: Int,
        val fileLength: Int,
        val version: Int,
        val shapeType: ShapeType,
        val minX: Double,
        val minY: Double,
        val maxX: Double,
        val maxY: Double,
        val minZ: Double,
        val maxZ: Double,
        val minM: Double,
        val maxM: Double
)