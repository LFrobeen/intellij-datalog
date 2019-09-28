package com.lfrobeen.datalog.ide.run

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.util.ProgramParametersUtil
import com.lfrobeen.datalog.ide.run.configuration.DatalogRunConfiguration


class DatalogCommandLineState(
    private val runConfiguration: DatalogRunConfiguration,
    environment: ExecutionEnvironment
) : CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val workingDir = findWorkingDir(runConfiguration)
        val cmd = createCommandLine(workingDir, runConfiguration)

        val processHandler = KillableColoredProcessHandler(cmd)
        ProcessTerminatedListener.attach(processHandler, environment.project)

        return processHandler
    }

    private fun createCommandLine(workingDir: String, runConfiguration: DatalogRunConfiguration): GeneralCommandLine {
        val cmd = GeneralCommandLine()
        val interpreter = runConfiguration.getInterpreterPath()!!
        cmd.exePath = interpreter

        if (!runConfiguration.getInterpreterOptions().isNullOrBlank()) {
            cmd.addParameters(runConfiguration.getInterpreterOptions()!!.split(" "))
        }

        cmd.parametersList.addParametersString(runConfiguration.getFilePath())

        cmd.withWorkDirectory(workingDir)
        cmd.withParentEnvironmentType(if (runConfiguration.isPassParentEnvs) GeneralCommandLine.ParentEnvironmentType.CONSOLE else GeneralCommandLine.ParentEnvironmentType.NONE)
        cmd.withEnvironment(runConfiguration.envs)
        return cmd
    }

    private fun findWorkingDir(runConfig: DatalogRunConfiguration): String {
        return ProgramParametersUtil.getWorkingDir(runConfig, runConfig.project, null)
    }
}
