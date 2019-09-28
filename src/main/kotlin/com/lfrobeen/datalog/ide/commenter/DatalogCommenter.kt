package com.lfrobeen.datalog.ide.commenter

import com.intellij.lang.Commenter

class DatalogCommenter : Commenter {
    override fun getLineCommentPrefix(): String? = "//"

    override fun getBlockCommentPrefix(): String? = null

    override fun getBlockCommentSuffix(): String? = null

    override fun getCommentedBlockCommentPrefix(): String? = "/*"

    override fun getCommentedBlockCommentSuffix(): String? = "*/"
}