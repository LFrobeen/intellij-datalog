package com.lfrobeen.datalog.lang.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.impl.source.tree.PsiCoreCommentImpl
import com.intellij.psi.tree.IElementType

// TODO: replace this with proper comment lexing
class DatalogDeclarationComment(type: IElementType, text: CharSequence) : PsiCoreCommentImpl(type, text),
    PsiNameIdentifierOwner {

    override fun getNameIdentifier(): PsiElement? = this

    override fun setName(name: String): PsiElement = this

    override fun getName(): String? {
        return text.replace("//>", "").trim()
    }

    override fun getTextRange(): TextRange {
        return super.getTextRange()
    }
}