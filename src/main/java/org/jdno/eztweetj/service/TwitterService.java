package org.jdno.eztweetj.service;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdno.eztweetj.entity.Profile;
import org.jdno.eztweetj.exception.EzTweetAppException;
import org.jdno.eztweetj.util.Consts;
import org.jdno.eztweetj.util.LogWrapper;
import org.jdno.eztweetj.xml.Tweet;
import org.jdno.eztweetj.xml.TweetList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Twitterに対するアクセスを提供するServiceクラス
 * @author rheud
 *
 */
@Service
public class TwitterService {

    /** Log4J 2 ロガー */
    private static final Logger logger = LogManager.getLogger(TwitterService.class);

    /** Log4J 2 ロガー(冗長出力無し) */
    private static final Logger simpleLogger = LogManager.getLogger(Consts.SIMPLE_LOGGER_NAME);

    /** ログ出力ラッピングクラス */
    @Autowired
    protected LogWrapper logWrapper;

    /** Twitterアクセスクラス */
    @Autowired
    protected Twitter twitter;

    /**
     * アプリ連携の認証を実施する。
     * @param profile プロファイル情報
     * @return 認証結果。アプリ連携に成功した場合はtrue、失敗した場合はfalse。
     */
    public boolean authorize(Profile profile) {
        RequestToken requestToken = null;
        AccessToken accessToken = null;
        boolean isSuccess = false;

        try {
            // Twiiterにアクセスし、アプリ連携を実行するためのURLを取得する
            requestToken = twitter.getOAuthRequestToken();

            // 標準入力からアプリ連携で得たPINコードを入力させる
            logWrapper.log(simpleLogger, "MSGI300001", requestToken.getAuthorizationURL());
            logWrapper.log(simpleLogger, "MSGI300002");

            System.out.print("PIN:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String pin = br.readLine();

            try {
                // PINコードが入力された場合はアクセストークンをTwitterから取得する
                if (!StringUtils.isEmpty(pin)) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                }

                // 正常にアクセストークンを取得できた場合はプロファイルに格納
                if (accessToken != null) {
                    profile.userToken = accessToken.getToken();
                    profile.userSecret = accessToken.getTokenSecret();
                    isSuccess = true;
                }

            } catch (TwitterException te) {
                logWrapper.log(logger, "MSGE100006", te.getStatusCode(), te.getErrorCode(), te.getErrorMessage());
            }
        } catch (Exception e) {
            logWrapper.log(logger, "MSGE100005", e);
            throw new EzTweetAppException(e);
        }

        return isSuccess;
    }

    /**
     * ツイートを実行する
     * @param profile プロファイル情報
     * @param tweetList ツイート情報
     */
    public void tweet(Profile profile, TweetList tweetList) {

        // プロファイルのユーザートークンをセット
        AccessToken accessToken = new AccessToken(profile.userToken, profile.userSecret);
        twitter.setOAuthAccessToken(accessToken);

        // ツイート情報内のツイート件数分、ツイートを実行する
        Status tweetStatus = null;
        ArrayLongList mediaIdList = new ArrayLongList(Consts.UPLOAD_MEDIA_MAX_COUNT);
        for (Tweet tweet : tweetList.getTweet()) {
            // アップロード対象のメディアファイルが存在する場合はアップロードを実行する
            mediaIdList.clear();
            if (!CollectionUtils.isEmpty(tweet.getMediaPath())) {
                for (String mediaFilePath : tweet.getMediaPath()) {
                    mediaIdList.add(uploadMedia(mediaFilePath));
                }
            }

            // Twitterに渡すツイート内容の作成
            StatusUpdate statusUpdate = new StatusUpdate(tweet.getText());
            if (mediaIdList.size() > 0) {
                statusUpdate.setMediaIds(mediaIdList.toArray(new long[mediaIdList.size()]));
            }

            // ツイート実行
            try {
                tweetStatus = twitter.updateStatus(statusUpdate);
                logWrapper.log(logger, "MSGI100003", Long.toString(tweetStatus.getId()),
                        DateFormatUtils.format(tweetStatus.getCreatedAt() , Consts.DATETIME_DISP_FORMAT));
            } catch (TwitterException te) {
                logWrapper.log(logger, "MSGE100007", te.getStatusCode(), te.getErrorCode(), te.getErrorMessage());
                throw new EzTweetAppException(te);
            }
        }
    }

    /**
     * メディアファイルのアップロードを実行する
     * @param filePath メディアファイルのパス
     * @return Twitterから取得したアップロード済みのメディアID
     */
    private long uploadMedia(String filePath) {

        // メディアファイルをTwitterにアップロードする
        UploadedMedia media = null;
        Path mediaFilePath = FileSystems.getDefault().getPath(filePath);

        try {
            media = twitter.uploadMedia(mediaFilePath.toFile());
        } catch (TwitterException te) {
            logWrapper.log(logger, "MSGE100008", te.getStatusCode(), te.getErrorCode(), te.getErrorMessage());
            logger.error("",
                    te.getStatusCode(), te.getErrorCode(), te.getErrorMessage());
            throw new EzTweetAppException(te);
        }

        return media.getMediaId();
    }
}
