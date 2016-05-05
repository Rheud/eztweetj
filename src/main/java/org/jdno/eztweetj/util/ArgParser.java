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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdno.eztweetj.dto.BootArg;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * 起動パラメータ解析クラス
 * @author rheud
 *
 */
@Component
public class ArgParser implements InitializingBean {

    /** Log4J 2 ロガー */
    private static final Logger logger = LogManager.getLogger(ArgParser.class);

    /** ログ出力ラッピングクラス */
    @Autowired
    protected LogWrapper logWrapper;

    /** メッセージ リソースバンドル */
    @Autowired
    protected ResourceBundleMessageSource messageSource;

    /** 起動パラメータ定義情報 */
    protected Options options = null;

    /**
     * 起動パラメータの定義を行う
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        options = new Options();
        options.addOption(
            Option.builder("p").longOpt("profile").hasArg().argName("profile name").required()
                .desc(messageSource.getMessage("MSGI800001", null, Locale.getDefault())).build());
        options.addOption(
            Option.builder("t").longOpt("text").hasArg().argName("tweet text")
                .desc(messageSource.getMessage("MSGI800002", null, Locale.getDefault())).build());
        options.addOption(
            Option.builder("f").longOpt("filePath").hasArg().argName("tweet filepath")
                .desc(messageSource.getMessage("MSGI800003", null, Locale.getDefault())).build());
        options.addOption(
            Option.builder("a").longOpt("auth")
                .desc(messageSource.getMessage("MSGI800004", null, Locale.getDefault())).build());
    }

    /**
     * 起動パラメータの解析を行う
     * @param args 起動パラメータ
     * @return パラメータDTO
     */
    public BootArg parseArgs(String[] args) {
        BootArg arg = new BootArg();

        try {
            // 起動パラメータの解析
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            // 起動パラメータからパラメータDTOへの転記
            arg.profileName = cmd.getOptionValue("p");
            arg.text = cmd.getOptionValue("t");
            arg.filePath = cmd.getOptionValue("f");
            arg.isAuthorize = cmd.hasOption("a");

            // パラメータの相関チェック
            int paramCnt = 0;
            if (!StringUtils.isEmpty(arg.text)) {
                paramCnt++;
            }
            if (!StringUtils.isEmpty(arg.filePath)) {
                paramCnt++;
            }
            if (arg.isAuthorize) {
                paramCnt++;
            }

            if (paramCnt <= 0) {
                // ツイートテキストとツイート内容ファイルパス、アプリ連携認証実行のパラメータが全て未指定の場合はエラー
                logWrapper.log(logger, "MSGE100002");
                printUsage();
                return null;

            } else if (paramCnt > 1) {
                // ツイートテキストとツイート内容ファイルパス、アプリ連携認証実行のパラメータが2個以上指定されている場合もエラー
                logWrapper.log(logger, "MSGE100003");
                printUsage();
                return null;
            }

        } catch (ParseException pe) {
            logWrapper.log(logger, "MSGE100001");
            printUsage();
            return null;
        }

        return arg;
    }

    /**
     * Usage表示
     */
    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        formatter.printHelp("java -jar eztweetj.jar -p <profile name> [-a] [-t <tweet text>] [-f <tweet filepath>]", options);

    }

}
