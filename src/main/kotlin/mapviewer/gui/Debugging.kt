//package mapviewer.gui
//
//import javafx.scene.canvas.Canvas
//import javafx.scene.paint.Color
//import mapviewer.gui.domain.MapParams
//import mapviewer.parser.domain.ShpRecord
//
//object Debugging {
//    fun drawBBox(canvas: Canvas, shpRecordList: MutableList<ShpRecord>, mapParams: MapParams) {
//        var gc = canvas.graphicsContext2D
//        var viewBBox = mapParams.viewBBox
//        gc.run {
//            gc.stroke = Color.GREEN
//            gc.lineWidth = 8.0
//            strokePolygon(doubleArrayOf(RenderingUtil.translateX(viewBBox.minX, mapParams), RenderingUtil.translateX(viewBBox.minX, mapParams), RenderingUtil.translateX(viewBBox.maxX, mapParams), RenderingUtil.translateX(viewBBox.maxX, mapParams)),
//                    doubleArrayOf(RenderingUtil.translateY(viewBBox.minY, mapParams), RenderingUtil.translateY(viewBBox.maxY, mapParams), RenderingUtil.translateY(viewBBox.maxY, mapParams), RenderingUtil.translateY(viewBBox.minY, mapParams))
//                    , 4)
//            gc.lineWidth = 1.0
//        }
//        for (shpRecord in shpRecordList) {
//            if (RenderingUtil.isOverlap(shpRecord.recordContent.recordBBox, viewBBox)) {
//                gc.run {
//                    gc.stroke = Color.BLUE
//                    strokePolygon(doubleArrayOf(RenderingUtil.translateX(shpRecord.recordContent.recordBBox.minX, mapParams), RenderingUtil.translateX(shpRecord.recordContent.recordBBox.minX, mapParams), RenderingUtil.translateX(shpRecord.recordContent.recordBBox.maxX, mapParams), RenderingUtil.translateX(shpRecord.recordContent.recordBBox.maxX, mapParams)),
//                            doubleArrayOf(RenderingUtil.translateY(shpRecord.recordContent.recordBBox.minY, mapParams), RenderingUtil.translateY(shpRecord.recordContent.recordBBox.maxY, mapParams), RenderingUtil.translateY(shpRecord.recordContent.recordBBox.maxY, mapParams), RenderingUtil.translateY(shpRecord.recordContent.recordBBox.minY, mapParams))
//                            , 4)
//                    gc.stroke = Color.BLACK
//                }
//            } else {
//                gc.run {
//                    gc.stroke = Color.RED;
//                    strokePolygon(doubleArrayOf(RenderingUtil.translateX(shpRecord.recordContent.recordBBox.minX, mapParams), RenderingUtil.translateX(shpRecord.recordContent.recordBBox.minX, mapParams), RenderingUtil.translateX(shpRecord.recordContent.recordBBox.maxX, mapParams), RenderingUtil.translateX(shpRecord.recordContent.recordBBox.maxX, mapParams)),
//                            doubleArrayOf(RenderingUtil.translateY(shpRecord.recordContent.recordBBox.minY, mapParams), RenderingUtil.translateY(shpRecord.recordContent.recordBBox.maxY, mapParams), RenderingUtil.translateY(shpRecord.recordContent.recordBBox.maxY, mapParams), RenderingUtil.translateY(shpRecord.recordContent.recordBBox.minY, mapParams))
//                            , 4)
//                    gc.stroke = Color.BLACK;
//
//                }
//            }
//        }
//    }
//    fun drawPointBBox(canvas: Canvas, shpRecordList: MutableList<ShpRecord>, mapParams: MapParams) {
//        val gc = canvas.graphicsContext2D
//        val viewBBox = mapParams.viewBBox
//        val rectLength = 15.0
//        for (shpRecord in shpRecordList) {
//            if (RenderingUtil.isOverlap(shpRecord.recordContent.recordBBox, viewBBox)) {
//                gc.stroke = Color.BLUE
//                gc.strokeRect(RenderingUtil.translateX(shpRecord.recordContent.recordBBox.minX, mapParams) - rectLength / 2, RenderingUtil.translateY(shpRecord.recordContent.recordBBox.minY, mapParams) - rectLength / 2, rectLength, rectLength)
//                gc.stroke = Color.BLACK
//            }
//        }
//    }
//}