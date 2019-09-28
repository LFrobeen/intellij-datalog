package com.lfrobeen.datalog.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.lfrobeen.datalog.DatalogVariableReference
import com.lfrobeen.datalog.ide.icons.DatalogIcons
import javax.swing.Icon

abstract class DatalogVarDeclarationBase(node: ASTNode) : ASTWrapperPsiElement(node),
    DatalogVariable, DatalogDeclaration {

    override fun getNameIdentifier(): PsiElement? = getChildOfType(DatalogTypes.IDENTIFIER)

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement {
        nameIdentifier?.replace(DatalogPsiFactory(project).createIdentifier(name))
        return this
    }

    override fun getTextOffset(): Int = nameIdentifier?.node?.startOffset ?: super.getTextOffset()

    override fun getPresentation(): ItemPresentation {
        val self = this
        return object : ItemPresentation {
            override fun getPresentableText(): String? {
                return self.name
            }

            override fun getLocationString(): String? {
                return self.containingFile.name
            }

            override fun getIcon(unused: Boolean): Icon? {
                return DatalogIcons.MAIN
            }
        }
    }

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        return processor.execute(this, state)
    }

    override fun getReference(): PsiReference {
        val range = TextRange(0, textLength)

        return DatalogVariableReference(this, range)
    }
}
