package com.lfrobeen.datalog

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import com.lfrobeen.datalog.lang.psi.*


class DatalogReference(element: DatalogReferenceMixin, textRange: TextRange? = null) :
    PsiReferenceBase<DatalogReferenceMixin>(element, textRange ?: element.textRangeInParent), ResolvingHint {

    companion object {
        private val RESOLVER = ResolveCache.AbstractResolver { ref: DatalogReference, _: Boolean -> ref.resolveInner() }
    }

    override fun canResolveTo(elementClass: Class<out PsiElement>?): Boolean {
        return when (elementClass) {
            is DatalogDeclarationMixin -> true
            is DatalogVarDeclarationBase -> true
            else -> false
        }
    }

    override fun resolve(): PsiElement? {
        return ResolveCache.getInstance(element.project).resolveWithCaching(this, RESOLVER, false, false)
    }

    private fun resolveInner(): PsiNameIdentifierOwner? {
        val qualifier = element.parent as? DatalogReferenceQualifierMixin
        val qualifierInst = qualifier?.baseRef().takeIf { it != element }?.reference?.resolve() as? DatalogCompInstDecl
        val qualifierDecl = qualifierInst?.componentTyped?.anyRef?.reference?.resolve()

        val processor = DatalogResolvingScopeProcessor(this)

        PsiTreeUtil.treeWalkUp(
            processor,
            qualifierDecl ?: element,
            null,
            ResolveState.initial()
        )

        return processor.declaration
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        when (element) {
            is DatalogDeclarationMixin ->
                if (element.name != this.element.text)
                    return false

            is DatalogDeclarationComment ->
                if (element.name != this.element.text)
                    return false

            else -> return false
        }

        return super.isReferenceTo(element)
    }

}
