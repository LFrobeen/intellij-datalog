package com.lfrobeen.datalog.lang.completion

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.lfrobeen.datalog.lang.psi.DatalogDeclarationComment
import com.lfrobeen.datalog.lang.psi.DatalogDeclarationMixin
import com.lfrobeen.datalog.lang.psi.DatalogTypes
import com.lfrobeen.datalog.lang.psi.elementType

class DatalogCompletionScopeProcessor(private val result: CompletionResultSet) : PsiScopeProcessor {

    override fun execute(decl: PsiElement, state: ResolveState): Boolean {
        if (decl is DatalogDeclarationMixin) {
            result.addElement(
                LookupElementBuilder.create(decl.name)
                    .withPsiElement(decl)
                    .withBoldness(decl.elementType == DatalogTypes.REL_DECL)
                    .withIcon(decl.presentation?.getIcon(false))
                    .withTypeText(decl.elementType.toString())
            )
        }

        if (decl is DatalogDeclarationComment && !decl.name.isNullOrEmpty()) {
            result.addElement(
                LookupElementBuilder.create(decl.name!!)
                    .withPsiElement(decl)
                    .withBoldness(decl.elementType == DatalogTypes.REL_DECL)
                    .withIcon(decl.presentation?.getIcon(false))
                    .withTypeText(decl.elementType.toString())
            )
        }

        return true
    }

    override fun <T : Any?> getHint(hintKey: Key<T>): T? {
        return null
    }

    override fun handleEvent(event: PsiScopeProcessor.Event, associated: Any?) {
    }
}
