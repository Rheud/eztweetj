package org.jdno.eztweetj.util;

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

/**
 * 定数定義クラス
 *
 * @author rheud
 */
public class Consts {

    /** カレントディレクトリ取得 プロパティ名 */
    public static final String CURRENT_DIR_PROP_NAME = "user.dir";

    /** プロファイル保存 キャッシュファイル名 */
    public static final String PROFILE_CACHE_FILE_NAME = "eztwj-profile.cache";

    /** 冗長出力無し ロガー名 */
    public static final String SIMPLE_LOGGER_NAME = "SimpleConsoleStdOut";

    /** EzTweet/J Consumer Key */
    public static final String APP_CONSUMER_KEY = "qBx7JuSUJYwS2m6ESpg9RQ";

    /** EzTweet/J Consumer Secret */
    public static final String APP_CONSUMER_SECRET = "pb5VoQVYFefjEbnb4HTW1cdjty9dJkHFaT2NlWafA";

    /** EzTweet/J XSDファイル リソースパス  */
    public static final String XSD_FILE_RESOURCE_PATH = "/schema/ezTweetJ.xsd";

    /** アップロードするメディアファイルの最大数 */
    public static final int UPLOAD_MEDIA_MAX_COUNT = 4;

    /** 日時表示用フォーマット */
    public static final String DATETIME_DISP_FORMAT = "yyyy/MM/dd HH:mm:ss";
}
