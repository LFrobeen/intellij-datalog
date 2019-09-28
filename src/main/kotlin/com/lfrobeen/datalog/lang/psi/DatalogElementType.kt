package com.lfrobeen.datalog.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IReparseableElementType
import com.lfrobeen.datalog.lang.DatalogLanguage

class DatalogElementType(debugName: String) : IElementType(debugName, DatalogLanguage) {

    companion object {

        @JvmField
        val EXT_COMMENT = object : IReparseableElementType("EXT_COMMENT", DatalogLanguage) {
            override fun isParsable(buffer: CharSequence, fileLanguage: Language, project: Project): Boolean {
                return false
            }
        }


        @JvmField
        val DOC_COMMENT = object : IReparseableElementType("DOC_COMMENT", DatalogLanguage) {
            override fun isParsable(buffer: CharSequence, fileLanguage: Language, project: Project): Boolean {
                return false
            }
        }
    }

}
