package com.lfrobeen.datalog.ide.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.lfrobeen.datalog.ide.icons.DatalogIcons
import javax.swing.Icon

class NewDatalogFile : CreateFileFromTemplateAction(
    "New Datalog File",
    "Create a new Datalog File",
    DatalogIcons.FILE
) {
    override fun buildDialog(
        project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder
    ) {
        builder.setTitle("New Datalog File")

        for (template in Templates.values()) {
            builder.addKind(template.kind, template.icon, template.templateName)
        }
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String =
        Templates.byTemplateName[templateName]?.actionName ?: "Create new Datalog File"


    private companion object {
        @Suppress("unused")
        private enum class Templates(
            val templateName: String,
            val actionName: String,
            val kind: String,
            val icon: Icon
        ) {
            FILE("Datalog File", "Create new Datalog File", "Empty File", DatalogIcons.FILE),
            COMP("Datalog Component", "Create new Datalog Component", "Component", DatalogIcons.COMP);

            companion object {
                val byTemplateName = values().associateBy { it.templateName }
            }
        }
    }
}

