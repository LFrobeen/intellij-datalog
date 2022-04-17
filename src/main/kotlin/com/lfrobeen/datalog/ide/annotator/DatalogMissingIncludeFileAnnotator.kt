package com.lfrobeen.datalog.ide.annotator

import com.intellij.codeInsight.daemon.quickFix.CreateFilePathFix
import com.intellij.codeInsight.daemon.quickFix.NewFileLocation
import com.intellij.codeInsight.daemon.quickFix.TargetDirectory
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.lfrobeen.datalog.lang.psi.DatalogIncludeMixin

// TODO this doesnt really work
// TODO: There seems to be a PsiFileReference that provides similar functionality out of the box
class DatalogMissingIncludeFileAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is DatalogIncludeMixin || element.getIncludeFile() != null) {
            return
        }

        val directory = element.containingFile.containingDirectory
        val fileName = element.string.text

        val textRange = element.string.textRange.let {
            TextRange.from(
                it.startOffset + 1,
                it.length - 2
            )
        }

        holder
            .newAnnotation(HighlightSeverity.ERROR, "File not found")
            .range(textRange)
            .newFix(
                CreateFilePathFix(
                    element,
                    NewFileLocation(listOf(TargetDirectory(directory)), fileName.trim('"'))
                ) { "" }
            )
            .range(textRange)
            .registerFix()
            .create()
    }

}