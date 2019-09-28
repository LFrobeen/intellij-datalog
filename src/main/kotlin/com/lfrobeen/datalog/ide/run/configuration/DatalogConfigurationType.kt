package com.lfrobeen.datalog.ide.run.configuration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.lfrobeen.datalog.ide.icons.DatalogIcons

class DatalogConfigurationType : ConfigurationTypeBase("Datalog Program", "Datalog", null, DatalogIcons.MAIN) {
    init {
        addFactory(object : ConfigurationFactory(this) {
            override fun createTemplateConfiguration(project: Project): RunConfiguration =
                DatalogRunConfiguration(project, this)

            override fun createConfiguration(name: String?, template: RunConfiguration): RunConfiguration {
                val runConfiguration = super.createConfiguration(name, template)
                if (runConfiguration is DatalogRunConfiguration) {
                    /* set default interpreter path */
                    val path = PropertiesComponent.getInstance(runConfiguration.project).getValue(DatalogRunConfiguration.DATALOG_EXECUTABLE)
                    if (path != null) {
                        runConfiguration.setInterpreterPath(path)
                    }
                }
                return runConfiguration
            }
        })
    }

    companion object {
        fun getInstance(): DatalogConfigurationType = ConfigurationTypeUtil.findConfigurationType(
            DatalogConfigurationType::class.java)
    }
}