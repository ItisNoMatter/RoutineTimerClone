package jp.itisnomatter.firchecker

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.error0
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory

object AsCastFirErrors {
    val UNSAFE_AS_CAST by error0<PsiElement>()

    init {
        RootDiagnosticRendererFactory.registerFactory(AsCastErrorMessages)
    }
}
