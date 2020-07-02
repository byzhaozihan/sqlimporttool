DELETE FROM t_heo_stdonbrdevent WHERE ((fsourcebillid >= '#FPERSONID1') AND (fsourcebillid < '#FPERSONID2'));
DELETE FROM T_HERS_ERMANFILEHIS_H USING T_HERS_ERMANFILEHIS_H, (SELECT fvid FROM T_HERS_ERMANFILEHIS WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where T_HERS_ERMANFILEHIS_H.fvid = subQueryTempTable.fvid;
DELETE FROM T_HERS_ERMANFILE WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM T_HERS_ERMANFILEHIS WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_empentrelhis_h USING t_hrpi_empentrelhis_h, (SELECT fvid FROM t_hrpi_empentrelhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_empentrelhis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_empentrel WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_empentrelhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_empjobrelhis_h USING t_hrpi_empjobrelhis_h, (SELECT fvid FROM t_hrpi_empjobrelhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_empjobrelhis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_empjobrel WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_empjobrelhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_percrehis_h USING t_hrpi_percrehis_h, (SELECT fvid FROM t_hrpi_percrehis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_percrehis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_percre WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_percrehis_l USING t_hrpi_percrehis_l, (SELECT fvid FROM t_hrpi_percrehis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_percrehis_l.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_percrehis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_perregionhis_h USING t_hrpi_perregionhis_h, (SELECT fvid FROM t_hrpi_perregionhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_perregionhis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_perregion WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_perregionhis_l USING t_hrpi_perregionhis_l, (SELECT fvid FROM t_hrpi_perregionhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_perregionhis_l.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_perregionhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_emporgrelallhis_h USING t_hrpi_emporgrelallhis_h, (SELECT fvid FROM t_hrpi_emporgrelallhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_emporgrelallhis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_emporgrelall WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_emporgrelallhis_l USING t_hrpi_emporgrelallhis_l, (SELECT fvid FROM t_hrpi_emporgrelallhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_emporgrelallhis_l.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_emporgrelallhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_pertsprophis_h USING t_hrpi_pertsprophis_h, (SELECT fvid FROM t_hrpi_pertsprophis WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2')) subQueryTempTable where t_hrpi_pertsprophis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_pertsprop WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM t_hrpi_pertsprophis WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM t_hrpi_percontacthis_h USING t_hrpi_percontacthis_h, (SELECT fvid FROM t_hrpi_percontacthis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_percontacthis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_percontacthis_l USING t_hrpi_percontacthis_l, (SELECT fvid FROM t_hrpi_percontacthis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_percontacthis_l.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_percontacthis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_percontact WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_pernontsprophis_h USING t_hrpi_pernontsprophis_h, (SELECT fvid FROM t_hrpi_pernontsprophis WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2')) subQueryTempTable where t_hrpi_pernontsprophis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_pernontsprophis WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM t_hrpi_pernontsprop WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM t_hrpi_empposorgrelhis_h USING t_hrpi_empposorgrelhis_h, (SELECT fvid FROM t_hrpi_empposorgrelhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_empposorgrelhis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_empposorgrel WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_empposorgrelhis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_employeehis_h USING t_hrpi_employeehis_h, (SELECT fvid FROM t_hrpi_employeehis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_employeehis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_employee_l USING t_hrpi_employee_l, (SELECT fid FROM t_hrpi_employee WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_employee_l.fid = subQueryTempTable.fid;
DELETE FROM t_hrpi_employeehis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_employee WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_cmpemphis_h USING t_hrpi_cmpemphis_h, (SELECT fvid FROM t_hrpi_cmpemphis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_cmpemphis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_cmpemphis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_cmpemp WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_depemphis_h USING t_hrpi_depemphis_h, (SELECT fvid FROM t_hrpi_depemphis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'))) subQueryTempTable where t_hrpi_depemphis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_depemphis WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_depemp WHERE ((fpersonid >= '#FPERSONID1') AND (fpersonid < '#FPERSONID2'));
DELETE FROM t_hrpi_personhis_h USING t_hrpi_personhis_h, (SELECT fvid FROM t_hrpi_personhis WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2')) subQueryTempTable where t_hrpi_personhis_h.fvid = subQueryTempTable.fvid;
DELETE FROM t_hrpi_personhis WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM t_hrpi_person_l WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM t_hrpi_person WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_ONBRDBILL WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_ONBRDBILL_L WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_ONBRDBILL_A WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_PERREGION WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_CONTACT WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_CONTACT_L WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_PERSONINFO WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_PERSONINFO_L WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_PERCRE WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_PERCRE_L WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HEO_ONBRDROLEREL WHERE (FENTRYID >= '#FPERSONID1') AND (FENTRYID < '#FPERSONID2');
DELETE FROM T_HLCS_CONTRACTSIGNBILL WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');
DELETE FROM T_HLCS_CONTRACTSIGNBILL_B WHERE (fid >= '#FPERSONID1') AND (fid < '#FPERSONID2');











