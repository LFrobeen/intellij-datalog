package com.lfrobeen.datalog.ide.colors

import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.util.io.StreamUtil
import com.lfrobeen.datalog.ide.highlight.DatalogSyntaxHighlighter
import com.lfrobeen.datalog.ide.icons.DatalogIcons
import java.nio.charset.Charset

class DatalogColorSettingsPage : ColorSettingsPage {
    private val attrs = DatalogColors.values().map { it.attributesDescriptor }.toTypedArray()
    private val tagToTextAttrKey = DatalogColors.values().associateBy({ it.name }, { it.textAttributesKey })
    private val textAttrKeyToTag = DatalogColors.values().associateBy({ it.textAttributesKey }, { it.name })

    private val demoCode by lazy {
        val stream = javaClass.classLoader.getResourceAsStream("datalog/ide/colors/highlighterDemoText.dl")!!
        StreamUtil.convertSeparators(StreamUtil.readText(stream.bufferedReader(Charset.forName("UTF-8"))))

        // TODO: dont use this in production
        // annotateText(text)!!
    }

//    // This method automatically generates the annotated text file
//    private fun annotateText(text: String): String? {
//        val factory = IdeaTestFixtureFactory.getFixtureFactory();
//        val light = factory.createFixtureBuilder("test").fixture;
//        val fixture = factory.createCodeInsightFixture(light);
//
//        val psiFile = DatalogPsiFactory(fixture.project).createFile(text)
//
//        val annotator = DatalogHighlightingAnnotator()
//        val annotationSession = AnnotationSession(psiFile)
//
//        val annotations = psiFile.childrenOfType<PsiElement>().mapNotNull {
//            val holder = AnnotationHolderImpl(annotationSession)
//            annotator.annotate(it, holder)
//            holder.firstOrNull()
//        }
//
//        val annotationTags = annotations.flatMap { a ->
//            val tag = textAttrKeyToTag.getValue(a.textAttributes)
//
//            listOf(
//                Pair(a.endOffset, "|||$tag///"),
//                Pair(a.startOffset, "|||$tag///")
//            )
//        }.sortedBy {
//            it.first
//        }.reversed()
//
//        val sb = StringBuilder(text)
//
//        annotationTags.forEach { (a, t) ->
//            sb.insert(a, t)
//        }
//
//        return sb.toString()
//    }

    override fun getDisplayName() = "Datalog"
    override fun getIcon() = DatalogIcons.MAIN
    override fun getAttributeDescriptors() = attrs
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter() = DatalogSyntaxHighlighter()
    override fun getAdditionalHighlightingTagToDescriptorMap() = tagToTextAttrKey
    override fun getDemoText() = demoCode
}
