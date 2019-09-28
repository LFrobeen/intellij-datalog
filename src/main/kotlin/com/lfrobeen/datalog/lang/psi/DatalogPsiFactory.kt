package com.lfrobeen.datalog.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.lfrobeen.datalog.lang.DatalogLanguage

class DatalogPsiFactory(private val project: Project) {

    fun createFile(text: CharSequence): DatalogPsiFile =
        PsiFileFactory.getInstance(project)
            .createFileFromText(
                "DUMMY.dl",
                DatalogLanguage,
                text,
                false,
                false
            ) as DatalogPsiFile

    fun createIdentifier(text: String): PsiElement =
        createFile(".decl $text()").descendantOfTypeStrict<DatalogRelDecl>()?.identifier
            ?: error("Failed to create identifier: `$text`")

}
