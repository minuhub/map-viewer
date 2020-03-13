package mapviewer.parser.domain

data class Shp(
    val shpHeader: ShpHeader,
    val shpRecordList: MutableList<ShpRecord>
)