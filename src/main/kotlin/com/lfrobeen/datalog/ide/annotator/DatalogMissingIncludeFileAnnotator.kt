package com.lfrobeen.datalog.ide.annotator

import com.intellij.codeInsight.daemon.quickFix.CreateFileFix
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.lfrobeen.datalog.lang.psi.DatalogIncludeMixin

// TODO: There seems to be a PsiFileReference that provides similar functionality out of the box
class DatalogMissingIncludeFileAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is DatalogIncludeMixin) {
            if (element.getIncludeFile() == null) {
                val annotation = holder.createErrorAnnotation(element.string.textRange.let {
                    TextRange.from(
                        it.startOffset + 1,
                        it.length - 2
                    )
                }, "File not found")

                val directory = element.containingFile.containingDirectory
                val fileName = element.string.text

                annotation
                    .registerFix(CreateFileFix(fileName.trim('"'), directory, ""))
            }
        }
    }

}