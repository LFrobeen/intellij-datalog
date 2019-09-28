package com.lfrobeen.datalog.ide.run

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.DefaultProgramRunner

class DatalogProgramRunner : DefaultProgramRunner() {
    override fun canRun(executorId: String, profile: RunProfile): Boolean = DefaultRunExecutor.EXECUTOR_ID == executorId

    override fun getRunnerId(): String = DATALOG_PROGRAM_RUNNER_ID

    companion object {
        const val DATALOG_PROGRAM_RUNNER_ID = "DatalogProgramRunner"
    }
}