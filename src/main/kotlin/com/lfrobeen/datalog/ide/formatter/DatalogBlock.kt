package com.lfrobeen.datalog.ide.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.lfrobeen.datalog.lang.psi.DatalogTypes.*

class DatalogBlock(
    private val myNode: ASTNode,
    private val myAlignment: Alignment?,
    private val myIndent: Indent?,
    private val myWrap: Wrap?,
    private val myContext: DatalogFormatterContext
) : AbstractBlock(myNode, myWrap, myAlignment) {

    override fun getIndent(): Indent? = myIndent

    override fun buildChildren(): List<Block> {
        return node.getChildren(null)
            .filter { !it.isWhitespaceOrEmpty() }
            .map { childNode: ASTNode ->
                DatalogBlock(
                    childNode,
                    Alignment.createAlignment(),
                    computeIndent(childNode),
                    null,
                    myContext
                )
            }
    }

    override fun getChildIndent(): Indent? {
        return Indent.getNoneIndent()
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return myContext.spacingBuilder.getSpacing(this, child1, child2)
    }

    override fun isLeaf(): Boolean = node.firstChildNode == null

    private fun ASTNode?.isWhitespaceOrEmpty() =
        this == null || textLength == 0 || elementType == TokenType.WHITE_SPACE

    private fun computeIndent(child: ASTNode): Indent? {
        val parentType = node.elementType
        val childType = child.elementType

        // val parentPsi = node.psi
        // val childPsi = child.psi

        return when {
            parentType == COMP_DECL && childType == PROGRAM_ELEMENT -> Indent.getNormalIndent(true)
            parentType == COMP_DECL && childType == COMMENT -> Indent.getNormalIndent(true)

            parentType == RECORD_TYPE_DEF && childType == TYPED_IDENTIFIER -> Indent.getNormalIndent(true)

            parentType == PREDICATE_ASSOCIATION && childType == PREDICATE_CONJUNCTION -> Indent.getNormalIndent(true)
            parentType == PREDICATE_ASSOCIATION && childType == PREDICATE_DISJUNCTION -> Indent.getNormalIndent(true)

            parentType == CLAUSE && childType == CLAUSE_BODY -> Indent.getNormalIndent(true)

            parentType == ARGUMENT_LIST -> Indent.getNormalIndent(true)
            parentType == TUPLE_EXPR && childType == ARGUMENT -> Indent.getNormalIndent(true)
            parentType == RECORD_TYPE_DEF && childType == TYPED_IDENTIFIER -> Indent.getNormalIndent(true)

            else -> Indent.getNoneIndent()
        }
    }
}
