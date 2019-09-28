package com.lfrobeen.datalog.lang.psi

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

class DatalogResolvingScopeProcessor(private val myReference: PsiReference) : PsiScopeProcessor {
    var declaration: PsiNameIdentifierOwner? = null

    override fun execute(decl: PsiElement, state: ResolveState): Boolean {
        when(decl) {
            is  DatalogDeclarationMixin->
                if (decl.name == myReference.element.text) {
                    declaration = decl
                    return false
                }

            is  DatalogDeclarationComment->
                if (decl.name == myReference.element.text) {
                    declaration = decl
                    return false
                }
        }

        return true
    }

    override fun <T : Any?> getHint(hintKey: Key<T>): T? {
        return null
    }

    override fun handleEvent(event: PsiScopeProcessor.Event, associated: Any?) {
    }
}
