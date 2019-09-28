package com.lfrobeen.datalog.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.lfrobeen.datalog.lang.DatalogFileType

fun datalogFiles(project: Project): List<DatalogPsiFile> {
    val psiManager = PsiManager.getInstance(project)
    return FileTypeIndex.getFiles(DatalogFileType, GlobalSearchScope.allScope(project))
        .mapNotNull { psiManager.findFile(it) as? DatalogPsiFile }
}

fun relatedFiles(fileIn: DatalogPsiFile): List<DatalogPsiFile> {
    val file = fileIn.originalFile as DatalogPsiFile

    val project = file.project
    val projectFiles = datalogFiles(project)

    val psiManager = PsiManager.getInstance(project)

    // List of pairs (A, B) for which file A includes file B
    val includeRelation = projectFiles
        .flatMap { f ->
            f.getIncludedFiles()
                .mapNotNull { psiManager.findFile(it) as? DatalogPsiFile }
                .map { i -> Pair(f, i) }
        }

    val includedBy = includeRelation.groupBy({ it.first }, { it.second })
    val includes = includeRelation.groupBy({ it.second }, { it.first })

    val relatedFiles = HashSet<DatalogPsiFile>()

    fun traverse(file: DatalogPsiFile, traverseParents: Boolean) {
        if (relatedFiles.contains(file))
            return

        relatedFiles.add(file)

        if (traverseParents)
            for (f in includes.getOrDefault(file, emptySet<DatalogPsiFile>()))
                traverse(f, true)

        for (f in includedBy.getOrDefault(file, emptySet<DatalogPsiFile>()))
            traverse(f, false)

    }

    traverse(file, true)

    return relatedFiles.toList()
}