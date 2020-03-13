package mapviewer.gui.domain

class MapParams(
        var zoomLevel: Int,
        var tileSizeInCanvas: Double,
        var canvasBBox : FunctionalBBox,
        var tileBBox: FunctionalBBox,
        var shpBBox : FunctionalBBox,
        var standardBBox: FunctionalBBox = FunctionalBBox(746075.4379000003,1458754.2844999991,1387947.7235000003,2069365.2097999994)
) {}