package com.lfrobeen.datalog.lang

import com.intellij.lang.Language

object DatalogLanguage : Language("Datalog") {
    override fun isCaseSensitive(): Boolean = true

    override fun getDisplayName(): String = "Datalog"
}