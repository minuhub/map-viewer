package mapviewer.parser.domain

data class ShpRecord(
    var recordHeader: RecordHeader,
    var recordContent: RecordContent
)