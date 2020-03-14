package mapviewer.gui.domain

import mapviewer.parser.domain.ShpRecord

data class SelectedFeatureParams(
        val featureList: MutableList<MutableList<ShpRecord>> = mutableListOf()
) {}