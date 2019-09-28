package com.lfrobeen.datalog.ide.component

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.BaseComponent
import com.intellij.openapi.extensions.PluginId
import com.lfrobeen.datalog.ide.settings.DatalogPluginSettings

class DatalogComponent : BaseComponent {

    var updated: Boolean = false

    override fun initComponent() {
        val plugin = PluginManager.getPlugin(PluginId.getId("com.lfrobeen.intellij-datalog"))
        val pluginSettings = DatalogPluginSettings.getInstance()

        val previousVersion = pluginSettings.version
        val currentVersion = plugin?.version

        updated = previousVersion != currentVersion

        if (currentVersion != previousVersion &&
            currentVersion != null
        ) pluginSettings.version = currentVersion
    }

    companion object {
        fun getInstance(): DatalogComponent =
            ApplicationManager.getApplication().getComponent(DatalogComponent::class.java)
    }
}