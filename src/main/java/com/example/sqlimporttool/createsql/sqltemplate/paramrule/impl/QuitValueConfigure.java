package com.example.sqlimporttool.createsql.sqltemplate.paramrule.impl;

import com.example.sqlimporttool.createsql.sqltemplate.paramrule.IParamValueConfigure;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 离职SQL模板参数值配置
 */
public class QuitValueConfigure implements IParamValueConfigure {
    @Override
    public Map<String, Function<int[], Object>> getInsertValuesMap() {
        Map<String, Function<int[], Object>> valuesMap = new HashMap<>();
        valuesMap.put("#FPERSONID", getPersonId);
        valuesMap.put("#FBILLNO", getBillNo);
        valuesMap.put("#FORGID", getOrgId);
        valuesMap.put("#FHRBUID", getHRBUId);
        valuesMap.put("#FBARCODE", getBarCode);
        valuesMap.put("#FBADMINORGID", getBAdminorgId);
        valuesMap.put("#FAADMINORGID", getAAdminorgId);
        valuesMap.put("#FBPOSITIONID", getBPosId);
        valuesMap.put("#FAPOSITIONID", getAPosId);
        valuesMap.put("#FBCOMPANYID", getBCompanyId);
        valuesMap.put("#FCERTIFICATENUMBER", getCertificateNumber);
        valuesMap.put("#FLANGID1", getZhCN);
        valuesMap.put("#FLANGID2", getZhTW);
        valuesMap.put("#FCREATORID", getCreatorId);
        valuesMap.put("#FTIME", getUniformTimeLast2Year);
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

    @Value("${paramconfig.quit.startPersonId}")
    private Long startPersonId;
    // 生成人员总数量
    @Value("${paramconfig.quit.personNums}")
    private Long personNums;
    @Value("${paramconfig.quit.step}")
    private int step;
    // 部门id初始值
    @Value("${paramconfig.quit.startBAdminorgId}")
    private Long startBAdminorgId;
    // 部门生成数：10万
    @Value("${paramconfig.quit.adminorgNums}")
    private Long adminorgNums;
    // 公司id初始值
    @Value("${paramconfig.quit.startBCompanyId}")
    private Long startBCompanyId;
    // 公司生成数：100个
    @Value("${paramconfig.quit.companyNums}")
    private Long companyNums;
    // 岗位初始id
    @Value("${paramconfig.quit.startBPosId}")
    private Long startBPosId;
    // 岗位总数
    @Value("${paramconfig.quit.positionNums}")
    private Long positionNums;

    @Value("${paramconfig.quit.startCertNumber}")
    private Long startCertNumber;
    @Value("${paramconfig.quit.startBillno}")
    private Long startBillno;
    @Value("${paramconfig.quit.startBarCode}")
    private Long startBarCode;
    // 多语言表数据id
    @Value("${paramconfig.quit.startLangId}")
    private Long startLangId;
    @Value("${paramconfig.quit.fuserid}")
    private Long fuserid;

    private Long[] existedOrgIds = new Long[]{687395562785352704L,687395907917851648L,687396149836918784L,
            687396686170959872L,687397112664566784L,687397370379381760L,687397961205820416L,687398324331883520L,
            687398563340103680L,687398892089651200L,687399153260571648L,687399323448651776L,687399567959796736L,
            687399802102624256L,687400124862705664L,687400272846138368L,687400608348516352L,692501230861156352L,
            692501233360961536L,692658788255137792L};
    private Function<int[], Object> getOrgId = p -> {
        int count = p[0];
        return existedOrgIds[count % existedOrgIds.length];
    };

    private Long[] existHRBUId = new Long[]{687395562785352704L,687395907917851648L,687396149836918784L,
            687396686170959872L,687397112664566784L,687397370379381760L,687397961205820416L,687398324331883520L,
            687398563340103680L,687398892089651200L,687399153260571648L,687399323448651776L,687399567959796736L,
            687399802102624256L,687400124862705664L,687400272846138368L,687400608348516352L,692501230861156352L,
            692501233360961536L,692658788255137792L};
    private Function<int[], Object> getHRBUId = p -> {
        int count = p[0];
        return existHRBUId[count % existHRBUId.length];
    };

    private Function<int[], Object> getPersonId = p -> startPersonId + p[0] * step;
    private Function<int[], Object> getCertificateNumber = p -> String.valueOf(startCertNumber++);
    private Function<int[], Object> getBillNo = p -> String.valueOf(startBillno++);
    private Function<int[], Object> getBarCode = p -> String.valueOf(startBarCode++);
    private Function<int[], Object> getZhCN = p -> String.valueOf(startLangId++);
    private Function<int[], Object> getZhTW = p -> String.valueOf(startLangId++);

    private Function<int[], Object> getBAdminorgId = p -> {
        int count = p[0];
        int nums = p[1];
        // 每个部门平均人员数
        int avgPerNum = (int) (nums / adminorgNums);
        if (avgPerNum != 0 && count % avgPerNum == 0) {
            return startBAdminorgId + (count / avgPerNum) * step;
        }
        return startBAdminorgId;
    };

    private Function<int[], Object> getAAdminorgId = p -> getBAdminorgId.apply(p);

    private Function<int[], Object> getBCompanyId = p -> {
        // 获取人员的部门id
        Long adminorgId = (Long) getBAdminorgId.apply(p);
        // 部门增加的数量
        long differAdminorg = adminorgId - startBAdminorgId;
        if (differAdminorg == 0) {
            return startBCompanyId;
        }
        // 平均每个公司的部门数
        long avgAdminNum = adminorgNums / companyNums;
        // 确定部门对应的公司
        long location = (differAdminorg / step) / avgAdminNum;
        return startBCompanyId + location * step;
    };

    private Function<int[], Object> getBPosId = p -> {
        int count = p[0];
        int nums = p[1];
        // 每个岗位平均人员数
        int avgPerPos = (int) (nums / positionNums);
        if (avgPerPos != 0 && count % avgPerPos == 0) {
            return startBPosId + (count / avgPerPos) * step;
        }
        return startBPosId;
    };

    private Function<int[], Object> getAPosId = p -> getBPosId.apply(p);

    private Function<int[], Object> getCreatorId = p -> fuserid;

    /**
     * 最近两年内的均匀分布的时间，以天为间隔
     */
    private Function<int[], Object> getUniformTimeLast2Year = p -> {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, -p[0] % (365 * 2));
        return new Timestamp(calendar.getTime().getTime());
    };

    // 删除执行的字段值
    private Function<int[], Object> delConf = p -> new String[]{"#FPERSONID1", "#FPERSONID2"};
    private Function<int[], Object> getPersonId1 = p -> startPersonId;
    private Function<int[], Object> getPersonId2 = p -> startPersonId + 10000 + personNums * step;
}