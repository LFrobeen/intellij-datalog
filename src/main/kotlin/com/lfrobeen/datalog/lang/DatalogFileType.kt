package com.lfrobeen.datalog.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.vfs.VirtualFile
import com.lfrobeen.datalog.ide.icons.DatalogIcons

import javax.swing.*

object DatalogFileType : LanguageFileType(DatalogLanguage) {

    override fun getName(): String = "Datalog"

    override fun getDescription(): String = "Datalog Files"

    override fun getDefaultExtension(): String = "dl"

    override fun getIcon(): Icon = DatalogIcons.FILE

    override fun getCharset(file: VirtualFile, content: ByteArray): String = "UTF-8"

}
