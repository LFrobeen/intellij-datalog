package com.lfrobeen.datalog.ide.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.ExecutionManager
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.runners.executeState
import com.intellij.execution.ui.RunContentDescriptor
import org.jetbrains.concurrency.resolvedPromise

class DatalogProgramRunner : ProgramRunner<RunnerSettings> {
    override fun canRun(executorId: String, profile: RunProfile): Boolean = DefaultRunExecutor.EXECUTOR_ID == executorId

    override fun getRunnerId(): String = DATALOG_PROGRAM_RUNNER_ID

    override fun execute(environment: ExecutionEnvironment) {
        val state = environment.state ?: return
        ExecutionManager.getInstance(environment.project).startRunProfile(environment) {
            resolvedPromise(doExecute(state, environment))
        }
    }

    private fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        return executeState(state, environment, this)
    }

    companion object {
        const val DATALOG_PROGRAM_RUNNER_ID = "DatalogProgramRunner"
    }
}