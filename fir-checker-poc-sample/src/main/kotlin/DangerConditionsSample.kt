package jp.itisnomatter.firchecker.sample

sealed interface Animal
class Dog : Animal
class Cat : Animal

fun describe(animal: Animal): String {
    // 宣言型 Animal がキャスト先 Animal を満たすアップキャスト(実質同一型)。失敗しようがないため安全。
    // AsCastChecker によるコンパイルエラーは発生しない想定。
    val asAnimal: Animal = animal as Animal

    if (animal is Dog) {
        // スマートキャストで既に Dog に絞り込まれているため、この as は不要(代替可能)。
        // AsCastChecker によりコンパイルエラーになる想定。
        val dog = animal as Dog
        return "Dog: $dog, $asAnimal"
    }

    // 宣言型・スマートキャストのいずれからも Cat であることは保証されないダウンキャスト。
    // AsCastChecker によりコンパイルエラーになる想定。
    val cat = animal as Cat
    return "Cat: $cat"
}
