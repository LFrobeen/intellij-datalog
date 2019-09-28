package com.lfrobeen.datalog.ide.run.configuration

import com.intellij.execution.Executor
import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import com.lfrobeen.datalog.ide.run.DatalogCannotRunException
import com.lfrobeen.datalog.ide.run.DatalogCommandLineState
import com.lfrobeen.datalog.ide.run.DatalogSettingsEditor
import org.jdom.Element
import java.lang.Boolean.parseBoolean
import java.util.*

class DatalogRunConfiguration(project: Project, configurationFactory: ConfigurationFactory) :
        LocatableConfigurationBase<RunConfigurationOptions>(project, configurationFactory, "Datalog run configuration"),
    DatalogRunConfigurationsParams {

    private var workingDir: String? = null
    private var programParams: String? = null
    private var interpreterPath: String? = PropertiesComponent.getInstance(project).getValue(DATALOG_EXECUTABLE)
    private var filePath: String? = null
    private var interpreterOptions: String? = null

    /* see AbstractRunConfiguration */
    private val myEnvs = LinkedHashMap<String, String>()
    private var myPassParentEnvs = true

    override fun getInterpreterPath(): String? = interpreterPath

    override fun setInterpreterPath(path: String) {
        if (path.isBlank()) {
            interpreterPath = null
        } else {
            interpreterPath = path
            /* update project level interpreter */
            PropertiesComponent.getInstance(project).setValue(DATALOG_EXECUTABLE, path)
        }
    }

    override fun getFilePath(): String? = filePath

    override fun setFilePath(path: String) {
        filePath = if (path.isBlank()) null else path
    }

    override fun getInterpreterOptions(): String? = interpreterOptions

    override fun setInterpreterOptions(options: String) {
        interpreterOptions = if (options.isBlank()) null else options
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> =
        DatalogSettingsEditor()

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        if (interpreterPath == null) {
            throw DatalogCannotRunException.interpreterNotSetUp()
        }
        if (filePath == null) {
            throw DatalogCannotRunException.fileNotSetUp()
        }
        return DatalogCommandLineState(this, environment)
    }

    override fun getWorkingDirectory(): String? = workingDir

    override fun setWorkingDirectory(dir: String?) {
        workingDir = dir
    }

    override fun setProgramParameters(params: String?) {
        programParams = params
    }

    override fun getProgramParameters(): String? = programParams

    override fun writeExternal(element: Element) {
        super.writeExternal(element)

        JDOMExternalizerUtil.writeField(element, "FILE_PATH", filePath)
        JDOMExternalizerUtil.writeField(element, "INTERPRETER_PATH", interpreterPath)
        JDOMExternalizerUtil.writeField(element, "WORKING_DIRECTORY", workingDirectory)
        JDOMExternalizerUtil.writeField(element, "PARENT_ENVS", isPassParentEnvs.toString())
        JDOMExternalizerUtil.writeField(element, "PARAMETERS", programParameters)
        JDOMExternalizerUtil.writeField(element, "INTERPRETER_OPTIONS", interpreterOptions)
        EnvironmentVariablesComponent.writeExternal(element, myEnvs)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        EnvironmentVariablesComponent.readExternal(element, myEnvs)

        filePath = JDOMExternalizerUtil.readField(element, "FILE_PATH")
        if (filePath.isNullOrBlank()) {
            filePath = null
        }
        interpreterPath = JDOMExternalizerUtil.readField(element, "INTERPRETER_PATH")
        if (interpreterPath.isNullOrBlank()) {
            interpreterPath = null
        }
        interpreterOptions = JDOMExternalizerUtil.readField(element, "INTERPRETER_OPTIONS")
        if (interpreterOptions.isNullOrBlank()) {
            interpreterOptions = null
        }
        workingDirectory = JDOMExternalizerUtil.readField(element, "WORKING_DIRECTORY")
        programParameters = JDOMExternalizerUtil.readField(element, "PARAMETERS")

        val parentEnvValue = JDOMExternalizerUtil.readField(element, "PARENT_ENVS")
        if (parentEnvValue != null) {
            isPassParentEnvs = parseBoolean(parentEnvValue)
        }
    }

    override fun getEnvs(): MutableMap<String, String> = myEnvs

    override fun setEnvs(envs: MutableMap<String, String>) {
        myEnvs.clear()
        myEnvs.putAll(envs)
    }

    override fun isPassParentEnvs(): Boolean = myPassParentEnvs

    override fun setPassParentEnvs(passParentEnvs: Boolean) {
        myPassParentEnvs = passParentEnvs
    }

    companion object {
        const val DATALOG_EXECUTABLE = "DATALOG_EXECUTABLE"
    }
}