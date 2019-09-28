package com.lfrobeen.datalog.ide.run.configuration

import com.intellij.execution.ui.CommonProgramParametersPanel
import com.intellij.execution.ui.MacroComboBoxWithBrowseButton
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.LabeledComponent.create
import com.intellij.ui.RawCommandLineEditor
import java.awt.BorderLayout


class DatalogConfigForm : CommonProgramParametersPanel() {
    private var interpreterOptionsComponent: LabeledComponent<RawCommandLineEditor>? = null
    private var interpreterPathComponent: LabeledComponent<MacroComboBoxWithBrowseButton>? = null
    private var filePathComponent: LabeledComponent<MacroComboBoxWithBrowseButton>? = null

    private fun initOwnComponents() {

        interpreterOptionsComponent = create(RawCommandLineEditor(), "Interpreter options")
        interpreterOptionsComponent!!.labelLocation = BorderLayout.WEST

        val chooseInterpreterDesc = FileChooserDescriptorFactory.createSingleLocalFileDescriptor()
        chooseInterpreterDesc.title = "Choose interpreter..."

        interpreterPathComponent = create(
            MacroComboBoxWithBrowseButton(chooseInterpreterDesc, project),
            "Interpreter path:"
        )
        interpreterPathComponent!!.labelLocation = BorderLayout.WEST

        val chooseScriptDesc = FileChooserDescriptorFactory.createSingleLocalFileDescriptor()
        chooseScriptDesc.title = "Choose datalog program..."

        filePathComponent = create(MacroComboBoxWithBrowseButton(chooseScriptDesc, project), "Program:")
        filePathComponent!!.labelLocation = BorderLayout.WEST
    }

    override fun addComponents() {
        initOwnComponents()

        add(filePathComponent)
        add(interpreterPathComponent)
        add(interpreterOptionsComponent)

        super.addComponents()
    }

    fun resetForm(configuration: DatalogRunConfiguration) {
        interpreterOptionsComponent!!.component.text = configuration.getInterpreterOptions()
        interpreterPathComponent!!.component.text = configuration.getInterpreterPath()
        filePathComponent!!.component.text = configuration.getFilePath()
    }

    fun applyToForm(configuration: DatalogRunConfiguration) {
        configuration.setInterpreterOptions(interpreterOptionsComponent!!.component.text)
        configuration.setInterpreterPath(interpreterPathComponent!!.component.text)
        configuration.setFilePath(filePathComponent!!.component.text)
    }
}