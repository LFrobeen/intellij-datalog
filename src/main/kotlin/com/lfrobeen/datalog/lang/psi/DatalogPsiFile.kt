package com.lfrobeen.datalog.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil
import com.lfrobeen.datalog.lang.DatalogFileType
import com.lfrobeen.datalog.lang.DatalogLanguage

import javax.swing.*

class DatalogPsiFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, DatalogLanguage) {

    private fun declarations() = CachedValuesManager.getCachedValue(this) {
        val files = relatedFiles(this)

        val declarations = files.flatMap {
            val declarationSources = it.findChildrenByClass(DatalogProgramElement::class.java)
                .mapNotNull { it.compDecl ?: it.statement?.decl ?: it.preprocessor?.macroDecl }

            val comments = it.childrenOfType<DatalogDeclarationComment>()
            declarationSources+comments
        }

        CachedValueProvider.Result.create(declarations, *files.toTypedArray())
    }

    override fun getFileType(): FileType {
        return DatalogFileType
    }

    override fun toString(): String {
        return "Datalog File"
    }

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        declarations().forEach { c ->
            if (!processor.execute(c, state))
                return false
        }

        return false
    }

    fun getIncludedFiles(): List<VirtualFile> {
        return PsiTreeUtil.findChildrenOfType(this, DatalogIncludeMixin::class.java).mapNotNull { it.getIncludeFile() }
    }

}