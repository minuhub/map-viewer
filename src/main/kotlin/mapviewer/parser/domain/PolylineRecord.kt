package mapviewer.parser.domain

data class PolylineRecord(
    override val shapeType: ShapeType,
    override val recordBBox: BBox,
    override val numParts: Int,
    override val numPoints: Int,
    override val parts: MutableList<Int>,
    override val points: MutableList<MutableList<Point>>
) : RecordContent(shapeType, recordBBox, numParts, numPoints, parts, points)