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

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.jdno.eztweetj.enums.MessageLevelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * ログ出力ラッピングクラス
 *
 * メッセージIDとパラメータからメッセージ内容を組み立て、適切なログレベルでログを出力する。
 * @author rheud
 *
 */
@Component
public class LogWrapper {

    /** メッセージID フォーマットチェック用 正規表現 */
    protected final static Pattern MSG_ID_REGEX_PATTERN = Pattern.compile("^MSG(I|W|E)\\d{6}$");

    /** メッセージ リソースバンドル */
    @Autowired
    protected ResourceBundleMessageSource messageSource;

    /**
     * ログ出力
     * @param logger Log4J 2のロガー
     * @param msgId メッセージID
     * @param args パラメータ
     */
    public void log(Logger logger, String msgId, Object... args) {
        log(logger, msgId, null, args);
    }

    /**
     * ログ出力
     * @param logger Log4J 2のロガー
     * @param msgId メッセージID
     * @param th 例外クラス
     * @param args パラメータ
     */
    public void log(Logger logger, String msgId, Throwable th, Object... args) {

        // メッセージIDのフォーマットチェック
        Matcher m = MSG_ID_REGEX_PATTERN.matcher(msgId);
        if (!m.find()) {
            this.log(logger, "MSGW900001", msgId);
            return;
        }

        // メッセージ内容の編集を行う
        String message = messageSource.getMessage(msgId, args, Locale.getDefault());


        // メッセージIDからメッセージレベルコードを取得
        String levelCode = m.group(1);
        MessageLevelEnum messageLevel = MessageLevelEnum.getMessageLevelByCode(levelCode);

        // メッセージレベルに応じたログレベルでメッセージを出力する
        if (messageLevel == MessageLevelEnum.INFO) {
            if (th == null) {
                logger.info(message);
            } else {
                logger.info(message, th);
            }

        } else if (messageLevel == MessageLevelEnum.WARN) {
            if (th == null) {
                logger.warn(message);
            } else {
                logger.warn(message, th);
            }

        } else if (messageLevel == MessageLevelEnum.ERROR) {
            if (th == null) {
                logger.error(message);
            } else {
                logger.error(message, th);
            }

        }
    }

}
