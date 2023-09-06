package com.sungeon.bos.entity.pmila;

import com.burgeon.framework.restapi.annotation.RestColumn;
import com.burgeon.framework.restapi.annotation.RestTable;
import com.burgeon.framework.restapi.model.BaseRestBean;
import com.burgeon.framework.restapi.parser.value.LongParser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @BelongsPackage: com.sungeon.bos.entity.pmila
 * @ClassName: PmilaAttribute
 * @Author: 陈苏洲
 * @Description: 品牌方的商品特征集
 * @CreateTime: 2023-09-05 11:41
 * @Version: 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@RestTable(tableName = "M_ATTRIBUTE", description = "商品特征", defaultQueryFilter = "ISACTIVE = 'Y'")
public class PmilaAttribute extends BaseRestBean {
	@RestColumn(name = "ID", isAkField = true, valuePraser = LongParser.class)
	private Long id;
	@RestColumn(name = "NAME")
	private String name;
	@RestColumn(name ="CLRSIZE" )
	private Integer clrSize;
}
