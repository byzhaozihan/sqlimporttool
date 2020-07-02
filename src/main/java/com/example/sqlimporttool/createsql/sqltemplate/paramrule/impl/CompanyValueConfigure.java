package com.example.sqlimporttool.createsql.sqltemplate.paramrule.impl;

import com.example.sqlimporttool.createsql.sqltemplate.paramrule.IParamValueConfigure;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 公司SQL模板字段参数配置
 */

public class CompanyValueConfigure implements IParamValueConfigure {
    /**
     * Function<int[], Object> ：
     *     int[0]：执行的次数
     *     int[1]：执行总数
     */
    @Override
    public Map<String, Function<int[], Object>> getInsertValuesMap() {
        Map<String, Function<int[], Object>> valuesMap = new HashMap<>();
        valuesMap.put("#adminorgid", getCompAdminorgId);
        valuesMap.put("#adminnumber", getCompAdminNumber);
        valuesMap.put("#admintype", getCompAdminType);
        valuesMap.put("#adminparent", getCompAdminParent);
        valuesMap.put("#admincompany", getCompAdminCompany);
        valuesMap.put("#adminname", getCompAdminName);
        valuesMap.put("#adminlongnumber", getCompAdminLongNumber);
        valuesMap.put("#adminscortcode", getCompAdminScortCode);
        return valuesMap;
    }

    @Override
    public Map<String, Function<int[], Object>> getDeleteValuesMap() {
        Map<String, Function<int[], Object>> valuesMap = new HashMap<>();
        // 配置删除语句的字段值
        valuesMap.put("#DELCONF", delConf);
        valuesMap.put(((String[]) delConf.apply(new int[0]))[0], getAdminorgid1);
        valuesMap.put(((String[]) delConf.apply(new int[0]))[1], getAdminorgid2);
        return valuesMap;
    }

    // 初始值
    @Value("${paramconfig.company.startAdminorgId}")
    private Long startAdminorgId;

    // id增长步长
    @Value("${paramconfig.company.step}")
    private int step;

    @Value("${paramconfig.company.adminorgRootId}")
    private Long adminorgRootId;

    // 公司生成数
    @Value("${paramconfig.company.adminorgNums}")
    private Long adminorgNums;

    // root number
    @Value("${paramconfig.company.rootNum}")
    private String rootNum;

    // 100个公司
    private Function<int[], Object> getCompAdminorgId = p -> startAdminorgId + p[0] * step;
    private Function<int[], Object> getCompAdminNumber = p -> String.valueOf(startAdminorgId + p[0] * step);
    // 公司类型
    private Function<int[], Object> getCompAdminType = p -> 1020L;
    private Function<int[], Object> getCompAdminParent = p -> adminorgRootId;
    private Function<int[], Object> getCompAdminCompany = p -> startAdminorgId + p[0] * step;
    private Function<int[], Object> getCompAdminName = p -> "测试公司" + p[0];
    // 长编码
    private Function<int[], Object> getCompAdminLongNumber = p -> rootNum + "!" + (startAdminorgId + p[0] * step);
    private Function<int[], Object> getCompAdminScortCode = p -> "999!999";

    // 删除执行的字段值
    private Function<int[], Object> delConf = p -> new String[]{"#adminorgid1", "#adminorgid2"};
    private Function<int[], Object> getAdminorgid1 = p -> startAdminorgId;
    private Function<int[], Object> getAdminorgid2 = p -> startAdminorgId + adminorgNums * step + 10000;
}