# FIR Checker PoC (#154) / 危険判定ロジック (#155) / Gradle組み込み (#156)

K2コンパイラのFIR拡張APIを使い、危険な`as`キャストを検出してコンパイルエラーにする最小構成のコンパイラプラグインのPoC(#154)と、
「危険」とみなす条件を定義した検出ロジック本体(#155)。
`#153`(FIR Checker導入)の子issue。**`:app`には`firChecker.enabled`(既定`false`)で有効化できる形で組み込み済み(`#156`)。詳細は後述。**

## 構成

- `fir-checker-poc/` … コンパイラプラグイン本体
- `fir-checker-poc-sample/` … プラグインを適用したサンプルモジュール。意図的に危険な`as`キャストを含み、**コンパイルが失敗することが正しい状態**

## 動作確認方法

```
./gradlew :fir-checker-poc-sample:compileKotlin
```

`fir-checker-poc-sample/src/main/kotlin/Sample.kt`の`value as String`(宣言型・スマートキャストいずれからも保証されないダウンキャスト)で以下のエラーが出て失敗する。
`value as? Int`(安全な`as?`)は検出対象外のため、そちらではエラーが出ないことも確認済み。

```
e: .../Sample.kt:5:16 This 'as' downcast is not guaranteed to succeed and may throw ClassCastException at runtime. Use 'as?' or an 'is' check instead.
```

`as`を`as?`に変えれば`BUILD SUCCESSFUL`になることも確認済み。
`DangerConditionsSample.kt`では残り2つの条件(アップキャストは安全/スマートキャストで代替可能)も確認済み(詳細は次節)。

## FIR拡張APIの調査結果

危険な`as`キャストを検出するために必要な最小限のコンポーネントは以下の5つ。

1. **`CompilerPluginRegistrar`** (`org.jetbrains.kotlin.compiler.plugin`)
   コンパイラプラグインのエントリポイント。`supportsK2 = true`をオーバーライドし、`ExtensionStorage.registerExtensions()`内で`FirExtensionRegistrarAdapter.registerExtension(...)`を呼んでFIR拡張を登録する。
   `META-INF/services/org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar`にFQCNを1行書くだけでサービスとして認識される。

2. **`FirExtensionRegistrar`** (`org.jetbrains.kotlin.fir.extensions`)
   `configurePlugin()`内で`+::MyFirAdditionalCheckersExtension`という独特の構文(コンストラクタ参照を`unaryPlus`で登録)でFIR拡張のファクトリを登録する。

3. **`FirAdditionalCheckersExtension`** (`org.jetbrains.kotlin.fir.analysis.extensions`)
   `expressionCheckers`(今回使用。他に`declarationCheckers`, `typeCheckers`がある)に、実際のチェッカー実装をまとめた`ExpressionCheckers`オブジェクトを渡す。

4. **チェッカー本体**
   `as`/`as?`/`is`/`!is`はいずれもFIR上では`FirTypeOperatorCall`という同じノードで表現され、`FirOperation`列挙型(`AS`, `SAFE_AS`, `IS`, `NOT_IS`)で区別される。
   対応する基底クラスは`FirTypeOperatorCallChecker`で、`ExpressionCheckers.typeOperatorCallCheckers`に登録する。
   `#154`のPoCでは`expression.operation == FirOperation.AS`の場合に無条件でエラーを出していた(issueの要求通りの最小構成)。「危険」の判定ロジックへの精緻化は`#155`で行った(後述)。

5. **診断(エラー)の定義**
   `by error0<PsiElement>()`という委譲プロパティでエラーファクトリを定義し(`org.jetbrains.kotlin.diagnostics.error0`)、`BaseDiagnosticRendererFactory`でメッセージ文言を紐付け、`RootDiagnosticRendererFactory.registerFactory(...)`でグローバル登録する。

## 「危険」の判定ロジック (#155)

`#154`のPoCは`as`を無条件でエラーにしていたが、実際にはキャストが失敗しようがない安全なケース(アップキャスト等)まで巻き込んでしまう。
`AsCastChecker`は以下の2つの型情報を`ConeKotlinType`の`isSubtypeOf`(`org.jetbrains.kotlin.fir.types.isSubtypeOf`)で比較し、`AsCastDangerClassifier`(純粋関数、`fir-checker-poc/src/test`でユニットテスト済み)に渡して3値に分類する。

- **宣言型 (`declaredType`)**: スマートキャストを考慮しない、素の型。`argument`が`FirSmartCastExpression`でラップされている場合は`originalExpression.resolvedType`、そうでなければ`argument.resolvedType`をそのまま使う。
- **スマートキャスト型 (`smartCastType`)**: `FirSmartCastExpression.smartcastType`。スマートキャストが効いていない場合は`null`。

| 条件 | 分類 | 意味 |
| --- | --- | --- |
| `declaredType`がキャスト先のサブタイプ(または同一型) | `SAFE` | アップキャスト/同一型キャスト。失敗しようがない |
| `declaredType`は満たさないが`smartCastType`がキャスト先のサブタイプ | `SMART_CAST_REPLACEABLE` | issueの「スマートキャストで代替可能」に対応。`as`を書かずスマートキャストされた値をそのまま使うべき |
| どちらも満たさない | `UNGUARANTEED_DOWNCAST` | issueの「型の関係が保証されないダウンキャストである」に対応。`ClassCastException`の恐れがある実行時失敗しうるダウンキャスト |

`SAFE`以外は`AsCastFirErrors`の対応する診断(`SMART_CAST_REPLACEABLE_AS_CAST` / `UNGUARANTEED_DOWNCAST_AS_CAST`)でコンパイルエラーにする。
`fir-checker-poc-sample/src/main/kotlin/DangerConditionsSample.kt`で3パターンとも`./gradlew :fir-checker-poc-sample:compileKotlin`でローカル確認済み(アップキャストの行だけエラーが出ないことも確認済み)。

### Gradle組み込み(プラグインとしての適用方法)

サブプロジェクト側で以下を書くだけで、Gradleサブプラグイン(`KotlinCompilerPluginSupportPlugin`)を自作しなくてもローカルの生プラグインとして適用できる。

```kotlin
dependencies {
    kotlinCompilerPluginClasspath(project(":fir-checker-poc"))
}
```

### `:app`への組み込み (#156)

上記の仕組みを`:app`に実際に適用した。ただし現時点の`:app`には`#157`(危険な`as`の棚卸し)未対応の危険なキャストが複数残っており、
チェッカーを無条件で有効化するとビルドが壊れる。そのため`gradle.properties`の`firChecker.enabled`(既定`false`)でON/OFFを切り替えられる形にした。

```kotlin
// app/build.gradle.kts
val firCheckerEnabled = (findProperty("firChecker.enabled") as String?)?.toBoolean() ?: false

dependencies {
    if (firCheckerEnabled) {
        kotlinCompilerPluginClasspath(project(":fir-checker-poc"))
    }
    // ...
}
```

動作確認方法:

```
# 既定(無効)。既存コードに影響せず BUILD SUCCESSFUL になることを確認済み
./gradlew :app:compileDebugKotlin

# 有効化。#157 未対応の危険なキャスト6件(RunRoutineViewModel.kt / RoutineEditViewModel.kt)が
# すべて検出されコンパイルエラーになることを確認済み
./gradlew :app:compileDebugKotlin -PfirChecker.enabled=true
```

`#157`で危険なキャストを解消したのち、`firChecker.enabled`の既定値を`true`に切り替える想定。

## つまずいた点

- ルートの`build.gradle.kts`で`org.jetbrains.kotlin.android`だけを`apply false`宣言している状態で、別モジュールから`org.jetbrains.kotlin.jvm`を素朴に適用しようとすると
  `Error resolving plugin ... already on the classpath with an unknown version`で失敗した。
  両者は同じ`kotlin-gradle-plugin`実体を指すマーカーIDが異なるだけなので、ルートの`plugins{}`ブロックに`jetbrains.kotlin.jvm`も`apply false`で追加し、バージョン解決を一本化することで解消した。
- FIR拡張APIは**Unstable**であることが明記されており、Kotlinのバージョンが上がるたびに破壊的変更が入りうる。今回は本プロジェクトが使用しているKotlin 2.1.10のコンパイラソース(タグ`v2.1.10`)を直接参照してAPI形状を確認した。

## 参考にした実装

- [JetBrains/kotlin `docs/fir/fir-plugins.md`](https://github.com/JetBrains/kotlin/blob/master/docs/fir/fir-plugins.md)
- [Kotlin/compiler-plugin-template](https://github.com/Kotlin/compiler-plugin-template) — 公式テンプレート
- [kitakkun/NoCopy-Compiler-Plugin](https://github.com/kitakkun/NoCopy-Compiler-Plugin) — `FirAdditionalCheckersExtension`で特定の呼び出しを禁止する実例(Kotlin 2.0.0時点)。本PoCの構成は主にこれを踏襲した
- [JetBrains/kotlin `FirCastOperatorsChecker.kt`](https://github.com/JetBrains/kotlin/blob/v2.1.10/compiler/fir/checkers/src/org/jetbrains/kotlin/fir/analysis/checkers/expression/FirCastOperatorsChecker.kt) — コンパイラ本体が`UNCHECKED_CAST`等を報告する際に使っている実際の`as`/`is`チェッカー
