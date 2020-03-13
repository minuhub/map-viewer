package mapviewer.parser.domain

data class PointRecord(
    override val shapeType: ShapeType,
    override val recordBBox: BBox,
    override val numParts: Int = 1,
    override val numPoints: Int = 1,
    override val parts: MutableList<Int> = mutableListOf(1),
    override val points: MutableList<MutableList<Point>>
) : RecordContent(shapeType, recordBBox, numParts, numPoints, parts, points)