package jp.itisnomatter.firchecker

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory

object AsCastErrorMessages : BaseDiagnosticRendererFactory() {
    override val MAP: KtDiagnosticFactoryToRendererMap = KtDiagnosticFactoryToRendererMap("AsCastChecker").apply {
        put(
            AsCastFirErrors.SMART_CAST_REPLACEABLE_AS_CAST,
            "This 'as' cast is redundant: the value is already smart-cast to a compatible type. Remove the cast and rely on the smart cast instead.",
        )
        put(
            AsCastFirErrors.UNGUARANTEED_DOWNCAST_AS_CAST,
            "This 'as' downcast is not guaranteed to succeed and may throw ClassCastException at runtime. Use 'as?' or an 'is' check instead.",
        )
    }
}
