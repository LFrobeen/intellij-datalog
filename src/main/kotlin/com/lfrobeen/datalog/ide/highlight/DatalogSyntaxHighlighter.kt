package com.lfrobeen.datalog.ide.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.lfrobeen.datalog.ide.colors.DatalogColors
import com.lfrobeen.datalog.lang.lexer.DatalogHighlightingLexer
import com.lfrobeen.datalog.lang.psi.DatalogTypes.*


class DatalogSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey?> =
        when (tokenType) {
            TokenType.BAD_CHARACTER -> pack(BAD_CHARACTER)
            else -> pack(map(tokenType)?.textAttributesKey)
        }

    companion object {
        private val BAD_CHARACTER =
            createTextAttributesKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        fun map(tokenType: IElementType?) = when (tokenType) {
            COMMENT -> DatalogColors.COMMENT
            STRING -> DatalogColors.STRING

            NUMBER_BIN, NUMBER_HEX, NUMBER_DEC ->
                DatalogColors.NUMBER

            TRUE, FALSE ->
                DatalogColors.NUMBER

            // IDENTIFIER -> DatalogColors.IDENTIFIER

            DEFINE_DIRECTIVE, INCLUDE_DIRECTIVE,
            IFDEF_DIRECTIVE, IFNDEF_DIRECTIVE, ENDIF_DIRECTIVE ->
                DatalogColors.PREPROCESSOR


            FLOAT_TYPE, NUMBER_TYPE, UNSIGNED_TYPE, SYMBOL_TYPE ->
                DatalogColors.TYPE_PRIMITIVE

            INPUT_DIRECTIVE, OUTPUT_DIRECTIVE, RELATION_DIRECTIVE,
            TYPE_DIRECTIVE, TYPE_NUM_DIRECTIVE, TYPE_SYM_DIRECTIVE,
            COMP_DIRECTIVE, INIT_DIRECTIVE, FUNCTOR_DIRECTIVE ->
                DatalogColors.KEYWORD

            PRAGMA_DIRECTIVE ->
                DatalogColors.KEYWORD

            LNOT, LAND, LOR,
            BNOT, BAND, BOR, BXOR ->
                DatalogColors.KEYWORD

            AS ->
                DatalogColors.KEYWORD

            SEMICOLON, IF -> DatalogColors.KEYWORD

            COUNT, MEAN, MAX, MIN, SUM ->
                DatalogColors.KEYWORD_AGGREGATION

            else -> null
        }

    }

//        return when (tokenType) {
//            StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN -> VALID_STRING_ESCAPE_KEYS
//            StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN -> INVALID_STRING_ESCAPE_KEYS
//        }
//    }

    override fun getHighlightingLexer(): Lexer = DatalogHighlightingLexer()
}
