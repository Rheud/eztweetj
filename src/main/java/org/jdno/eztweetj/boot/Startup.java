package org.jdno.eztweetj.boot;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdno.eztweetj.config.ApplicationConfig;
import org.jdno.eztweetj.dto.BootArg;
import org.jdno.eztweetj.exception.EzTweetAppException;
import org.jdno.eztweetj.logic.EzTweetLogic;
import org.jdno.eztweetj.util.ArgParser;
import org.jdno.eztweetj.util.LogWrapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * アプリ起動クラス
 *
 * 処理はパラメータの解析、主ロジックの起動のみ。
 * @author rheud
 *
 */
public class Startup {

    /** 自クラスのインスタンス */
    private static final Startup instance = new Startup();

    /** Log4J 2 ロガー */
    private static final Logger logger = LogManager.getLogger(Startup.class);

    /** Spring framework コンテキスト */
    protected static AbstractApplicationContext applicationContext = null;

    /** ログ出力ラッピングクラス */
    protected static LogWrapper logWrapper = null;

    /**
     * Mainロジック
     * @param args 起動パラメータ
     */
    public static void main(String[] args) {
        // コンテキストの作成
        applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        logWrapper = applicationContext.getBean(LogWrapper.class);

        try {
            instance.exec(args);

        } catch (EzTweetAppException etae) {
            // EzTweetAppExceptionがスローされた場合はスローした元で既にログを出力済みの想定のため、ここでは何もしない
            ;

        } catch (Throwable th) {
            logWrapper.log(logger, "MSGE900001", th);

        } finally {
            // コンテキストのクローズ
            applicationContext.close();
        }
    }

    /**
     * 主ロジックの起動
     * @param args 起動パラメータ
     */
    private void exec(String[] args) {

        // 起動パラメータの解析
        ArgParser parser = (ArgParser)applicationContext.getBean(ArgParser.class);
        BootArg arg = parser.parseArgs(args);

        // 主ロジック実行
        if (arg != null) {
            EzTweetLogic logic = (EzTweetLogic)applicationContext.getBean(EzTweetLogic.class);
            logic.exec(arg);
        }
    }

}
