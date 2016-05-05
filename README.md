# EzTweet/J

![Badge Status](https://ci-as-a-service)

コマンドラインからツイートをするためのツールです。

## Description

EzTweet/JはコマンドラインからツイートをするためにJavaで作成されたツールです。
ツイートするのに最低限必要な機能だけを実装していますので、TLを取得することなどはできません。
単純にツイートをすることしかできません。

## Requirement

JDK 1.7以上

## Usage

1. アプリ連携の認証

 あなたのアカウントでツイートをするためにはEzTweet/Jのアプリ連携を許可する必要があります。
 次のコマンドを実行するとアプリ連携のためのURLが表示され、連携を許可後PINコードを入力することで認証が完了します。

 * Windowsの場合 `eztweetj.bat -p hoge -a`
 * Linux または MacOSXの場合 `./eztweetj.sh -p hoge -a`

 -pオプションで指定するのはプロファイル名ですが、これはアカウント名と一致する必要はありません。
 どのアカウントを使用するのかというエイリアスのようなものです。

2. ツイート

 アプリ連携が許可されたらツイートすることができます。
 ツイートの方法は2種類あります。
 
* オプション指定
  
  コマンドラインのオプションで直接ツイート内容を指定します。

  + Windowsの場合 `eztweetj.bat -p hoge -t ツイートするよ`
  + Linux または MacOSXの場合 `./eztweetj.sh -p hoge -t ツイートするよ`

  -tオプションで指定した内容がそのままツイートされます。

* XMLファイル

  ツイート内容をXMLファイルにまとめて、そのXMLファイルのファイルパスを指定します。
  この方法の場合、連続で複数のツイートをすることや画像付きのツイートをすることができます。

  + Windowsの場合 `eztweetj.bat -p hoge -f tweet.xml`
  + Linux または MacOSXの場合 `./eztweetj.sh -p hoge -f tweet.xml`

  -fオプションでXMLファイルのファイルパスを指定してください。

  XMLファイルの内容については下記のサンプルを参照してください。
  tweetタグが1つのツイートに対応してします。
  textタグがツイートの本文、mediaPathタグがアップロードする画像のファイルパスになります。
  mediaPathは1つのツイートに付き4つまで指定できます。画像をアップロードしない場合は省略可能です。
  ```XML
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<tweetList xmlns="http://jdno.org/eztweetj/xml">
  <tweet>
    <text>これはテストツイートです。</text>
    <mediaPath>src/test/resources/img/01.jpg</mediaPath>
    <mediaPath>src/test/resources/img/02.jpg</mediaPath>
    <mediaPath>src/test/resources/img/03.jpg</mediaPath>
    <mediaPath>src/test/resources/img/04.jpg</mediaPath>
  </tweet>
  <tweet>
    <text>連続ツイートのテストです。</text>
  </tweet>
</tweetList>
```

## Install

下記のURLからzipファイルをダウンロードし、zipファイルを展開してください。
本体のjarファイルと起動のためのスクリプトファイルが格納されています。


## Author

[@Rheud](https://twitter.com/rheud)

## License

[MIT](https://opensource.org/licenses/mit-license.php)
