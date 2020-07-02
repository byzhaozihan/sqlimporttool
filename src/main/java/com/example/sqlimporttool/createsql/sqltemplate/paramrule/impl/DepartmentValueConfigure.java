package com.example.sqlimporttool.createsql.sqltemplate.paramrule.impl;

import com.example.sqlimporttool.createsql.sqltemplate.paramrule.IParamValueConfigure;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 部门SQL模板字段参数配置
 */
public class DepartmentValueConfigure implements IParamValueConfigure {
    /**
     * Function<int[], Object> ：
     *     int[0]：执行的次数
     *     int[1]：执行总数
     */
    @Override
    public Map<String, Function<int[], Object>> getInsertValuesMap() {
        Map<String, Function<int[], Object>> valuesMap = new HashMap<>();
        valuesMap.put("#adminorgid", getDepAdminorgId);
        valuesMap.put("#adminnumber", getDepAdminNumber);
        valuesMap.put("#admintype", getDepAdminType);
        valuesMap.put("#adminparent", getDepAdminParent);
        valuesMap.put("#admincompany", getDepAdminCompany);
        valuesMap.put("#adminname", getDepAdminName);
        valuesMap.put("#adminlongnumber", getDepAdminLongNumber);
        valuesMap.put("#adminscortcode", getDepAdminScortCode);
        return valuesMap;
    }

    @Override
    public Map<String, Function<int[], Object>> getDeleteValuesMap() {
        Map<String, Function<int[], Object>> valuesMap = new HashMap<>();
        // 配置删除语句的字段值
        valuesMap.put("#DELCONF", delConf);
        valuesMap.put(((String[]) delConf.apply(new int[0]))[0], getDepartmentId1);
        valuesMap.put(((String[]) delConf.apply(new int[0]))[1], getDepartmentId2);
        return valuesMap;
    }

    // 公司id初始值
    @Value("${paramconfig.department.startAdminorgId}")
    private Long startAdminorgId;
    // 部门id初始值比公司多10倍
    @Value("${paramconfig.department.startDepartmentId}")
    private Long startDepartmentId;
    // id增长步长
    @Value("${paramconfig.department.step}")
    private int step;
    // 公司生成数量
    @Value("${paramconfig.department.companyNums}")
    private Long companyNums;
    // 部门生成数
    @Value("${paramconfig.department.departmentNums}")
    private Long departmentNums;
    // root number
    @Value("${paramconfig.department.rootNum}")
    private String rootNum;

    // p[0]为循环次数，p[1]为总数
    private Function<int[], Object> getDepAdminorgId = p -> startDepartmentId + p[0] * step;
    private Function<int[], Object> getDepAdminNumber = p -> String.valueOf(startDepartmentId + p[0] * step);
    private Function<int[], Object> getDepAdminType = p -> 1040L;
    private Function<int[], Object> getDepAdminParent = p -> startAdminorgId + (p[0] / (p[1] / companyNums)) * step;
    private Function<int[], Object> getDepAdminCompany = p -> startAdminorgId + (p[0] / (p[1] / companyNums)) * step;
    private Function<int[], Object> getDepAdminName = p -> "测试部门" + p[0];
    private Function<int[], Object> getDepAdminLongNumber = p -> rootNum + "!" + (startAdminorgId + (p[0] / (p[1] / companyNums)) * step)
            + "!" + (startDepartmentId + p[0] * step);
    private Function<int[], Object> getDepAdminScortCode = p -> "999!999!999";

    // 删除执行的字段值
    private Function<int[], Object> delConf = p -> new String[]{"#adminorgid1", "#adminorgid2"};
    private Function<int[], Object> getDepartmentId1 = p -> startDepartmentId;
    private Function<int[], Object> getDepartmentId2 = p -> startDepartmentId + departmentNums * step + 10000;
}