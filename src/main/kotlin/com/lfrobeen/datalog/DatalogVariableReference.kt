package com.lfrobeen.datalog

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.lfrobeen.datalog.ide.icons.DatalogIcons
import com.lfrobeen.datalog.lang.psi.*


class DatalogVariableReference(myElement: DatalogVariable, textRange: TextRange? = null) :
    PsiPolyVariantReferenceBase<DatalogVariable>(myElement, textRange ?: myElement.textRangeInParent) {

    class Result(private val element: PsiElement) : ResolveResult {
        override fun isValidResult(): Boolean = true

        override fun getElement(): PsiElement? = element
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        var macro: DatalogMacroDecl? = null

        PsiTreeUtil.treeWalkUp(
            { elem, _ ->
                when {
                    elem !is DatalogMacroDecl -> true
                    elem.identifier.text != element.identifier.text -> true
                    else -> {
                        macro = elem
                        false
                    }
                }

            },
            element,
            null,
            ResolveState.initial()
        )

        val mentions =
            macro?.let { listOf(it) } ?: variablesInClause()
                .filter { it.identifier.text == element.identifier.text }

        return mentions.map { Result(it) }.take(1).toTypedArray()
    }

    override fun getVariants(): Array<Any> {
        val variables = variablesInClause().reversed().associateBy { (it as PsiNamedElement).name }
        val variants = variables.map {
            LookupElementBuilder.create(it.value, it.key!!)
                .withIcon(DatalogIcons.MAIN)
                .withTypeText("VARIABLE") // TODO: infer actual type
                .withItemTextItalic(true)
        }

        return variants.toTypedArray()
    }

    private fun variablesInClause(): List<DatalogVariable> {
        val clause = element.parentOfType<DatalogClause>() ?: return emptyList()

        return (CachedValuesManager.getCachedValue(clause) {
            val declarations = clause.childrenOfType<DatalogVariable>()

            CachedValueProvider.Result.create(declarations, clause)
        }).toList()
    }
}
