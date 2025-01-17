package de.michelinside.glucodatahandler.common.notifier

import de.michelinside.glucodatahandler.common.R

enum class DataSource(val resId: Int) {
    NONE(R.string.empty_string),
    JUGGLUCO(R.string.source_juggluco),
    XDRIP(R.string.source_xdrip),
    PHONE(R.string.source_phone),
    WEAR(R.string.source_wear),
    LIBREVIEW(R.string.source_libreview),
    NIGHTSCOUT(R.string.source_nightscout);
    
    companion object {
        fun fromResId(id: Int): DataSource {
            DataSource.values().forEach {
                if(it.resId == id) {
                    return it
                }
            }
            return NONE
        }
    }
}