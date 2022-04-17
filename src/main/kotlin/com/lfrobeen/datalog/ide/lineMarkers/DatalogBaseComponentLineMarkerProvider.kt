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

        val componentIdentifier = element.identifier

        // Usages
        val derivedDeclarations =
            ReferencesSearch.search(element)
                .mapNotNull { t ->
                    t.element
                        .parentOfType<DatalogCompDecl>()
                        .takeIf { it?.componentTyped?.anyRef == t.element }
                }

        if (derivedDeclarations.isNotEmpty()) {
            val lineMarker = NavigationGutterIconBuilder
                .create(AllIcons.Gutter.OverridenMethod)
                .setTargets(derivedDeclarations)
                .setTooltipText("Derived components")
                .createLineMarkerInfo(componentIdentifier)

            result.add(lineMarker)
        }

        val baseDeclaration = element.componentTyped?.anyRef?.reference?.resolve() as? DatalogCompDecl

        if (baseDeclaration != null) {
            val lineMarker = NavigationGutterIconBuilder
                .create(AllIcons.Gutter.OverridingMethod)
                .setTargets(baseDeclaration)
                .setTooltipText("Base component")
                .createLineMarkerInfo(componentIdentifier)


            result.add(lineMarker)
        }

    }
}
