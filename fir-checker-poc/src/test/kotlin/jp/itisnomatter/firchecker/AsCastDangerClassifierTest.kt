package jp.itisnomatter.firchecker

import org.junit.Assert.assertEquals
import org.junit.Test

class AsCastDangerClassifierTest {

    @Test
    fun `declared type already satisfies target -- upcast or identity cast is safe`() {
        val result = AsCastDangerClassifier.classify(
            declaredTypeSatisfiesTarget = true,
            smartCastTypeSatisfiesTarget = false,
        )

        assertEquals(AsCastDanger.SAFE, result)
    }

    @Test
    fun `only smart cast type satisfies target -- redundant, replaceable by smart cast`() {
        val result = AsCastDangerClassifier.classify(
            declaredTypeSatisfiesTarget = false,
            smartCastTypeSatisfiesTarget = true,
        )

        assertEquals(AsCastDanger.SMART_CAST_REPLACEABLE, result)
    }

    @Test
    fun `neither declared nor smart cast type satisfies target -- unguaranteed downcast`() {
        val result = AsCastDangerClassifier.classify(
            declaredTypeSatisfiesTarget = false,
            smartCastTypeSatisfiesTarget = false,
        )

        assertEquals(AsCastDanger.UNGUARANTEED_DOWNCAST, result)
    }

    @Test
    fun `declared type satisfying target takes priority over smart cast flag`() {
        val result = AsCastDangerClassifier.classify(
            declaredTypeSatisfiesTarget = true,
            smartCastTypeSatisfiesTarget = true,
        )

        assertEquals(AsCastDanger.SAFE, result)
    }
}
