package com.lfrobeen.datalog.lang.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class DatalogElementManipulator : AbstractElementManipulator<DatalogReferenceMixin>() {
    override fun handleContentChange(
        psi: DatalogReferenceMixin,
        range: TextRange,
        newContent: String
    ): DatalogReferenceMixin? {
        val oldText = psi.text
        val newText = oldText.substring(0, range.startOffset) + newContent + oldText.substring(range.endOffset)
        psi.getIdentifier().replace(DatalogPsiFactory(psi.project).createIdentifier(newText))
        return psi
    }
}