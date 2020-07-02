package com.example.sqlimporttool.createsql.sqltemplate.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.example.sqlimporttool.createsql.sqltemplate.paramrule.IParamValueConfigure;
import com.example.sqlimporttool.importdb.DataTask;
import com.example.sqlimporttool.importdb.SingleSqlInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class TemplateSQLParser {

    private IParamValueConfigure paramValueConfigure;

    @Autowired
    public TemplateSQLParser(IParamValueConfigure iParamValueConfigure) {
        this.paramValueConfigure = iParamValueConfigure;
    }

    private static final String DB_TYPE = JdbcConstants.MYSQL;

    /**
     * 解析插入数据SQL模板
     *
     * @param sqlFilePath SQL模板路径
     * @param nums        生成数量
     */
    public void parseInsertSQLData(String sqlFilePath, Integer nums, DataTask dataTask) {
        String sqlFile = loadSQLFile(sqlFilePath);
        // 解析得到SQL语句列表
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sqlFile, DB_TYPE);
        List<SingleSqlInfo> sqlObjectDataList;
        // 计算循环次数
        int loopTimes = nums == null ? 1 : nums;
        // 调用字段值配置的参数
        int[] confParam = new int[2];
        confParam[1] = loopTimes;
        for (int i = 0; i < loopTimes; i++) {
            sqlObjectDataList = new ArrayList<>();
            confParam[0] = i;
            for (SQLStatement sqlStatement : sqlStatements) {
                // 仅解析插入的SQL
                if (!(sqlStatement instanceof SQLInsertStatement)) {
                    continue;
                }
                SQLInsertStatement sqlInsertStatement = (SQLInsertStatement) sqlStatement;
                // 构造占位符SQL
                String holderSQLExpr = buildPlaceHolderSqlExpr(sqlInsertStatement);
                // 设置配置的字段参数数据
                List<SQLExpr> values = sqlInsertStatement.getValues().getValues();
                Object[] fieldValues = getParamValues(values, confParam);
                SingleSqlInfo singleSqlInfo = new SingleSqlInfo(holderSQLExpr, fieldValues);
                sqlObjectDataList.add(singleSqlInfo);
            }
            dataTask.createData(sqlObjectDataList);
        }
    }

    /**
     * 解析删除数据SQL模板
     *
     * @param sqlFilePath SQL模板路径
     */
    public void parseDeleteSQLData(String sqlFilePath, DataTask dataTask) {
        String sqlFile = loadSQLFile(sqlFilePath);
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sqlFile, DB_TYPE);
        List<SingleSqlInfo> sqlObjectDataList = new ArrayList<>();
        for (SQLStatement sqlStatement : sqlStatements) {
            if (!(sqlStatement instanceof SQLDeleteStatement)) {
                continue;
            }
            SQLDeleteStatement sqlDeleteStatement = (SQLDeleteStatement) sqlStatement;
            String holderSQLExpr = buildDelPlaceHolderSQLExpr(sqlDeleteStatement);
            Object[] fieldValues = getDelParamValues();
            SingleSqlInfo singleSqlInfo = new SingleSqlInfo(holderSQLExpr, fieldValues);
            sqlObjectDataList.add(singleSqlInfo);
        }
        dataTask.createData(sqlObjectDataList);
    }

    /**
     * 解析SQL语句参数值
     */
    private Object[] getParamValues(List<SQLExpr> values, int[] params) {
        int size = values.size();
        Object[] paramValues = new Object[size];
        for (int i = 0; i < size; i++) {
            SQLExpr value = values.get(i);
            String simpleName = value.getClass().getSimpleName();
            switch (simpleName) {
                case "SQLDateExpr":
                    SQLDateExpr dateExpr = (SQLDateExpr) value;
                    String dateStr = dateExpr.getValue();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = dateFormat.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    paramValues[i] = date;
                    break;
                case "SQLIntegerExpr":
                    SQLIntegerExpr integerExpr = (SQLIntegerExpr) value;
                    long number = integerExpr.getNumber().longValue();
                    paramValues[i] = number;
                    break;
                case "SQLCharExpr":
                    SQLCharExpr sqlExpr = (SQLCharExpr) value;
                    String paramValue = sqlExpr.getValue().toString();
                    Object fieldValue = getFieldValue(paramValue, params);
                    paramValues[i] = fieldValue;
                    break;
                case "SQLNullExpr":
                    paramValues[i] = null;
                    break;
                default:
                    paramValues[i] = " ";
                    break;
            }
        }
        return paramValues;
    }

    /**
     * 通过配置获取删除id的参数值
     */
    private Object[] getDelParamValues() {
        Map<String, Function<int[], Object>> paramsMap = paramValueConfigure.getDeleteValuesMap();
        int[] p = new int[0];
        String[] params = (String[]) paramsMap.get("#DELCONF").apply(p);
        Object[] paramValues = new Object[2];
        paramValues[0] = paramsMap.get(params[0]).apply(p);
        paramValues[1] = paramsMap.get(params[1]).apply(p);
        return paramValues;
    }

    /**
     * 获取SQL模板配置参数的字段数据
     *
     * @param paramStr 字段参数
     * @param params   获取数据的参数
     * @return 字段数据
     */
    private Object getFieldValue(String paramStr, int[] params) {
        Map<String, Function<int[], Object>> paramsMap = paramValueConfigure.getInsertValuesMap();
        if (paramStr.contains("#")) {
            return paramsMap.get(paramStr).apply(params);
        }
        return paramStr;
    }

    /**
     * 构造插入数据的占位符的SQL语句
     * <p>
     * eg: insert into tabelA (fid, fnumber) values (?,?)
     *
     * @param sqlInsertStatement SQL解析语句
     * @return 含占位符语句SQL
     */
    private String buildPlaceHolderSqlExpr(SQLInsertStatement sqlInsertStatement) {
        String replaceSql = getReplaceSql(sqlInsertStatement.getColumns().size());
        String sql = sqlInsertStatement.toLowerCaseString();
        String subSql = sql.substring(sql.indexOf("values"));
        return sql.replace(subSql, replaceSql);
    }

    /**
     * 构造删除的占位符SQL
     */
    private String buildDelPlaceHolderSQLExpr(SQLDeleteStatement sqlDeleteStatement) {
        Map<String, Function<int[], Object>> paramsMap = paramValueConfigure.getDeleteValuesMap();
        String[] params = (String[]) paramsMap.get("#DELCONF").apply(new int[0]);
        String replaceParam1 = params[0];
        String replaceParam2 = params[1];
        String statement = sqlDeleteStatement.toLowerCaseString();
        if (statement.contains("#")) {
            return statement.replace("'", "").replace(";", "")
                    .replace(replaceParam1, "?").replace(replaceParam2, "?");
        }
        return statement;
    }

    /**
     * 设置SQL占位符
     *
     * @param size 占位符数
     * @return SQL
     */
    private String getReplaceSql(int size) {
        StringBuilder sql = new StringBuilder();
        sql.append("values (");
        for (int i = 0; i < size; i++) {
            sql.append("?");
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        return sql.toString();
    }

    /**
     * 加载SQL模板文件
     *
     * @param path 文件路径
     * @return SQL字符串
     */
    private String loadSQLFile(String path) {
        String sqlString;
        byte[] buffer = null;
        File sqlFile = new File(path);
        int fileSize = (int) sqlFile.length();
        // 取1024B的倍数
        int time = fileSize / 1024 + 1;
        buffer = new byte[1024 * time];
        InputStream inputStream = null;
        int len = 0;
        try {
            inputStream = new FileInputStream(sqlFile);
            while ((len = inputStream.read(buffer)) != -1) {
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqlString = new String(buffer);
        return sqlString;
    }
}

