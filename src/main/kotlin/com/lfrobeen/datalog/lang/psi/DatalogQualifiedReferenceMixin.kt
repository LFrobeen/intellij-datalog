package com.lfrobeen.datalog.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class DatalogQualifiedReferenceMixin(node: ASTNode) : ASTWrapperPsiElement(node), DatalogAnyReference {
    // Qualified references parse as follows
    //  - `instance.member` ==> [[QualifiedRef -> Ref Ref[[
    //  - `instance.instance.member` ==> [[QualifiedRef -> [[QualifiedRef -> Ref Ref]] Ref]]
    fun qualifierRef(): DatalogAnyRef? {
        val ref = anyReference?.children?.filterIsInstance<DatalogAnyRef>()?.lastOrNull()
            ?: children.filterIsInstance<DatalogAnyRef>().first()

        // Make sure we're not returning a qualifier in a [[QualifiedRef -> Ref]] situation
        if (ref === mainRef()) {
            return null
        }

        return ref
    }

    fun mainRef(): DatalogAnyRef {
        return childrenOfType<DatalogAnyRef>().last()
    }
}
