package com.lfrobeen.datalog.ide.navigation.goto

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.lfrobeen.datalog.lang.psi.DatalogCompDecl
import com.lfrobeen.datalog.lang.psi.datalogFiles
import kotlin.collections.HashSet


class DatalogChooseByNameContributor : ChooseByNameContributor {
    override fun getNames(project: Project?, includeNonProjectItems: Boolean): Array<String> {
        return getItemsByName(null, null, project, includeNonProjectItems)
            .mapNotNull { it.name }
            .toTypedArray()
    }

    override fun getItemsByName(
        name: String?,
        pattern: String?,
        project: Project?,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        if (project == null)
            return emptyArray()

        val result = HashSet<NavigationItem>()
        val processor = PsiScopeProcessor { element, _ ->
            if (element is NavigationItem)
                result.add(element)

            true
        }

        val resolveState = ResolveState.initial()

        datalogFiles(project).forEach {
            it.processDeclarations(processor, resolveState, null, it)
        }

        result.toList().forEach {
            (it as? DatalogCompDecl)?.processDeclarations(processor, resolveState, null, it)
        }

        return result.toTypedArray()
    }

}