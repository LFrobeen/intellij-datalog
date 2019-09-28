package com.lfrobeen.datalog.ide.run.configuration

import com.intellij.execution.CommonProgramRunConfigurationParameters

interface DatalogRunConfigurationsParams : CommonProgramRunConfigurationParameters {
    fun getInterpreterPath(): String?

    fun setInterpreterPath(path: String)

    fun getFilePath(): String?

    fun setFilePath(path: String)

    fun getInterpreterOptions(): String?

    fun setInterpreterOptions(options: String)
}
