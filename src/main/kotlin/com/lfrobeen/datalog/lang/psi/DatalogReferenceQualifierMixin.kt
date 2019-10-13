package com.lfrobeen.datalog.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.lfrobeen.datalog.DatalogReference

abstract class DatalogReferenceQualifierMixin(node: ASTNode) : ASTWrapperPsiElement(node), DatalogAnyReference {
    override fun getReference(): PsiReference =
        DatalogReference(headRef() as DatalogReferenceMixin, headRef()?.textRangeInParent)


    fun baseRef(): DatalogAnyRef? {
        return anyReference?.headRef() ?: children.filterIsInstance<DatalogAnyRef>().first()
    }

    private fun DatalogAnyReference.headRef(): DatalogAnyRef? {
        return children.filterIsInstance<DatalogAnyRef>().lastOrNull()
    }
}
