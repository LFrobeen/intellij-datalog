package com.lfrobeen.datalog.ide.formatter

import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CommonCodeStyleSettings

data class DatalogFormatterContext constructor(
    val commonSettings: CommonCodeStyleSettings,
    val spacingBuilder: SpacingBuilder)
