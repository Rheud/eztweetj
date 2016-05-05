package org.jdno.eztweetj.exception;

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
 * アプリ内で発生した例外のラッピングクラス
 * @author rheud
 *
 */
public class EzTweetAppException extends RuntimeException {

    /** シリアルバージョンID */
    private static final long serialVersionUID = -7648755131755458360L;

    /**
     * 指定された詳細メッセージを使用して、新規例外を構築します。
     * @param message  詳細メッセージ。詳細メッセージは、あとで Throwable.getMessage() メソッドで取得できるように保存されます。
     */
    public EzTweetAppException(String message) {
        super(message);
    }

    /**
     * 指定された詳細メッセージおよび原因を使用して新しい実行時例外を構築します。
     * @param message  詳細メッセージ。詳細メッセージは、あとで Throwable.getMessage() メソッドで取得できるように保存されます。
     * @param cause 原因 (あとで Throwable.getCause() メソッドで取得できるように保存される)。(null 値が許可されており、原因が存在しないか不明であることを示す)。
     */
    public EzTweetAppException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 指定された詳細メッセージ、原因、抑制の有効化または無効化、書き込み可能スタックトレースの有効化または無効化に基づいて、新しい実行時例外を構築します。
     * @param message  詳細メッセージ。詳細メッセージは、あとで Throwable.getMessage() メソッドで取得できるように保存されます。
     * @param cause 原因 (あとで Throwable.getCause() メソッドで取得できるように保存される)。(null 値が許可されており、原因が存在しないか不明であることを示す)。
     * @param enableSuppression 抑制を有効化するか、それとも無効化するか
     * @param writableStackTrace スタックトレースを書き込み可能にするかどうか
     */
    public EzTweetAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 指定された原因を使用して新しい実行時例外を構築します。
     * @param cause 原因 (あとで Throwable.getCause() メソッドで取得できるように保存される)。(null 値が許可されており、原因が存在しないか不明であることを示す)。
     */
    public EzTweetAppException(Throwable cause) {
        super(cause);
    }
}
