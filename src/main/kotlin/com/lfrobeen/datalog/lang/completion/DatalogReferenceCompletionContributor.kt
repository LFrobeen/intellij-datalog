package com.lfrobeen.datalog.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import com.lfrobeen.datalog.lang.DatalogLanguage
import com.lfrobeen.datalog.lang.psi.DatalogCompInstDecl
import com.lfrobeen.datalog.lang.psi.DatalogQualifiedReferenceMixin

class DatalogReferenceCompletionContributor : CompletionContributor() {

    companion object {
        val file = PlatformPatterns.psiElement().withLanguage(DatalogLanguage)
    }

    init {
        extend(CompletionType.BASIC,
            file,
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position

                    val qualifier = position.parentOfType<DatalogQualifiedReferenceMixin>()?.qualifierRef()
                    val qualifierInst = qualifier?.reference?.resolve() as? DatalogCompInstDecl
                    val qualifierDecl = qualifierInst?.componentTyped?.anyRef?.reference?.resolve()

                    val processor = DatalogCompletionScopeProcessor(result)

                    PsiTreeUtil.treeWalkUp(
                        processor,
                        qualifierDecl ?: position,
                        qualifierDecl,
                        ResolveState.initial()
                    )
                }
            })
    }
}
