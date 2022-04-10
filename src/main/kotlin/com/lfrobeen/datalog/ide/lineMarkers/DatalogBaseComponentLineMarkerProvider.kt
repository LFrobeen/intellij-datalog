package com.lfrobeen.datalog.ide.lineMarkers

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.parentOfType
import com.lfrobeen.datalog.lang.psi.DatalogCompDecl

class DatalogBaseComponentLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        if (element !is DatalogCompDecl)
            return

        val references =
            ReferencesSearch.search(element)
                .mapNotNull { t ->
                    t.element
                        .parentOfType<DatalogCompDecl>()
                        .takeIf { it?.componentTyped?.anyRef == t.element }
                }

        if (references.isEmpty())
            return

        val builder = NavigationGutterIconBuilder
            .create(AllIcons.Gutter.OverridenMethod)
            .setTargets(references)
            .setTooltipText("Derived components")
//            .setNamer { param ->
//                (param as? DatalogClause)?.text
//                    ?.lines()
//                    ?.joinToString(separator = " ") { (it.trim() + " ") }
//                    ?.take(40)
//            }

        result.add(builder.createLineMarkerInfo(element))
    }
}
