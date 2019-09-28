package com.lfrobeen.datalog.ide.actions.mover

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.TokenSet
import com.lfrobeen.datalog.lang.psi.DatalogTypes
import com.lfrobeen.datalog.lang.psi.ancestors
import com.lfrobeen.datalog.lang.psi.elementType

class DatalogProgramElementUpDownMover : DatalogLineMover() {
    private val movableItems = TokenSet.create(
        DatalogTypes.PROGRAM_ELEMENT
    )

    override fun findMovableAncestor(psi: PsiElement, endpoint: Companion.RangeEndpoint): PsiElement? =
        psi.ancestors.find { it.elementType in movableItems }

    override fun findTargetElement(sibling: PsiElement, down: Boolean): PsiElement? {
        return sibling
    }

    override fun findTargetWhitespace(sibling: PsiElement, down: Boolean): PsiWhiteSpace? {
        val whitespace = (if (down) sibling.prevSibling else sibling.nextSibling) as? PsiWhiteSpace ?: return null
        // if there is multi-line whitespace between source and target, it should not be jumped over
        return whitespace.takeIf { it.isMultiLine() }
    }

    private fun PsiWhiteSpace.isMultiLine(): Boolean = getLineCount() > 1

    private fun PsiElement.getLineCount(): Int {
        val doc = containingFile?.let { file -> PsiDocumentManager.getInstance(project).getDocument(file) }
        if (doc != null) {
            val spaceRange = textRange ?: TextRange.EMPTY_RANGE

            if (spaceRange.endOffset <= doc.textLength) {
                val startLine = doc.getLineNumber(spaceRange.startOffset)
                val endLine = doc.getLineNumber(spaceRange.endOffset)

                return endLine - startLine
            }
        }

        return (text ?: "").count { it == '\n' } + 1
    }
}
