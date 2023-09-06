package com.sungeon.bos.entity.pmila;

import com.burgeon.framework.restapi.annotation.RestColumn;
import com.burgeon.framework.restapi.annotation.RestOneToMany;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.parser.value.LongParser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @BelongsPackage: com.sungeon.bos.entity.pmila
 * @ClassName: PmilaCuspurchaseReturn
 * @Author: 陈苏洲
 * @Description: 经销商采购退货单
 * @CreateTime: 2023-09-01 09:48
 * @Version: 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "M_V_RET_CUSPUR", description = "经销商采购退货单",
		defaultQueryFilter = "M_V_RET_CUSPUR.ISACTIVE = 'Y'")
public class PmilaCuspurchaseReturn extends BaseRestBean {
	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;
	@RestColumn(name = "DOCNO")
	private String docNo;
	@RestColumn(name = "BILLDATE", isRestSave = true)
	private Integer billDate;
	@RestColumn(name = "DATEOUT", isRestSave = true)
	private Integer outDate;
	@RestColumn(name = "DATEIN", isRestSave = true)
	private Integer inDate;
	@RestColumn(name = "DESCRIPTION", isRestSave = true)
	private String description;
	@RestColumn(name = "STATUS")
	private String status;
	@RestColumn(name = "OUT_STATUS")
	private String outStatus;
	@RestColumn(name = "IN_STATUS")
	private String inStatus;
	@RestOneToMany(fkParentColumnName = "id", fkChildColumnName = "retSaleId", childBeanClass = PmilaCuspurchaseReturnItem.class)
	private List<PmilaCuspurchaseReturnItem> items;
}
