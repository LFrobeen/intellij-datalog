package com.lfrobeen.datalog.lang.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class DatalogVariableManipulator : AbstractElementManipulator<DatalogVariable>() {
    override fun handleContentChange(
        psi: DatalogVariable,
        range: TextRange,
        newContent: String
    ): DatalogVariable? {
        val oldText = psi.text
        val newText = oldText.substring(0, range.startOffset) + newContent + oldText.substring(range.endOffset)
        psi.identifier.replace(DatalogPsiFactory(psi.project).createIdentifier(newText))
        return psi
    }
}