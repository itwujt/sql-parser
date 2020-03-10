package com.bestwu.util;


import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.bestwu.tools.Pair;
import com.bestwu.visitor.ExportASTVisitor;

import java.util.List;

/**
 *
 * @author Best Wu
 */
public class SQLUtil {


    public static List<Pair<String, String>> obtainTableAndAlias(String sql) {
        sql = SQLUtils.formatMySql(sql);
        ExportASTVisitor visitor = ExportASTVisitor.newInstance();
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        sqlStatement.accept(visitor);

        return visitor.tablePairs();
    }
}
