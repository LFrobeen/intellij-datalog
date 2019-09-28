package com.lfrobeen.datalog.ide.typing

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.lfrobeen.datalog.lang.psi.DatalogTypes.*


class DatalogBraceMatcher : PairedBraceMatcher {
    companion object {
        private val PAIRS = arrayOf(
            BracePair(LBRACE, RBRACE, true),
            BracePair(LPARENTH, RPARENTH, true),
            BracePair(LBRACKET, RBRACKET, true)
        )
    }

    override fun getPairs(): Array<BracePair> {
        return PAIRS
    }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }
}