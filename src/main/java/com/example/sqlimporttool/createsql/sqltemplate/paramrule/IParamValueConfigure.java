package com.example.sqlimporttool.createsql.sqltemplate.paramrule;

import java.util.Map;
import java.util.function.Function;

/**
 * SQL模板字段参数值配置接口
 */
public interface IParamValueConfigure {
    Map<String, Function<int[], Object>> getInsertValuesMap();
    Map<String, Function<int[], Object>> getDeleteValuesMap();
}