package com.lfrobeen.datalog.ide.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil


@State(name = "DatalogPluginSettings", storages = [(Storage("datalog.xml"))])
class DatalogPluginSettings : PersistentStateComponent<DatalogPluginSettings> {

    var version = "N/A"

    override fun getState() = this

    override fun loadState(state: DatalogPluginSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): DatalogPluginSettings = ServiceManager.getService(DatalogPluginSettings::class.java)
    }
}
