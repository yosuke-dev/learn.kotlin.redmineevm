# 概要

Repository for kotlin and ktor learning  
Kotlin/ktor学習用リポジトリ  
以前PHP(Laravel)で作成した社内向け進捗管理ツールをバックエンド側のみKotlin/ktorに置き換えてみたものです

## 学習

1. 外部APIとの連携
2. テストコードの作成
3. テスト内でMockを利用

## 作ったもの

* プロジェクト進捗管理における補助ツール
* Redmineに記録したプロジェクトをEVM(Earned Value Management)方式で進捗を返す
* 大まかなツールの内容(流れ)
  1. プロジェクトのチケットとその進捗状況および作業記録を取得
  2. プロジェクトの開始日から終了日(実行中の場合は最終更新日)までの進捗を計算
  3. 計算された進捗結果を返却

## 使い方

* Gradle
* `{ServerAddress}/evm/version/`にリクエストを投げる
  * リクエストBody
    * uri
      * Redmineの対象URL
    * apikey
      * Redmineで発行した自身のApiKey
    * versionId
      * 進捗確認をしたい対象のバージョンID
* 計算された進捗結果がレスポンス(Json)で返ってくる
  * earnedScheduleDate
    * 対象日
    * String
      * "yyyy-MM-dd"
  * budgetAtCompletion
    * 当初予算
    * float
  * plannedValue
    * 計画コスト
    * float
  * actualCost
    * 実績コスト
    * float
  * earnedValue
    * 出来高実績
    * float

##注意
* PHP(Laravel)では、対象プロジェクトの進捗をDBに永続化していましたが、当プロジェクトではAPIをラッピングするのみとしています
* リクエストの度にRedmine側に何度もリクエストを投げますので、以下を注意してください。
  * Redmine側の負荷問題
    * 当ツールへの1リクエストにつき、通常一か月程度のプロジェクトの場合で、Redmine側に約100リクエストが行われます
  * 当ツールのレスポンス時間問題
    * 上記の通り、100リクエストほどの取得が行われるため、単純に30秒ほど待たされます。
* 永続化&利用時間外におけるバッチ導入をすることで解消しますが、上記を対応する予定は現在のところありません。
  * 理由:PMを掛け持ちしていても最大3つ程度で 1回/週*プロジェクト数 なので実装コスパが悪いため。
