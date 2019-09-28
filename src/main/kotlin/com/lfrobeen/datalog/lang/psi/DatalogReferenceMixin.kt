package com.lfrobeen.datalog.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.lfrobeen.datalog.DatalogReference
import com.lfrobeen.datalog.DatalogVariableReference

abstract class DatalogReferenceMixin(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference {
        val range = TextRange(0, textLength)

        return when (this) {
            is DatalogVariable -> DatalogVariableReference(this, range)
            else -> DatalogReference(this, range)
        }
    }

    override fun getName(): String = text

    abstract fun getIdentifier(): PsiElement
}