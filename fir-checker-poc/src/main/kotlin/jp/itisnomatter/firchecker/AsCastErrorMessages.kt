package jp.itisnomatter.firchecker

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory

object AsCastErrorMessages : BaseDiagnosticRendererFactory() {
    override val MAP: KtDiagnosticFactoryToRendererMap = KtDiagnosticFactoryToRendererMap("AsCastChecker").apply {
        put(
            AsCastFirErrors.UNSAFE_AS_CAST,
            "Unsafe 'as' cast is prohibited by AsCastChecker. Use 'as?' or a smart cast instead.",
        )
    }
}
