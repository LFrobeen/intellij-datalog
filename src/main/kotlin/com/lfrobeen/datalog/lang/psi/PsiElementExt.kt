package com.lfrobeen.datalog.lang.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import kotlin.reflect.KClass

val PsiElement.elementType: IElementType
    get() = PsiUtilCore.getElementType(this)

val PsiElement.ancestors: Sequence<PsiElement>
    get() = generateSequence(this) {
        if (it is PsiFile) null else it.parent
    }


fun PsiElement.getChildOfType(type: IElementType?): PsiElement? {
    if (type == null) return null
    val child = firstChild ?: return null
    return child.findSiblingForward(type)
}

private fun PsiElement.findSiblingForward(type: IElementType): PsiElement? {
    // PsiTreeUtil.findSiblingForward is available since 173
    var e: PsiElement? = this
    while (e != null) {
        if (type == e.node.elementType) return e
        e = e.nextSibling
    }
    return null
}


inline fun <reified T : PsiElement> PsiElement.childrenOfType(): MutableCollection<T> = childrenOfType(T::class)

fun <T : PsiElement> PsiElement.childrenOfType(c: KClass<out T>): MutableCollection<T> {
    return PsiTreeUtil.findChildrenOfType(this, c.java)
}

inline fun <reified T : PsiElement> PsiElement.childOfType(): T? = childOfType(T::class)

fun <T : PsiElement> PsiElement.childOfType(c: KClass<out T>): T? {
    return PsiTreeUtil.findChildOfType(this, c.java)
}

inline fun <reified T : PsiElement> PsiElement.descendantOfTypeStrict(): T? =
    PsiTreeUtil.findChildOfType(this, T::class.java, /* strict */ true)