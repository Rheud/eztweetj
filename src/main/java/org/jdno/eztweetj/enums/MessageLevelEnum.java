package org.jdno.eztweetj.enums;

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

import org.apache.commons.lang3.StringUtils;

/**
 * メッセージレベル Enumクラス
 * @author rheud
 */
public enum MessageLevelEnum {
    /** メッセージレベル:INFO */
    INFO("I"),
    /** メッセージレベル:警告 */
    WARN("W"),
    /** メッセージレベル:エラー */
    ERROR("E");

    /** メッセージレベルコード */
    private String levelCode;

    /** コンストラクタ */
    private MessageLevelEnum(String levelCode) {
        this.levelCode = levelCode;
    }

    /**
     * メッセージレベルのコードからメッセージレベルを取得する。
     * @param levelCode メッセージレベルコード
     * @return メッセージレベル
     */
    public static MessageLevelEnum getMessageLevelByCode(String levelCode) {
        for (MessageLevelEnum messageLevel : values()) {
            if (StringUtils.equals(messageLevel.levelCode, levelCode)) {
                return messageLevel;
            }
        }

        return null;
    }

}
