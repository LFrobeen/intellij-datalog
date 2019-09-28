package com.lfrobeen.datalog.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.TokenType
import com.intellij.util.ProcessingContext
import com.lfrobeen.datalog.lang.DatalogLanguage
import com.lfrobeen.datalog.lang.psi.DatalogCompDecl
import com.lfrobeen.datalog.lang.psi.DatalogPsiFile
import com.lfrobeen.datalog.lang.psi.DatalogTypes
import com.lfrobeen.datalog.lang.psi.elementType

class DatalogKeywordCompletionContributor : CompletionContributor() {

    companion object {
        val file = psiElement().withLanguage(DatalogLanguage)
    }

    init {
        extend(
            CompletionType.BASIC,
            file,
            provider(
                ".decl",
                ".type",
                ".input",
                ".output",
                ".init",
                "#include",
                "#ifdef",
                "#ifndef",
                "#endif",
                "#define"
            )
        )
    }

    private fun provider(vararg keywords: String): CompletionProvider<CompletionParameters> {
        return object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(
                parameters: CompletionParameters,
                context: ProcessingContext,
                result: CompletionResultSet
            ) {
                var pos = parameters.originalPosition

                while (pos?.elementType ==  DatalogTypes.IDENTIFIER || pos?.elementType == TokenType.WHITE_SPACE)
                    pos = pos?.parent

                if (pos is DatalogPsiFile || pos is DatalogCompDecl) {
                    for (keyword in keywords) {
                        result.addElement(LookupElementBuilder.create(keyword).bold())
                    }
                }
            }
        }
    }
}
