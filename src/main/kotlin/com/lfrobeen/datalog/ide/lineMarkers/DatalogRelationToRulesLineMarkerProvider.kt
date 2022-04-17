package com.lfrobeen.datalog.ide.lineMarkers

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.parentOfType
import com.lfrobeen.datalog.lang.psi.DatalogClause
import com.lfrobeen.datalog.lang.psi.DatalogClauseHead
import com.lfrobeen.datalog.lang.psi.DatalogFact
import com.lfrobeen.datalog.lang.psi.impl.DatalogRelDeclImpl

class DatalogRelationToRulesLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        if (element !is DatalogRelDeclImpl)
            return

        val references =
            ReferencesSearch.search(element)
                .mapNotNull {
                    it.element.parentOfType<DatalogClauseHead>() ?: it.element.parentOfType<DatalogFact>()
                }

        if (references.isEmpty())
            return

        // TODO: use appropriate cell renderer
        val builder = NavigationGutterIconBuilder
            .create(AllIcons.Gutter.ImplementedMethod)
            .setTargets(references)
            .setTooltipText("Rules for this relation")
            .setNamer { param ->
                (param as? DatalogClause)?.text
                    ?.lines()
                    ?.joinToString(separator = " ") { (it.trim() + " ") }
                    ?.take(40)
            }

        result.add(builder.createLineMarkerInfo(element.identifier))
    }
}
