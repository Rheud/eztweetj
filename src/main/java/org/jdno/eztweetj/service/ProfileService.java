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

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.PersistentUserManagedCache;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.UserManagedCacheBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.service.LocalPersistenceService;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.impl.config.persistence.UserManagedPersistenceContext;
import org.ehcache.impl.persistence.DefaultLocalPersistenceService;
import org.jdno.eztweetj.entity.Profile;
import org.jdno.eztweetj.util.Consts;
import org.jdno.eztweetj.util.LogWrapper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * プロファイル情報を管理するキャッシュに対するアクセスを提供するServiceクラス
 * @author rheud
 *
 */
@Service
public class ProfileService implements InitializingBean, DisposableBean {

    /** Log4J 2 ロガー */
    private static final Logger logger = LogManager.getLogger(ProfileService.class);

    /** ログ出力ラッピングクラス */
    @Autowired
    protected LogWrapper logWrapper;


    /** キャッシュのインスタンス */
    protected PersistentUserManagedCache<String, Profile> cache = null;

    /**
     * オブジェクト破棄時の処理。キャッシュのクローズを行う。
     */
    @Override
    public void destroy() throws Exception {
        logger.debug("キャッシュクローズを開始します。");

        cache.close();
        logWrapper.log(logger, "MSGI500001");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("キャッシュをファイルから復元します。");

        // キャッシュの保管場所は起動時のカレントディレクトリとする
        String bootDir = System.getProperty(Consts.CURRENT_DIR_PROP_NAME);

        // ディスクに永続化されるキャッシュを作成する。既に永続化されていた場合は前回の状態が読み込まれる。
        LocalPersistenceService persistenceService =
                new DefaultLocalPersistenceService(new DefaultPersistenceConfiguration(new File(bootDir, Consts.PROFILE_CACHE_FILE_NAME)));
        cache = UserManagedCacheBuilder.newUserManagedCacheBuilder(String.class, Profile.class)
            .with(new UserManagedPersistenceContext<String, Profile>("cache-name", persistenceService))
            .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(10L, EntryUnit.ENTRIES)
                .disk(10L, MemoryUnit.MB, true))
            .build(true);

        logWrapper.log(logger, "MSGI500002");
    }

    /**
     * キャッシュからプロファイル情報を取得する
     * @param name プロファイル名
     * @return プロファイル情報。該当の名前のプロファイルが存在しない場合はnull。
     */
    public Profile get(String name) {
        return cache.get(name);
    }

    /**
     * キャッシュにプロファイル情報を登録する
     * @param name プロファイル名
     * @param profile プロファイル情報
     */
    public void put(String name, Profile profile) {
        cache.put(name, profile);
    }
}
