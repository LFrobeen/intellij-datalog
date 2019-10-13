package com.lfrobeen.datalog.ide.docs

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup.*
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.parentOfType
import com.lfrobeen.datalog.lang.psi.DatalogDeclarationMixin
import com.lfrobeen.datalog.lang.psi.DatalogProgramElement

//TODO
class DatalogDocumentationProvider : AbstractDocumentationProvider() {

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        val declaration = when (element) {
            is DatalogDeclarationMixin -> element.name
            else -> return null
        }

        return declaration + getLocationString(element)
    }

    private fun getLocationString(element: PsiElement): String {
        val file = element.containingFile
        return if (file != null) " [" + file.name + "]" else ""
    }

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element is DatalogDeclarationMixin) {
            val declaration = element.text

            return DEFINITION_START + declaration + DEFINITION_END +
                    CONTENT_START + StringUtil.escapeXml(getDocString(element)).replace("\n", "<br>") +
                    CONTENT_END
        }

        return null
    }

    private fun getDocString(decl: PsiElement): String? {
        var doc = decl.parentOfType<DatalogProgramElement>()?.prevSibling
        while (doc != null) {
            if (doc is PsiWhiteSpace) {
                doc = doc.getPrevSibling()
            }
            if (doc is PsiComment) {
                val comment = doc.text.trim()

                if (comment.isNotEmpty() && comment.startsWith("/**")) {
                    return comment
                        .removePrefix("/**")
                        .removeSuffix("*/")
                        .split("\n").joinToString("\n") {
                            it.trim().trimStart { t -> t.isWhitespace() || t == '*' }
                        }
                }
                break
            } else {
                break
            }
        }
        return ""
    }

}