package com.lfrobeen.datalog.lang

import com.intellij.core.CoreASTFactory
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.tree.IElementType
import com.lfrobeen.datalog.lang.psi.DatalogDeclarationComment

class DatalogASTFactory : CoreASTFactory() {
    override fun createComment(type: IElementType, text: CharSequence): LeafElement {
        if (text.startsWith("//>"))
            return DatalogDeclarationComment(type, text)

        return super.createComment(type, text)
    }
}
