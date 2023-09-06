package com.sungeon.bos.entity.base;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * @BelongsPackage: com.sungeon.bos.entity.base
 * @ClassName: AttributeEntity
 * @Author: 陈苏洲
 * @Description: 商品特征
 * @CreateTime: 2023-09-05 11:05
 * @Version: 1.0
 */

@Data
@Alias("Attribute")
public class AttributeEntity {

	private static final long serialVersionUID = 1L;


	private Long id;
	private String name;
	private Integer clrSize;

}
