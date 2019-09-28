package com.lfrobeen.datalog.ide.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.lfrobeen.datalog.lang.psi.*
import com.lfrobeen.datalog.lang.psi.impl.DatalogCompDeclImpl
import com.lfrobeen.datalog.lang.psi.impl.DatalogMacroDeclImpl
import com.lfrobeen.datalog.lang.psi.impl.DatalogTypeDeclImpl


class DatalogFoldingBuilder : FoldingBuilderEx() {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val mutableList: MutableList<FoldingDescriptor> = ArrayList()

        for (n in root.childrenOfType<DatalogCompDecl>())
            mutableList += FoldingDescriptor(n, n.textRange)

        for (n in root.childrenOfType<DatalogStatement>())
            if (n.textContains('\n'))
                mutableList += FoldingDescriptor(n, n.textRange)

        for (n in root.childrenOfType<DatalogMacroDecl>())
            mutableList += FoldingDescriptor(n, n.textRange)

        for (n in root.childrenOfType<DatalogTypeDecl>())
            mutableList += FoldingDescriptor(n, n.textRange)

        return mutableList.toTypedArray()
    }

    override fun getPlaceholderText(astNode: ASTNode): String? {
        when (val node = astNode.psi) {
            is DatalogCompDeclImpl ->
                return ".comp ${node.name} { ... }"

            is DatalogTypeDeclImpl ->
                return ".type ${node.name} { ... }"

            is DatalogMacroDeclImpl ->
                return "#define ${node.name} { ... }"

            is DatalogStatement ->
                return node.fact?.atom?.anyReference?.text?.let { "$it(...)." }
                    ?: node.clause?.clauseHead?.atom?.anyReference?.text?.let { "$it(...) :- (...)." }
                    ?: "..."
        }

        return "..."
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false
    }
}

