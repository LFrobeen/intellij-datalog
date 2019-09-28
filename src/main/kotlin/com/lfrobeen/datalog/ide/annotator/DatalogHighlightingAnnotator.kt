package com.lfrobeen.datalog.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.lfrobeen.datalog.ide.colors.DatalogColors
import com.lfrobeen.datalog.lang.psi.*

class DatalogHighlightingAnnotator : Annotator {

    private fun styleForDeclType(definition: Any) = when (definition) {
        is DatalogMacroDecl -> DatalogColors.MACRO
        is DatalogFunctorDecl -> DatalogColors.FUNCTOR
        is DatalogTypeDecl -> DatalogColors.TYPE_DERIVED
        is DatalogRelDecl -> DatalogColors.RELATION
        is DatalogCompDecl -> DatalogColors.COMPONENT
        is DatalogCompInstDecl -> DatalogColors.INSTANCE
        is DatalogCompParameter -> DatalogColors.TYPE_DERIVED
        is DatalogDeclarationComment -> DatalogColors.RELATION
        else -> null
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is DatalogReferenceMixin) {
            val decl = element.reference.resolve()
            if (decl != null) {
                val style = styleForDeclType(decl)
                if (style != null) {
                    val annotation = holder.createInfoAnnotation(element.textRange, null)
                    annotation.textAttributes = style.textAttributesKey
                }
            } else {
                holder.createErrorAnnotation(element.textRange, "Declaration not found.")
            }
        }

        if (element is DatalogDeclarationMixin) {
            val style = styleForDeclType(element)
            if (style != null) {
                val annotation = holder.createInfoAnnotation(element.nameIdentifier.textRange, null)
                annotation.textAttributes = style.textAttributesKey
            }
        }

        if (element is DatalogVariable) {
            val annotation = holder.createInfoAnnotation(element.identifier.textRange, null)
            val decl = element.reference?.resolve()

            if (decl is DatalogMacroDecl && styleForDeclType(decl) != null) {
                annotation.textAttributes = styleForDeclType(decl)!!.textAttributesKey
            } else {
                annotation.textAttributes = DatalogColors.VARIABLE.textAttributesKey
            }
        }

        if (element is PsiComment) {
            if (element.text.trim().startsWith("/**")) {
                val annotation = holder.createInfoAnnotation(element.getTextRange(), null)
                annotation.textAttributes = DatalogColors.DOC_COMMENT.textAttributesKey
            }
        }
    }
}