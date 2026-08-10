package jp.itisnomatter.firchecker

enum class AsCastDanger {
    /** 元の宣言型のままキャスト先に代入可能。アップキャストや同一型へのキャストで、失敗しうる余地がない。 */
    SAFE,

    /** スマートキャストにより既にキャスト先を満たしている。明示的な `as` は不要で、スマートキャストに委ねられる。 */
    SMART_CAST_REPLACEABLE,

    /** 宣言型・スマートキャストのいずれからもキャスト先への型関係が保証されない、実行時失敗しうるダウンキャスト。 */
    UNGUARANTEED_DOWNCAST,
}

object AsCastDangerClassifier {
    fun classify(declaredTypeSatisfiesTarget: Boolean, smartCastTypeSatisfiesTarget: Boolean): AsCastDanger = when {
        declaredTypeSatisfiesTarget -> AsCastDanger.SAFE
        smartCastTypeSatisfiesTarget -> AsCastDanger.SMART_CAST_REPLACEABLE
        else -> AsCastDanger.UNGUARANTEED_DOWNCAST
    }
}
