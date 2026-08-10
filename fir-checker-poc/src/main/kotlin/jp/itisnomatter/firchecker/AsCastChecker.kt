package jp.itisnomatter.firchecker

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirTypeOperatorCallChecker
import org.jetbrains.kotlin.fir.expressions.FirOperation
import org.jetbrains.kotlin.fir.expressions.FirSmartCastExpression
import org.jetbrains.kotlin.fir.expressions.FirTypeOperatorCall
import org.jetbrains.kotlin.fir.expressions.argument
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.fir.types.isSubtypeOf
import org.jetbrains.kotlin.fir.types.resolvedType

object AsCastChecker : FirTypeOperatorCallChecker(MppCheckerKind.Common) {
    override fun check(expression: FirTypeOperatorCall, context: CheckerContext, reporter: DiagnosticReporter) {
        if (expression.operation != FirOperation.AS) return

        val session = context.session
        val argument = expression.argument
        val targetType = expression.conversionTypeRef.coneType

        // スマートキャスト適用中は argument が FirSmartCastExpression でラップされる。
        // 素の宣言型(originalExpression)と、スマートキャストで絞り込まれた型を分けて評価する。
        val smartCastArgument = argument as? FirSmartCastExpression
        val declaredType = (smartCastArgument?.originalExpression ?: argument).resolvedType
        val smartCastType = smartCastArgument?.smartcastType?.coneType

        val danger = AsCastDangerClassifier.classify(
            declaredTypeSatisfiesTarget = declaredType.isSubtypeOf(targetType, session),
            smartCastTypeSatisfiesTarget = smartCastType?.isSubtypeOf(targetType, session) ?: false,
        )

        when (danger) {
            AsCastDanger.SAFE -> Unit
            AsCastDanger.SMART_CAST_REPLACEABLE ->
                reporter.reportOn(expression.source, AsCastFirErrors.SMART_CAST_REPLACEABLE_AS_CAST, context)
            AsCastDanger.UNGUARANTEED_DOWNCAST ->
                reporter.reportOn(expression.source, AsCastFirErrors.UNGUARANTEED_DOWNCAST_AS_CAST, context)
        }
    }
}
