package com.lfrobeen.datalog.lang.psi

import com.intellij.psi.tree.IElementType
import com.lfrobeen.datalog.lang.DatalogLanguage

class DatalogTokenType(debugName: String) : IElementType(debugName, DatalogLanguage) {

    override fun toString(): String {
        return "DatalogTokenType. " + super.toString()
    }
}
