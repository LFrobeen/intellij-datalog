package com.lfrobeen.datalog.ide.formatter


import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.lfrobeen.datalog.lang.DatalogLanguage
import com.lfrobeen.datalog.lang.psi.DatalogTypes.*

class DatalogFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        val commonSettings = settings.getCommonSettings(DatalogLanguage)

        val block = DatalogBlock(
            element.node, null, Indent.getNoneIndent(), null,
            DatalogFormatterContext(commonSettings, createSpaceBuilder(settings))
        )

        return FormattingModelProvider
            .createFormattingModelForPsiFile(
                element.containingFile,
                block, settings
            )
    }

    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(settings, DatalogLanguage)
            .between(PROGRAM_ELEMENT, PROGRAM_ELEMENT).lineBreakInCode()
            .after(tokens(LBRACKET, LPARENTH)).none()
            .before(tokens(RBRACKET, RPARENTH)).none()
            .before(tokens(COMMA, SEMICOLON)).none()
            .after(tokens(COMMA, SEMICOLON)).spaceIf(true)
            .around(tokens(IF)).spaceIf(true)
            .around(tokens(PIPE)).spaceIf(true)
            .after(
                tokens(
                    INPUT_DIRECTIVE, OUTPUT_DIRECTIVE,
                    RELATION_DIRECTIVE, TYPE_DIRECTIVE, COMP_DIRECTIVE,
                    TYPE_SYM_DIRECTIVE, TYPE_NUM_DIRECTIVE
                )
            ).spaceIf(true)
            .around(tokens(LBRACE, RBRACE)).spaceIf(true)
            .aroundInside(tokens(COLON), AGGREGATION_EXPR).spaceIf(true)
            .aroundInside(tokens(COLON), COMP_DECL).spaceIf(true)
            .beforeInside(tokens(COLON), TYPED_IDENTIFIER).none()
            .afterInside(tokens(COLON), TYPED_IDENTIFIER).spaceIf(true)
            .around(tokens(LESS, MORE)).none()
            .around(tokens(BIN_OP)).spaceIf(true)
    }

    private fun tokens(vararg tokens: IElementType) = TokenSet.create(*tokens)

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null
}
