package com.lfrobeen.datalog.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

interface DatalogPsiElement : PsiElement

abstract class DatalogCompositePsiElement(node: ASTNode) : ASTWrapperPsiElement(node), DatalogPsiElement {
    override fun toString(): String {
        return "${javaClass.simpleName}(${node.elementType})"
    }
}
