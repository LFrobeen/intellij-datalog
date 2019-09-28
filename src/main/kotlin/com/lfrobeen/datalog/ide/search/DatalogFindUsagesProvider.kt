package com.lfrobeen.datalog.ide.search

import com.intellij.lang.cacheBuilder.*
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.*
import com.intellij.psi.tree.TokenSet
import com.lfrobeen.datalog.lang.lexer.DatalogLexer
import com.lfrobeen.datalog.lang.psi.DatalogDeclarationMixin
import com.lfrobeen.datalog.lang.psi.DatalogRelDecl
import com.lfrobeen.datalog.lang.psi.DatalogTypes

class DatalogFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner? {
        return DefaultWordsScanner(
            DatalogLexer(),
            TokenSet.create(DatalogTypes.IDENTIFIER),
            TokenSet.create(DatalogTypes.COMMENT),
            TokenSet.create(DatalogTypes.STRING)
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is PsiNamedElement
    }

    override fun getType(element: PsiElement): String {
        if (element is DatalogRelDecl) {
            return "Relation"
        }

        return ""
    }


    override fun getDescriptiveName(element: PsiElement): String {
        if (element is DatalogDeclarationMixin) {
            return element.name
        }

        return ""
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        if (element is DatalogDeclarationMixin) {
            return element.name
        }

        return ""
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

}
