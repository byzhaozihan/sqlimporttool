package com.example.sqlimporttool.createsql.sqltemplate.paramrule.impl;

import com.example.sqlimporttool.createsql.sqltemplate.paramrule.IParamValueConfigure;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 岗位SQL模板参数值配置
 *
 */
public class PositionValueConfigure implements IParamValueConfigure {
    @Override
    public Map<String, Function<int[], Object>> getInsertValuesMap() {
        Map<String, Function<int[], Object>> valuesMap = new HashMap<>();
        valuesMap.put("#positionid", getPositionId);
        valuesMap.put("#positionname", getPositionName);
        valuesMap.put("#adminorgid", getAdminorgId);
        valuesMap.put("#companyid", getCompanyId);
        return valuesMap;
    }

    @Override
    public Map<String, Function<int[], Object>> getDeleteValuesMap() {
        Map<String, Function<int[], Object>> valuesMap = new HashMap<>();
        // 配置删除语句的字段值
        valuesMap.put("#DELCONF", delConf);
        valuesMap.put(((String[]) delConf.apply(new int[0]))[0], getPositionId1);
        valuesMap.put(((String[]) delConf.apply(new int[0]))[1], getPositionId2);
        return valuesMap;
    }

    @Value("${paramconfig.position.startPositionId}")
    private Long startPositionId;
    @Value("${paramconfig.position.step}")
    private int step;
    // 公司id初始值
    @Value("${paramconfig.position.startCompanyId}")
    private Long startCompanyId;
    // 部门id初始值
    @Value("${paramconfig.position.startDepartmentId}")
    private Long startDepartmentId;
    // 生成岗位数
    @Value("${paramconfig.position.positionNums}")
    private Long positionNums;
    // 生成部门数
    @Value("${paramconfig.position.departmentNums}")
    private Long departmentNums;
    // 生成公司数
    @Value("${paramconfig.position.companyNums}")
    private Long companyNums;

    private Function<int[], Object> getPositionId = p -> p[0] * step + startPositionId;
    private Function<int[], Object> getPositionName = p -> "测试岗位" + p[0];
    private Function<int[], Object> getAdminorgId = p -> {
        long avgPosNum = positionNums / departmentNums;
        return startDepartmentId + (p[0] / avgPosNum) * step;
    };

    private Function<int[], Object> getCompanyId = p -> {
        Long departmentIds = (Long) getAdminorgId.apply(p);
        long depNums = departmentIds - startDepartmentId;
        return startCompanyId + (depNums / (departmentNums / companyNums));
    };

    // 删除执行的字段值
    private Function<int[], Object> delConf = p -> new String[]{"#positionid1", "#positionid2"};
    private Function<int[], Object> getPositionId1 = p -> startPositionId;
    private Function<int[], Object> getPositionId2 = p -> startPositionId + positionNums * step + 10000;
}