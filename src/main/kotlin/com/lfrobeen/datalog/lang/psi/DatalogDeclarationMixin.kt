package com.lfrobeen.datalog.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.KeyWithDefaultValue
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.lfrobeen.datalog.ide.icons.DatalogIcons
import javax.swing.Icon


abstract class DatalogDeclarationMixin(node: ASTNode) : ASTWrapperPsiElement(node), DatalogDeclaration {
    override fun getName(): String {
        return nameIdentifier.text
    }

    override fun getNameIdentifier(): PsiElement = when (this) {
        is DatalogRelDecl -> this.identifier
        is DatalogTypeDecl -> this.identifier
        is DatalogCompDecl -> this.identifier
        is DatalogCompInstDecl -> this.identifier
        is DatalogMacroDecl -> this.identifier
        is DatalogCompParameter -> this.identifier
        is DatalogFunctorDecl -> this.identifier
        else -> error("Unknown declaration type")
    }

    override fun setName(name: String): PsiElement {
        nameIdentifier.replace(DatalogPsiFactory(project).createIdentifier(name))
        return this
    }

    override fun getTextOffset(): Int = nameIdentifier.node?.startOffset ?: super.getTextOffset()

    // TODO: Select displayed icons according to declaration type
    override fun getPresentation(): ItemPresentation? {
        val self = this
        return object : ItemPresentation {
            override fun getLocationString(): String? = containingFile.name

            override fun getIcon(unused: Boolean): Icon? = when (self) {
                is DatalogRelDecl -> DatalogIcons.RELATION
                is DatalogTypeDecl -> DatalogIcons.TYPE
                is DatalogCompDecl -> DatalogIcons.COMP
                is DatalogCompInstDecl -> DatalogIcons.INST
                is DatalogMacroDecl -> DatalogIcons.MACRO
                is DatalogCompParameter -> DatalogIcons.TYPE
                is DatalogFunctorDecl -> DatalogIcons.FUNCTOR
                else -> DatalogIcons.MAIN
            }

            override fun getPresentableText(): String? = name
        }
    }

    override fun getElementIcon(flags: Int): Icon? = DatalogIcons.MAIN
    override fun getBaseIcon(): Icon = DatalogIcons.MAIN
    override fun getIcon(flags: Int): Icon = DatalogIcons.MAIN

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        when (this) {
            is DatalogStatement ->
                this.childrenOfType<DatalogVariable>().forEach { t ->
                    if (!processor.execute(t, state))
                        return false
                }

            is DatalogCompDecl -> {
                if (this.componentTyped?.anyRef == place)
                    return true

                val processedComponentsKey =
                    KeyWithDefaultValue.create("PROCESSED_COMPONENTS", emptySet<PsiElement>().toMutableSet())
                val processedComponents = state[processedComponentsKey]

                if (processedComponents.contains(this))
                    return true

                processedComponents += this

                val baseComponent = this.componentTyped?.anyRef?.reference?.resolve() as? DatalogCompDecl

                if (baseComponent != null) {
                    baseComponent.processDeclarations(processor, state, null, place)
                }

                componentDeclarations().forEach { t ->
                    if (!processor.execute(t, state))
                        return false
                }
            }
        }

        return true
    }

    private fun componentDeclarations() = CachedValuesManager.getCachedValue(this) {
        val declarations = when (this) {
            is DatalogCompDecl -> {
                (this.programElementList
                    .mapNotNull { it.compDecl ?: it.statement?.decl ?: it.preprocessor?.macroDecl } +
                        this.compParameterList)
                    .mapNotNull {
                        when (it) {
                            is DatalogDeclarationMixin -> it
                            else -> null
                        }
                    }
            }
            else -> emptyList()
        }

        CachedValueProvider.Result.create(declarations, this)
    }
}
