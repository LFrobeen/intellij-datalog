package com.lfrobeen.datalog.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.tree.injected.changesHandler.range

abstract class DatalogIncludeMixin(node: ASTNode) : ASTWrapperPsiElement(node), DatalogInclude {
    private fun getIncludeFileName(): String = string.text.trim('"')

    fun getIncludeFile(): VirtualFile? {
        val includeFileName = getIncludeFileName()
        val sourceFile = containingFile.virtualFile
        val sourceDir = sourceFile.parent

        return sourceDir.findFileByRelativePath(includeFileName)
    }

    override fun getReference(): PsiReferenceBase<DatalogIncludeMixin> {
        return object : PsiReferenceBase<DatalogIncludeMixin>(this, string.textRangeInParent) {
            init {
                rangeInElement = TextRange.from(
                    rangeInElement.startOffset + 1,
                    rangeInElement.length - 2
                )
            }

            override fun resolve(): PsiElement? {
                val includeFile = getIncludeFile()

                return if (includeFile != null)
                    PsiManager.getInstance(project).findFile(includeFile)
                else
                    null
            }

            override fun isReferenceTo(element: PsiElement): Boolean {
                return resolve() == element
            }
        }
    }
}
