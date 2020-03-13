package mapviewer.parser.domain

enum class ShapeType(val type: Int) {

    Point(1), PolyLine(3), Polygon(5), MultiPoint(8);

    companion object {
        private val map = values().associateBy(ShapeType::type)
        fun fromType(type: Int) = map[type] ?: error("$type is not defined type")
    }

}