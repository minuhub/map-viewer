package mapviewer.parser.domain

abstract class RecordContent(
    open val shapeType: ShapeType,
    open val recordBBox: BBox,
    open val numParts: Int,
    open val numPoints: Int,
    open val parts: MutableList<Int>,
    open val points: MutableList<MutableList<Point>>
)