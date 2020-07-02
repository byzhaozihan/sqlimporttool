package com.example.sqlimporttool.createsql.sqltemplate.paramrule.impl;

import com.example.sqlimporttool.createsql.sqltemplate.paramrule.IParamValueConfigure;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 字段数据参数配置
 */
public class OnBrdFinishValueConfigure implements IParamValueConfigure {
    @Override
    public Map<String, Function<int[], Object>> getInsertValuesMap() {
        Map<String, Function<int[], Object>> valuesMap = new HashMap<>();
        valuesMap.put("#FPERSONID", getPersonId);
        valuesMap.put("#FHRBUID", getHRBUId);
        valuesMap.put("#FAADMINORGID", getAAdminorgId);
        valuesMap.put("#FEMPNUMBER", getEmpNumber);
        valuesMap.put("#FCERTIFICATENUMBER", getCertificateNumber);
        valuesMap.put("#FNAME", getName);
        valuesMap.put("#FLANGID1", getZhCN);
        valuesMap.put("#FLANGID2", getZhTW);
        valuesMap.put("#FMOBILE", getMobile);
        valuesMap.put("#FAPOSITIONID", getAPosId);
        valuesMap.put("#FBILLNO", getBillNo);
        valuesMap.put("#FBARCODE", getBarCode);
        valuesMap.put("#FORGID", getOrgId);
        valuesMap.put("#FCONBILLNO", getConBillNo);
        valuesMap.put("#FCONBARCODE", getConBarCode);
        return valuesMap;
    }

    @Override
    public Map<String, Function<int[], Object>> getDeleteValuesMap() {
        Map<String, Function<int[], Object>> valuesMap = new HashMap<>();
        // 配置删除语句的字段值
        valuesMap.put("#DELCONF", delConf);
        valuesMap.put(((String[]) delConf.apply(new int[0]))[0], getPersonId1);
        valuesMap.put(((String[]) delConf.apply(new int[0]))[1], getPersonId2);
        return valuesMap;
    }

    @Value("${paramconfig.onbrdfinish.startPersonId}")
    private Long startPersonId;
    // 生成人员总数量
    @Value("${paramconfig.onbrdfinish.personNums}")
    private Long personNums;
    @Value("${paramconfig.onbrdfinish.step}")
    private int step;
    @Value("${paramconfig.onbrdfinish.startMobileNum}")
    private Long startMobileNum;
    // 部门id初始值
    @Value("${paramconfig.onbrdfinish.startAAdminorgId}")
    private Long startAAdminorgId;
    // 部门生成数：10万
    @Value("${paramconfig.onbrdfinish.adminorgNums}")
    private Long adminorgNums;
    // 岗位初始id
    @Value("${paramconfig.onbrdfinish.startAPosId}")
    private Long startAPosId;
    // 岗位总数
    @Value("${paramconfig.onbrdfinish.positionNums}")
    private Long positionNums;

    @Value("${paramconfig.onbrdfinish.startCertNumber}")
    private Long startCertNumber;
    @Value("${paramconfig.onbrdfinish.startEmpNumber}")
    private Long startEmpNumber;
    @Value("${paramconfig.onbrdfinish.startBillno}")
    private Long startBillno;
    @Value("${paramconfig.onbrdfinish.startBarCode}")
    private Long startBarCode;
    // 多语言表数据id
    @Value("${paramconfig.onbrdfinish.startLangId}")
    private Long startLangId;
    @Value("${paramconfig.onbrdfinish.fconbillno}")
    private Long fconbillno;
    @Value("${paramconfig.onbrdfinish.fconbarcode}")
    private Long fconbarcode;
    @Value("${paramconfig.onbrdfinish.personFlag}")
    private String personFlag;

    private Function<int[], Object> getPersonId = p -> startPersonId + p[0] * step;
    private Function<int[], Object> getMobile = p -> String.valueOf(startMobileNum++);
    //TODO:
    private Long[] existedOrgIds = new Long[]{100000L, 100001L, 100002L, 100003L,
            100004L, 100005L, 100006L, 100007L, 100008L, 100009L};
    private Function<int[], Object> getOrgId = p -> {
        int count = p[0];
        return (count + 1) % 56;
    };

    //TODO:
    private Long[] existHRBUId = new Long[]{100000L, 100001L, 100002L, 100003L,
            100004L, 100005L, 100006L, 100007L, 100008L, 100009L};
    private Function<int[], Object> getHRBUId = p -> {
        int count = p[0];
        return (count + 1) % 56;
    };

    private Function<int[], Object> getAAdminorgId = p -> {
        int count = p[0];
        int nums = p[1];
        // 每个部门平均人员数
        int avgPerNum = (int) (nums / adminorgNums);
        if (avgPerNum != 0 && count % avgPerNum == 0) {
            return startAAdminorgId + (count / avgPerNum) * step;
        }
        return startAAdminorgId;
    };

    private Function<int[], Object> getAPosId = p -> {
        int count = p[0];
        int nums = p[1];
        // 每个岗位平均人员数
        int avgPerPos = (int) (nums / positionNums);
        if (avgPerPos != 0 && count % avgPerPos == 0) {
            return startAPosId + (count / avgPerPos) * step;
        }
        return startAPosId;
    };

    private Function<int[], Object> getCertificateNumber = p -> String.valueOf(startCertNumber++);
    private Function<int[], Object> getEmpNumber = p -> String.valueOf(startEmpNumber++);
    private Function<int[], Object> getBillNo = p -> String.valueOf(startBillno++);
    private Function<int[], Object> getBarCode = p -> String.valueOf(startBarCode++);
    private Function<int[], Object> getZhCN = p -> String.valueOf(startLangId++);
    private Function<int[], Object> getZhTW = p -> String.valueOf(startLangId++);
    private Long fname = 0L;
    private String[] firName = new String[]{"李", "王", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴", "徐", "孙", "胡", "朱", "高",
            "林", "何", "郭", "马", "罗", "梁", "宋", "郑", "谢", "韩", "唐", "冯", "于", "董", "萧"};
    private Function<int[], Object> getName = p -> firName[p[0] % 30] + "测试" + personFlag + String.valueOf(fname++);
    private Function<int[], Object> getConBillNo = p -> String.valueOf(fconbillno++);
    private Function<int[], Object> getConBarCode = p -> String.valueOf(fconbarcode++);

    // 删除执行的字段值
    private Function<int[], Object> delConf = p -> new String[]{"#FPERSONID1", "#FPERSONID2"};
    private Function<int[], Object> getPersonId1 = p -> startPersonId;
    private Function<int[], Object> getPersonId2 = p -> startPersonId + 10000 + personNums * step;
}
