package com.lfrobeen.datalog.ide.colors

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor

enum class DatalogColors(humanName: String, default: TextAttributesKey? = null) {
    PREPROCESSOR("Preprocessor Directives", DefaultLanguageHighlighterColors.KEYWORD),

    VARIABLE("Variables//Variable", DefaultLanguageHighlighterColors.IDENTIFIER),
    INSTANCE("Variables//Component Instance", DefaultLanguageHighlighterColors.IDENTIFIER),

    COMPONENT("Program Elements//Component", DefaultLanguageHighlighterColors.CLASS_NAME),
    RELATION("Program Elements//Relation", DefaultLanguageHighlighterColors.IDENTIFIER),
    FUNCTOR("Program Elements//Functor", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION),
    MACRO("Program Elements//Macro", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION),

    KEYWORD("Keywords//Keyword", DefaultLanguageHighlighterColors.KEYWORD),
    KEYWORD_AGGREGATION("Keywords//Aggregator", DefaultLanguageHighlighterColors.KEYWORD),

    NUMBER("Literals//Number", DefaultLanguageHighlighterColors.NUMBER),
    STRING("Literals//String", DefaultLanguageHighlighterColors.STRING),

    TYPE_PRIMITIVE("Types//Primitive", DefaultLanguageHighlighterColors.IDENTIFIER),
    TYPE_DERIVED("Types//Derived", DefaultLanguageHighlighterColors.IDENTIFIER),

    COMMENT("Comments//Comment", DefaultLanguageHighlighterColors.LINE_COMMENT),
    DOC_COMMENT("Comments//Documentation", DefaultLanguageHighlighterColors.DOC_COMMENT),

    ;

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("datalog.$name", default)
    val attributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
}

