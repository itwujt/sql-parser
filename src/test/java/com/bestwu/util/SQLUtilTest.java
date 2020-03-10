package com.bestwu.util;

import com.bestwu.tools.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Best Wu
 */
@Slf4j
public class SQLUtilTest {

    @Test
    public void test0() {
        String sql = "select * from user";
        List<Pair<String, String>> pairs = SQLUtil.obtainTableAndAlias(sql);
        pairs.forEach(pair -> log.info("表名：{}， 别名：{}", pair.of(), pair.next()));
    }

    @Test
    public void test1() {
        String sql = "select * from user where User in (select User from user) and Host in (select Host from user)";
        List<Pair<String, String>> pairs = SQLUtil.obtainTableAndAlias(sql);
        pairs.forEach(pair -> log.info("表名：{}， 别名：{}", pair.of(), pair.next()));
    }
}