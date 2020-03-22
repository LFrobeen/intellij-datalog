package com.lfrobeen.datalog.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.lfrobeen.datalog.lang.psi.*
import com.lfrobeen.datalog.lang.psi.impl.DatalogRelDeclImpl

class DatalogWarningAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is DatalogVariable && element.reference?.resolve() !is DatalogMacroDecl) {
            val fact = element.parentOfType<DatalogClause>()
            val variables = fact?.childrenOfType<DatalogVariable>() ?: emptyList<DatalogVariable>()

            if (variables.count { it.identifier.text == element.identifier.text } == 1) {
                holder.createWarningAnnotation(element.textRange, "Variable occurs only once in fact or rule.")
            }
        }

        if (element is DatalogAtom) {
            val relation = (element.childOfType<DatalogAnyRef>()?.reference?.resolve()) as? DatalogRelDecl

            if (relation != null) {
                annotateParameter(holder, relation, element)
            }
        }
    }

    private fun annotateParameter(holder: AnnotationHolder, relation: DatalogRelDecl, element: DatalogAtom) {
        val requiredParams = relation.typedIdentifierList
        val argumentList = element.argumentList
        val passedParams = argumentList.argumentList

        if (requiredParams.size == passedParams.size) return

        for (i in 0..requiredParams.size.coerceAtLeast(passedParams.size)) {
            val required = requiredParams.getOrNull(i)
            val passed = passedParams.getOrNull(i)

            if (required != null && passed == null) {
                holder.createErrorAnnotation(
                    argumentList.node.lastChildNode,
                    "Missing parameter ${required.text}"
                )
                break
            }

            if (required == null && passed != null) {
                holder.createErrorAnnotation(
                    passed,
                    "Too many parameters for relation ${(relation as DatalogRelDeclImpl).name}"
                )
            }
        }
    }
}