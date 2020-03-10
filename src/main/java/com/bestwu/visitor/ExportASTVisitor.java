package com.bestwu.visitor;

import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.bestwu.tools.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 visitor <br>
 * @author Best Wu
 */
@Slf4j
public class ExportASTVisitor extends MySqlASTVisitorAdapter {

    /**
     * 用于存储 从sql 中解析的 表名以及别名
     */
    private List<Pair<String, String>> tablePairs = new ArrayList<>();
    /**
     * 用于存储 从外部传来的 条件 <br>
     * 条件中的 key 为 表名/别名  value 为 拼接好的条件 <br>
     */
    private List<Pair<String, String>> conditionPairs;

    /**
     * 所有的 sql 都会走的 方法，并且只会走一次
     * @param selectStatement 查询代码段
     * @return boolean
     */
    @Override
    public boolean visit(SQLSelectStatement selectStatement) {
        return true;
    }

    /**
     * 用于获取 表名 和 别名
     * @param x tableSource
     * @return boolean
     */
    @Override
    public boolean visit(SQLExprTableSource x) {
        if (conditionPairs == null || conditionPairs.isEmpty()) {
            // 获取表名和别名
            String tableName = x.getName().getSimpleName();
            String alias = x.getAlias();
            // 如果在 tablePairs 中已经存在的 k/v 存在相同的话就不再进行重复添加
            if (!tablePairs.isEmpty()) {
                tablePairs.forEach(tablePair -> {
                    String existTable = tablePair.of();
                    String existTableAlias = tablePair.next();
                    if (existTable.equals(tableName) && existTableAlias == null && alias == null) {
                        return;
                    }
                });
                return true;
            }
            Pair<String, String> pair = Pair.newInstance();
            pair.put(tableName, alias);
            tablePairs.add(pair);
            return true;
        }
        // 添加逻辑的部分在这个方法里直接跳过
        return true;
    }

    /**
     * 处理 from 后的子查询
     * @param x subqueryTableSource
     * @return boolean
     */
    @Override
    public boolean visit(SQLSubqueryTableSource x) {
        return true;
    }

    /**
     * 处理 in 后的子查询
     * @param x inSubQueryExpr
     * @return boolean
     */
    @Override
    public boolean visit(SQLInSubQueryExpr x) {
        return true;
    }



    @Override
    public boolean visit(SQLUnionQuery x) {
        return super.visit(x);
    }

    @Override
    public boolean visit(SQLUnionQueryTableSource x) {
        return super.visit(x);
    }

    /**
     * 用于获取 从 sql 中 获取的 tableName 和 alias
     * @return java.util.List&lt;com.bestwu.tools.Pair&lt;java.lang.String, java.lang.String&gt;&gt;
     */
    public List<Pair<String, String>> tablePairs() {
        return this.tablePairs;
    }

    /**
     * 对外提供的创建对象方法
     * @return com.bestwu.visitor.ExportASTVisitor
     */
    public static ExportASTVisitor newInstance() {
        return new ExportASTVisitor();
    }

    /**
     * 用于 添加条件时 创建的 自定义 visitor <br>
     * 对外提供的创建对象方法 <br>
     * @param conditionPairs 条件列表
     * @return com.bestwu.visitor.ExportASTVisitor
     */
    public static ExportASTVisitor newInstance(List<Pair<String, String>> conditionPairs) {
        return new ExportASTVisitor(conditionPairs);
    }



    private ExportASTVisitor() {

    }

    private ExportASTVisitor(List<Pair<String, String>> conditionPairs) {
        this.conditionPairs = conditionPairs;
    }
}
