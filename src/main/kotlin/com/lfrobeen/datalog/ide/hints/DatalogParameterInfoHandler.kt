package com.lfrobeen.datalog.ide.hints

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.lang.parameterInfo.*
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import com.lfrobeen.datalog.lang.psi.*

class DatalogParameterInfoHandler : ParameterInfoHandler<DatalogArgumentList, DatalogRelDecl> {
    override fun showParameterInfo(element: DatalogArgumentList, context: CreateParameterInfoContext) {
        context.showHint(element, element.textRange.startOffset, this)
    }

    override fun updateParameterInfo(parameterOwner: DatalogArgumentList, context: UpdateParameterInfoContext) {
    }

    override fun updateUI(p: DatalogRelDecl?, context: ParameterInfoUIContext) {
        if (p == null)
            return

        val sb = StringBuilder()
        var hlStart = -1
        var hlEnd = -1
        val names = p.typedIdentifierList.map { it.variable.identifier.text }
        val types = p.typedIdentifierList.map { it.type.text }

        for (indexed in names.zip(types).withIndex()) {
            val i = indexed.index
            val (name, type) = indexed.value

            if (sb.isNotEmpty()) {
                sb.append(", ")
            }

            if (i == context.currentParameterIndex)
                hlStart = sb.length

            sb.append(name)
                .append(": ")
                .append(type)

            if (i == context.currentParameterIndex)
                hlEnd = sb.length
        }
        val isDisabled = context.currentParameterIndex >= names.size

        context.setupUIComponentPresentation(
            sb.toString(), hlStart, hlEnd, isDisabled, false, false, context.defaultParameterColor
        )
    }

    override fun getParametersForLookup(item: LookupElement?, context: ParameterInfoContext?): Array<Any>? = null

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): DatalogArgumentList? {
        val atom = getAtom(context.file.findElementAt(context.offset))
        val argumentList = atom?.argumentList

        if (argumentList != null) {
            val index = ParameterInfoUtils.getCurrentParameterIndex(
                argumentList.node,
                context.offset,
                DatalogTypes.COMMA
            )
            context.setCurrentParameter(index)
        }

        return atom?.argumentList
    }

    override fun findElementForParameterInfo(context: CreateParameterInfoContext): DatalogArgumentList? {
        val atom = getAtom(context.file.findElementAt(context.offset)) ?: return null

        val declRef = atom.anyReference.reference
        val decl = declRef?.resolve() as? DatalogRelDecl

        context.itemsToShow = arrayOf(decl)

        return atom.argumentList
    }

    private fun getAtom(psiElement: PsiElement?): DatalogAtom? =
        psiElement as? DatalogAtom ?: psiElement?.parentOfType()

    override fun couldShowInLookup(): Boolean = true

}
