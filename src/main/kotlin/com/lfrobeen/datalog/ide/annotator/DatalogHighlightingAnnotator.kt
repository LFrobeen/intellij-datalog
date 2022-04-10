package com.lfrobeen.datalog.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
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
                    holder
                        .newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .textAttributes(style.textAttributesKey)
                        .range(element)
                        .create()
                }
            } else {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Declaration not found.")
                    .range(element)
                    .create()
            }
        }

        if (element is DatalogDeclarationMixin) {
            val style = styleForDeclType(element)
            if (style != null) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(style.textAttributesKey)
                    .range(element.nameIdentifier)
                    .create()
            }
        }

        if (element is DatalogVariable) {
            val annotationBuilder = holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element.identifier)
            val decl = element.reference?.resolve()

            // TODO: This is supposed to enable highlighting of macro arguments that refer to components or types ect
            if (decl != null && styleForDeclType(decl) != null && element.parentOfType<DatalogMacroInvocation>() != null) {
                annotationBuilder
                    .textAttributes(styleForDeclType(decl)!!.textAttributesKey)
                    .create()
            } else {
                annotationBuilder
                    .textAttributes(DatalogColors.VARIABLE.textAttributesKey)
                    .create()
            }
        }

        if (element is DatalogQualifier) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(DatalogColors.PREPROCESSOR.textAttributesKey)
                .range(element)
                .create()
        }

        if (element is PsiComment) {
            if (element.text.trim().startsWith("/**")) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(DatalogColors.DOC_COMMENT.textAttributesKey)
                    .range(element)
                    .create()
            }
        }
    }
}