fun main() {
    val value: Any = "hello"

    // 危険とみなす as キャスト。AsCastChecker によりコンパイルエラーになる想定。
    val text = value as String
    println(text)

    // as? は安全なのでチェッカーの対象外。ここはコンパイルエラーにならない想定。
    val maybeNumber = value as? Int
    println(maybeNumber)
}
