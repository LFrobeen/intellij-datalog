package com.lfrobeen.datalog.lang.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.impl.source.tree.PsiCommentImpl
import com.intellij.psi.tree.IElementType
import com.lfrobeen.datalog.ide.icons.DatalogIcons
import javax.swing.Icon

// TODO: replace this with proper comment lexing
class DatalogDeclarationComment(type: IElementType, text: CharSequence) : PsiCommentImpl(type, text),
    PsiNameIdentifierOwner {

    override fun getNameIdentifier(): PsiElement? = this

    override fun setName(name: String): PsiElement = this

    override fun getName(): String? {
        return text.replace("//>", "").trim()
    }

    override fun getTextRange(): TextRange {
        return super.getTextRange()
    }

    override fun getPresentation(): ItemPresentation? {
        return object : ItemPresentation {
            override fun getLocationString(): String? = containingFile.name

            override fun getIcon(unused: Boolean): Icon? = DatalogIcons.COMMENT

            override fun getPresentableText(): String? = name
        }
    }
}