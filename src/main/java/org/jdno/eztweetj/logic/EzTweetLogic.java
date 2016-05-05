package org.jdno.eztweetj.logic;

/*
 * #%L
 * EzTweet/J
 * %%
 * Copyright (C) 2016 Yuya Ogawa
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdno.eztweetj.dto.BootArg;
import org.jdno.eztweetj.entity.Profile;
import org.jdno.eztweetj.service.ProfileService;
import org.jdno.eztweetj.service.TweetListService;
import org.jdno.eztweetj.service.TwitterService;
import org.jdno.eztweetj.util.LogWrapper;
import org.jdno.eztweetj.xml.Tweet;
import org.jdno.eztweetj.xml.TweetList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EzTweetLogic {

    /** Log4J 2 ロガー */
    private static final Logger logger = LogManager.getLogger(EzTweetLogic.class);

    /** ログ出力ラッピングクラス */
    @Autowired
    protected LogWrapper logWrapper;

    /** プロファイル Serviceクラス */
    @Autowired
    protected ProfileService profileService;

    /** Twitter API Serviceクラス */
    @Autowired
    protected TwitterService twitterService;

    /** ツイート内容ファイル Serviceクラス */
    @Autowired
    protected TweetListService tweetListService;

    /**
     * EzTweet/Jの主ロジック
     * @param arg 起動パラメータ情報
     */
    public void exec(BootArg arg) {

        // キャッシュからプロファイル情報を取得する
        Profile profile = profileService.get(arg.profileName);

        if ((profile == null) || arg.isAuthorize) {
            // プロファイルが存在しない場合、またはパラメータでアプリ連携認証実行を指定された場合はアプリ連携認証を実行する
            createProfile(arg.profileName);

        } else {
            logWrapper.log(logger, "MSGI100002", profile.name);

            // プロファイルが存在する場合はツイートを実行する
            if (StringUtils.isEmpty(arg.filePath)) {
                // ファイルパスが空の場合はtオプションの内容をツイートする
                tweetByText(profile, arg.text);

            } else {
                // ファイルパスが空ではない場合はfオプションで指定されたファイルの内容をツイートする
                tweetByFile(profile, arg.filePath);

            }
        }
    }

    /**
     * アプリ連携認証を実行し、プロファイルをキャッシュに登録する。
     * @param profileName プロファイル名
     */
    private void createProfile(String profileName) {
        Profile profile = new Profile();
        profile.name = profileName;

        // アプリ連携認証を実行
        if (twitterService.authorize(profile)) {
            // アプリ連携認証に成功した場合はキャッシュにプロファイル情報を登録する
            profileService.put(profile.name, profile);
            logWrapper.log(logger, "MSGI100001", profile.name);

        } else {
            // アプリ連携認証失敗時のログ出力
            logWrapper.log(logger, "MSGE100005");

        }

    }

    /**
     * 起動パラメータで指定されたテキストをツイートする。
     * @param profile プロファイル情報
     * @param text ツイートするテキスト
     */
    private void tweetByText(Profile profile, String text) {

        // ツイート情報クラスの作成
        Tweet tweet = new Tweet();
        tweet.setText(text);
        TweetList tweetList = new TweetList();
        tweetList.getTweet().add(tweet) ;

        // ツイート実行
        twitterService.tweet(profile, tweetList);
    }

    /**
     * 起動パラメータで指定されたファイルを元にツイートする。
     * @param profile
     * @param filePath
     */
    private void tweetByFile(Profile profile, String filePath) {
        // ファイルの存在チェック
        Path xmlFilePath = FileSystems.getDefault().getPath(filePath);
        if (!Files.exists(xmlFilePath)) {
            logWrapper.log(logger, "MSGE100004", filePath);
            return;
        }

        // ツイート内容ファイルの解析を行う
        TweetList tweetList = tweetListService.unmarshal(xmlFilePath);

        // ツイート実行
        twitterService.tweet(profile, tweetList);
    }

}
