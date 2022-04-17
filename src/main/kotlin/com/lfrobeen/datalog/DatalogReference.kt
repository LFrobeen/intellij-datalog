package com.lfrobeen.datalog

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import com.lfrobeen.datalog.lang.psi.*
import com.lfrobeen.datalog.lang.psi.impl.DatalogCompDeclImpl

class DatalogReference(element: DatalogReferenceMixin, textRange: TextRange? = null) :
    PsiReferenceBase<DatalogReferenceMixin>(element, textRange ?: element.textRangeInParent), ResolvingHint {

    companion object {
        private val RESOLVER = ResolveCache.AbstractResolver { ref: DatalogReference, _: Boolean -> ref.resolveImpl() }
    }

    override fun canResolveTo(elementClass: Class<out PsiElement>?): Boolean {
        return when (elementClass) {
            is DatalogDeclarationMixin -> true
            is DatalogVarDeclarationMixin -> true
            else -> false
        }
    }

    override fun resolve(): PsiElement? {
        return ResolveCache.getInstance(element.project).resolveWithCaching(this, RESOLVER, false, false)
    }

    private fun resolveImpl(): PsiNameIdentifierOwner? {
        val qualifiedReference = element.parent as? DatalogQualifiedReferenceMixin
        val qualifier = qualifiedReference?.qualifierRef()?.takeIf { it != element } // in case of [[QualifiedRef -> Ref Ref ]]

        if (qualifier == null) {
            return resolveByTreeWalkUp(element, null)
        }

        // Qualified reference handling
        val qualifierInst = qualifier.reference?.resolve() as? DatalogCompInstDecl
        val qualifierDecl = qualifierInst?.componentTyped?.anyRef?.reference?.resolve() as? DatalogCompDeclImpl

        if (qualifierDecl == null) {
            return null
        }

        return resolveByTreeWalkUp(qualifierDecl, qualifierDecl)
    }

    private fun resolveByTreeWalkUp(scopeEntrance: PsiElement, scopeUpperLimit: PsiElement?): PsiNameIdentifierOwner? {
        val processor = DatalogResolvingScopeProcessor(this)

        PsiTreeUtil.treeWalkUp(
            processor,
            scopeEntrance,
            scopeUpperLimit,
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
