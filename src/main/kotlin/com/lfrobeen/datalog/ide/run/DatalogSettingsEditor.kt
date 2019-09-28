package com.lfrobeen.datalog.ide.run

import com.intellij.openapi.options.SettingsEditor
import com.lfrobeen.datalog.ide.run.configuration.DatalogConfigForm
import com.lfrobeen.datalog.ide.run.configuration.DatalogRunConfiguration
import javax.swing.JComponent

class DatalogSettingsEditor : SettingsEditor<DatalogRunConfiguration>() {
    private val form = DatalogConfigForm()

    override fun resetEditorFrom(runConfiguration: DatalogRunConfiguration) {
        form.reset(runConfiguration)
        form.resetForm(runConfiguration)
    }

    override fun createEditor(): JComponent = form

    override fun applyEditorTo(runConfiguration: DatalogRunConfiguration) {
        form.applyTo(runConfiguration)
        form.applyToForm(runConfiguration)
    }
}
