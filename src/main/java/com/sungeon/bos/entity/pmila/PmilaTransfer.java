package com.sungeon.bos.entity.pmila;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.annotation.RestColumn;
import com.burgeon.framework.restapi.annotation.RestOneToMany;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.parser.value.LongParser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 陈苏洲
 * @date 2023-8-29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "M_V1_TRANSFER", description = "门店调拨单", defaultQueryFilter = "M_V1_TRANSFER.ISACTIVE = 'Y'"
		, isNeedSubmit = true)
public class PmilaTransfer extends BaseRestBean {

	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;
	@RestColumn(name = "DOCNO")
	private String docNo;
	@RestColumn(name = "BILLDATE", isRestSave = true)
	private Integer billDate;
	@RestColumn(name = "C_ORIG_ID;NAME")
	private String origName;
	@RestColumn(name = "C_ORIG_ID__NAME", isRestSave = true, isRestQuery = false)
	private String origNameAdd;
	@RestColumn(name = "C_DEST_ID;NAME")
	private String destName;
	@RestColumn(name = "C_DEST_ID__NAME", isRestSave = true, isRestQuery = false)
	private String destNameAdd;
	@RestColumn(name = "C_TRANSFERTYPE_ID__NAME", isRestSave = true, isRestQuery = false)
	private String transferType = "正常调拨";
	@RestColumn(name = "SOURCENO", isRestSave = true)
	private String sourceNo;
	@RestColumn(name = "DESCRIPTION", isRestSave = true)
	private String description;
	@RestColumn(name = "STATUS")
	private String status;
	@RestColumn(name = "OUT_STATUS")
	private String outStatus;
	@RestColumn(name = "IN_STATUS")
	private String inStatus;
	@RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "transferId", childBeanClass = PmilaTransferItem.class)
	private List<PmilaTransferItem> items;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
