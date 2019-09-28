package com.lfrobeen.datalog.ide.run.configuration

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.lfrobeen.datalog.lang.psi.DatalogPsiFile

class DatalogRunConfigurationProducer :
    RunConfigurationProducer<DatalogRunConfiguration>(DatalogConfigurationType.getInstance()) {

    override fun isConfigurationFromContext(
        configuration: DatalogRunConfiguration?,
        context: ConfigurationContext?
    ): Boolean {
        if (configuration != null && context != null) {
            val datalogFile = fileFromPsiElement(context.location?.psiElement)
            if (datalogFile != null) {
                return configuration.getFilePath() == datalogFile.virtualFile.canonicalPath
            }
        }
        return false
    }

    override fun setupConfigurationFromContext(
        configuration: DatalogRunConfiguration?,
        context: ConfigurationContext?,
        sourceElement: Ref<PsiElement>?
    ): Boolean {
        if (sourceElement != null && configuration != null) {
            val datalogFile = fileFromPsiElement(sourceElement.get())

            if (datalogFile != null) {
                val path = datalogFile.virtualFile.canonicalPath
                if (path != null) {
                    configuration.setFilePath(path)
                    configuration.name = datalogFile.virtualFile.name
                    return true
                }
            }
        }
        return false
    }

    private fun fileFromPsiElement(psiElement: PsiElement?): DatalogPsiFile? {
        if (psiElement is DatalogPsiFile)
            return psiElement

        if (psiElement !is PsiFile) {
            val containingFile = psiElement?.containingFile

            if (containingFile is DatalogPsiFile)
                return containingFile
        }

        return null
    }
}
