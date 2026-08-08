---
name: gh-pr-creator
description: >
  ユーザーがGitHubでプルリクエスト / PRの作成、オープン、または提出を依頼した際（例：「PR作って」「プルリクエスト作成して」「create a PR」「push and open a PR」）は、このスキルを使用してください。
  ローカルでの変更からPRのオープンまでの全フローを処理します。
  具体的には、git status/diffの確認、現在のブランチのoriginへのプッシュ、コミット履歴とdiffに基づいた標準的な日本語テンプレートでのPRタイトルおよび本文の起草、そしてgh CLIを利用したmain/masterに対するPRの作成を行います。
  ユーザーが「PR頼む」や「これでPR作って」といった短い言葉しか発しなかった場合でも、現在のブランチでの作業内容をGitHubのPRに変換したい意図がある場合は、必ずこのスキルを使用してください。
---

# GitHub PR Creator

現在のブランチの変更を GitHub の Pull Request として作成するスキル。`git push` から `gh pr create` までを一括で行う。

## 前提

- `gh` コマンドがインストール・認証済みであること(`gh auth status` で確認可能)
- 現在のディレクトリが対象の git リポジトリ内であること
- ベースブランチは常に `main` または `master`(リポジトリに存在する方を自動判定)

## ワークフロー

### 1. 状況確認

以下を順に実行し、状況を把握する。

```bash
git rev-parse --abbrev-ref HEAD          # 現在のブランチ名
git status --short                        # 未コミットの変更がないか確認
git branch -r                             # main か master かをここから判定
```

- 現在のブランチが `main`/`master` 自身の場合、作業ブランチではないので、続行前にユーザーに確認する(誤って本流ブランチからPRを作ろうとしていないか)。
- 未コミットの変更(`git status --short` に出力がある)がある場合、コミットするかどうかユーザーに確認する。勝手にコミットしない。
- ベースブランチは `git branch -r` の出力に `origin/main` があれば `main`、なければ `master` を使う。

### 2. 差分・コミット履歴の取得

PRのタイトルと本文を作るために必要な情報を集める。

```bash
git log <base>..HEAD --oneline           # このブランチ固有のコミット一覧
git diff <base>..HEAD --stat              # 変更ファイルの概要
```

コミットメッセージが1つで要点を表しているならタイトルはほぼそのまま使う。複数コミットがある場合は、変更全体の意図を要約した1行タイトルを作る。

### 3. ブランチをpush

```bash
git push -u origin <current-branch>
```

すでにリモートに存在し `-u` 済みの場合は単に `git push` でよい。push が reject される場合(force-push が必要なケースなど)は、ユーザーに確認してから対応する。絶対に確認なしで `--force` を使わない。

### 4. PR本文の作成

`assets/PR_TEMPLATE.md` を読み込み、以下のプレースホルダを埋める。テンプレートの見出し構成は変更しない。

- `{summary}`: このPRが何をするものか、1〜3行で
- `{changes}`: 変更点を箇条書きで(diffのstatとコミットログから抽出)
- `{verification}`: 動作確認した内容。テストコマンドを実行した場合はその結果、していなければユーザーに確認するか「未確認」と明記する。実際に確認していないことを確認済みのように書かない。
- `{notes}`: 関連Issue番号、レビュー時の注意点など。特になければ「特になし」

### 5. PR作成

```bash
gh pr create --title "<タイトル>" --body "<本文>" --base <base-branch> --head <current-branch>
```

- 本文はテンプレート埋め込み後、一時ファイルに書き出して `--body-file` で渡すと改行やクォートの問題を避けられる:
  ```bash
  gh pr create --title "<タイトル>" --body-file /tmp/pr_body.md --base <base-branch>
  ```
- 実行後、`gh pr create` が返すPR URLをそのままユーザーに提示する。

### 6. 作成後

PR URLを提示するだけでよい。ブラウザで開く必要があれば `gh pr view --web` を案内する程度に留め、勝手には開かない。

## 注意事項

- コミット・push・PR作成のいずれの段階でも、内容に迷いがあれば実行前にユーザーに確認する(特に force push、コミットメッセージの作成、ベースブランチの判定に自信がない場合)。
- 動作確認欄は正直に。テストを実行していないなら「未確認」「レビュー時にご確認ください」等、事実に即した記述にする。
- 認証エラー(`gh auth status` が失敗)の場合は `gh auth login` を促す。
