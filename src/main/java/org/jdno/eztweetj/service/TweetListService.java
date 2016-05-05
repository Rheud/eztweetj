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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdno.eztweetj.exception.EzTweetAppException;
import org.jdno.eztweetj.util.Consts;
import org.jdno.eztweetj.util.LogWrapper;
import org.jdno.eztweetj.xml.TweetList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 * ツイート情報XMLファイルに対するアクセスを提供するServiceクラス
 * @author rheud
 *
 */
@Component
public class TweetListService {

    /** Log4J 2 ロガー */
    private static final Logger logger = LogManager.getLogger(TweetListService.class);

    /** ログ出力ラッピングクラス */
    @Autowired
    protected LogWrapper logWrapper;

    /**
     * 指定されたツイート定義ファイルの内容を解析し、ツイート内容リストを作成する。
     * @param xmlFile ツイート定義ファイル
     * @return ツイート内容リスト
     */
    public TweetList unmarshal(Path xmlFilePath) {
        TweetList tweetList = null;

        try (BufferedReader br = Files.newBufferedReader(xmlFilePath, StandardCharsets.UTF_8)) {
            // スキーマ定義の読み込み
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(getClass().getResource(Consts.XSD_FILE_RESOURCE_PATH));

            // Unmarshallerインスタンス生成
            JAXBContext context = JAXBContext.newInstance(TweetList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);

            // 入力されたファイルのUnmarshall実行
            tweetList = (TweetList)unmarshaller.unmarshal(br);

        } catch (SAXException saxe) {
            logWrapper.log(logger, "MSGE100009", saxe);
            throw new EzTweetAppException(saxe);

        } catch (JAXBException jbe) {
            logger.error("", jbe);
            throw new EzTweetAppException(jbe);

        } catch (IOException ioe) {
            logWrapper.log(logger, "MSGE100011", ioe);
            throw new EzTweetAppException(ioe);

        }

        return tweetList;
    }
}
