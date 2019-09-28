package com.lfrobeen.datalog.ide.actions.mover

import com.intellij.codeInsight.editorActions.moveUpDown.LineMover
import com.intellij.codeInsight.editorActions.moveUpDown.LineRange
import com.intellij.codeInsight.editorActions.moveUpDown.StatementUpDownMover
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.lfrobeen.datalog.lang.psi.DatalogPsiFile

abstract class DatalogLineMover : LineMover() {
    override fun checkAvailable(editor: Editor, file: PsiFile, info: MoveInfo, down: Boolean): Boolean {
        if (file !is DatalogPsiFile && super.checkAvailable(editor, file, info, down)) return false
        @Suppress("USELESS_ELVIS") // NotNull annotation is wrong :(
        val originalRange = info.toMove ?: return false
        val psiRange = StatementUpDownMover.getElementRange(editor, file, originalRange) ?: return false
        if (psiRange.first == null || psiRange.second == null) return false

        val firstItem = findMovableAncestor(psiRange.first, RangeEndpoint.START) ?: return false
        val lastItem = findMovableAncestor(psiRange.second, RangeEndpoint.END) ?: return false

        if (!canApply(firstItem, lastItem)) {
            info.toMove2 = null
            return true
        }

        var sibling = StatementUpDownMover.firstNonWhiteElement(
            if (down) lastItem.nextSibling else firstItem.prevSibling,
            down
        )
        if (sibling != null) sibling = fixupSibling(sibling, down)
        // Either reached last sibling, or jumped over multi-line whitespace
        if (sibling == null) {
            info.toMove2 = null
            return true
        }

        val sourceRange = LineRange(firstItem, lastItem)
        info.toMove = sourceRange
        info.toMove.firstElement = firstItem
        info.toMove.lastElement = lastItem

        val whitespace = findTargetWhitespace(sibling, down)

        /** In some cases (e.g. [DatalogProgramElementUpDownMover]) we don't want to jump over whitespaces */
        if (whitespace != null) {
            val nearLine = if (down) sourceRange.endLine else sourceRange.startLine - 1
            info.toMove2 = LineRange(nearLine, nearLine + 1)
            info.toMove2.firstElement = whitespace
        } else {
            val target = findTargetElement(sibling, down)
            if (target != null) {
                info.toMove2 = LineRange(target)
                info.toMove2.firstElement = target
            } else {
                info.toMove2 = null
            }
        }

        return true
    }

    protected abstract fun findMovableAncestor(psi: PsiElement, endpoint: RangeEndpoint): PsiElement?
    protected abstract fun findTargetElement(sibling: PsiElement, down: Boolean): PsiElement?
    protected open fun fixupSibling(sibling: PsiElement, down: Boolean): PsiElement? = sibling
    protected open fun canApply(firstMovableElement: PsiElement, secondMovableElement: PsiElement): Boolean = true
    protected open fun findTargetWhitespace(sibling: PsiElement, down: Boolean): PsiWhiteSpace? = null

    companion object {
        enum class RangeEndpoint {
            START, END
        }

    }
}
