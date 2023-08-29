CREATE OR REPLACE TRIGGER "BI_M_OUT_MSCODEITEM"
INSTEAD OF INSERT  OR DELETE ON m_out_mscodeitem
 REFERENCES OLD AS OLD NEW AS NEW
 FOR EACH ROW

--add by xusc 2012-4-1  按唯一码扫描
--History:
    -- 1. Date:2013-06-18
    -- Author:tdd
    -- Modification: 新增逻辑判断：若销售单or调拨单的是否按箱为Y，则不允许新增或修改出库单明细
	-- 2. Date:2013-07-04
    -- Author:tdd
    -- Modification:增加控制：如果出库单的拆分装箱为2，不允许新增、修改明细。
    -- 3. Date:2016-10-10
    -- Author:XKK
    -- Modification:修改逻辑：
    --如果为删除，通过明细的id删除数据，不能通过唯一码，因为唯一码可以为空(原逻辑是通过唯一码删除)。
    --如果为新增，则修改后的逻辑为：
    --判断输入的[唯一码/条码]是否为条码，如果为条码则：
    --增加控制：如果输入的条码对应的款号的[是否使用唯一码]为Y，则提示：款号：XXXX必须使用唯一码，请确认。
    --如果输入的条码已经存在，则将数量合并到已经存在的记录上。
    --如果输入的条码不存在，则新增一条记录：
    --唯一码/条码为空。
    --条码、商品、ASI为该条码对应的值。
    --数量为当前数量。
    --如果输入的[唯一码/条码]不是条码，断[唯一码/条码]输入的是否为唯一码（根据系统参数[portal.6001]截取后，如果可以对应到我们的条码，则认为输入的为唯一码），如果为唯一码，则：
    --增加控制：如果输入的唯一码对应的条码所属的款号的[是否使用唯一码]为N，则提示：款号：XXXX不使用唯一码，请确认。
    --增加控制：如果数量不为1或-1，不允许。
    --增加控制：如果输入的唯一码已经存在，不允许。
    --新增一条记录：
    --唯一码/条码为当前值。
    --条码、商品、ASI为该条码对应的值。
    --数量为当前数量。
    --如果[唯一码/条码]输入的不是条码也不是唯一码，则提示：输入的唯一码/条码有误，请重新输入。
    --如果为更新，则：
    --如果[唯一码/条码]不为空，修改后的数量不为1或-1，不允许。
    --如果[唯一码/条码]为空，则允许修改数量。
    --增加控制：[唯一码/条码]为空，修改后的数量小于0，不允许。
    --4.Author:XKK
    --Date:20170228
    --Modification:出库单中唯一码明细界面，使用portal.6001参数支持多种唯一码截取位数

DECLARE
		v_id                     NUMBER(10);
		v_billtype               VARCHAR2(80);
		v_count                  NUMBER(10);
		v_cnt                    NUMBER(10);
		v_productid              NUMBER(10);
		v_attributesetinstanceid NUMBER(10);
		v_mastercode             VARCHAR2(255) := :NEW.mastercode;
		v_no                     VARCHAR2(255);
		v_pdaid                  NUMBER(10);
		v_flownum                NUMBER(10);
		v_is_bas                 m_out.is_bas%TYPE; --是否按箱

BEGIN

		/*SELECT substr(:NEW.m_out_id, 1, length(:NEW.m_out_id) - 2),
           chr(substr(:NEW.m_out_id, -2))
    INTO v_id, v_dype
    FROM dual;

    SELECT decode(v_dype, 'A', 'M_SALEOUT', 'B', 'M_RET_SALEOUT', 'C',
                   'M_TRANSFEROUT', 'D', 'M_V_CUSRET_PUROUT', 'E', 'M_RET_PUROUT')
    INTO v_billtype1
    FROM dual;*/

		--删除明细
		IF deleting THEN
				SELECT substr(:OLD.m_out_id, 1, length(:OLD.m_out_id) - 2),
							 chr(substr(:OLD.m_out_id, -2))
					INTO v_id, v_billtype
					FROM dual;

				--获取对应出库单的是否按箱
				SELECT m.is_bas INTO v_is_bas FROM m_out m WHERE m.id = :OLD.m_out_id;

				--add by tdd  2013-06-18
				--新增逻辑判断：若销售单or调拨单的是否按箱为Y，则不允许新增或修改出库单明细
				IF v_is_bas = 'Y' AND v_billtype IN ('A', 'C', 'E') THEN
						raise_application_error(-20201,
																		'该出库单的是否按箱为是，不允许新增、修改、删除明细！');
				END IF;
				--end tdd 2013-06-18

				CASE
						WHEN v_billtype = 'A' THEN
								DELETE FROM m_sale_mscodeitem t
								 WHERE t.m_sale_id = v_id
									 AND t.mastercode = :OLD.mastercode;
						WHEN v_billtype = 'B' THEN
								DELETE FROM m_retsale_mscodeitem t
								 WHERE t.m_ret_sale_id = v_id
									 AND t.mastercode = :OLD.mastercode;
						WHEN v_billtype = 'C' THEN
								DELETE FROM m_tran_mscodeitem t
								 WHERE t.m_transfer_id = v_id
									 AND t.mastercode = :OLD.mastercode;
						WHEN v_billtype = 'D' THEN
								DELETE FROM m_retsale_mscodeitem t
								 WHERE t.m_ret_sale_id = v_id
									 AND t.mastercode = :OLD.mastercode;
						WHEN v_billtype = 'E' THEN
								DELETE FROM m_retpur_mscodeitem t
								 WHERE t.m_ret_pur_id = v_id
									 AND t.mastercode = :OLD.mastercode;
				END CASE;

		END IF;
		IF inserting THEN
				SELECT substr(:NEW.m_out_id, 1, length(:NEW.m_out_id) - 2),
							 chr(substr(:NEW.m_out_id, -2))
					INTO v_id, v_billtype
					FROM dual;

				--获取对应出库单的是否按箱
				SELECT m.is_bas INTO v_is_bas FROM m_out m WHERE m.id = :NEW.m_out_id;

				--add by tdd  2013-06-18
				--新增逻辑判断：若销售单or调拨单的是否按箱为Y，则不允许新增或修改出库单明细
				IF v_is_bas = 'Y' AND v_billtype IN ('A', 'C', 'E') THEN
						raise_application_error(-20201,
																		'该出库单的是否按箱为是，不允许新增、修改、删除明细！');
				END IF;
				--end tdd 2013-06-18
				--输入的数据是否存在
				SELECT COUNT(1)
					INTO v_count
					FROM m_out_mscodeitem
				 WHERE mastercode = v_mastercode
					 AND m_out_id = :NEW.m_out_id;

				IF v_count <> 0 THEN
						raise_application_error(-20202,
																		'输入的唯一码在本单已存在：' || v_mastercode || '！');
				END IF;

				--第一次输入时候判断
				SELECT COUNT(*)
					INTO v_count
					FROM m_out_mscodeitem
				 WHERE m_out_id = :NEW.m_out_id;

				IF v_count = 0 THEN
						--判断出库单明细中是否存在出库数量大于0
						SELECT COUNT(1)
							INTO v_cnt
							FROM m_outitem
						 WHERE m_out_id = :NEW.m_out_id
							 AND qtyout <> 0;

						IF v_cnt <> 0 THEN
								raise_application_error(-20201,
																				'存在出库单明细出库数量不为0，不能新增唯一码扫描记录！');
						END IF;
				END IF;

				--获取当前系统使用唯一码流水位数
				SELECT t.VALUE INTO v_flownum FROM ad_param t WHERE t.NAME = 'portal.6001';

				--验证唯一码是否合法
				SELECT COUNT(1)
					INTO v_cnt
					FROM m_product_alias t
				 WHERE t.no = substr(v_mastercode, 1, length(v_mastercode) - v_flownum);

				IF v_cnt = 0 THEN
						raise_application_error(-20001, '无法识别唯一码：' || v_mastercode || '！');
				END IF;

				SELECT t.id, t.no, t.m_product_id, t.m_attributesetinstance_id
					INTO v_pdaid, v_no, v_productid, v_attributesetinstanceid
					FROM m_product_alias t
				 WHERE t.no = substr(v_mastercode, 1, length(v_mastercode) - v_flownum);

				CASE

						WHEN v_billtype = 'A' THEN
								SELECT COUNT(*)
									INTO v_count
									FROM m_saleitem t
								 WHERE t.m_product_id = v_productid
									 AND t.m_attributesetinstance_id = v_attributesetinstanceid
									 AND t.m_sale_id = v_id;

								IF v_count = 0 THEN
										raise_application_error(-20001,
																						'条码：' || v_no || ',在单据明细里不存在！');
								END IF;

								INSERT INTO m_sale_mscodeitem
										(id, ad_client_id, ad_org_id, m_sale_id, mastercode,
										 m_productalias_id, m_product_id, m_attributesetinstance_id, ownerid,
										 modifierid, creationdate, modifieddate, isactive)
										SELECT get_sequences('m_sale_mscodeitem'), t.ad_client_id, t.ad_org_id,
													 v_id, v_mastercode, v_pdaid, v_productid,
													 v_attributesetinstanceid, :NEW.ownerid, :NEW.modifierid,
													 SYSDATE, SYSDATE, 'Y'
											FROM m_sale t
										 WHERE rownum = 1;

						WHEN v_billtype = 'B' THEN

								SELECT COUNT(*)
									INTO v_count
									FROM m_ret_saleitem t
								 WHERE t.m_product_id = v_productid
									 AND t.m_attributesetinstance_id = v_attributesetinstanceid
									 AND t.m_ret_sale_id = v_id;

								IF v_count = 0 THEN
										raise_application_error(-20001,
																						'条码：' || v_no || ',在单据明细里不存在！');
								END IF;

								INSERT INTO m_retsale_mscodeitem
										(id, ad_client_id, ad_org_id, m_ret_sale_id, mastercode,
										 m_productalias_id, m_product_id, m_attributesetinstance_id, ownerid,
										 modifierid, creationdate, modifieddate, isactive)
										SELECT get_sequences('m_retsale_mscodeitem'), t.ad_client_id,
													 t.ad_org_id, v_id, v_mastercode, v_pdaid, v_productid,
													 v_attributesetinstanceid, :NEW.ownerid, :NEW.modifierid,
													 SYSDATE, SYSDATE, 'Y'
											FROM m_ret_sale t
										 WHERE rownum = 1;
						WHEN v_billtype = 'C' THEN

								SELECT COUNT(*)
									INTO v_count
									FROM m_transferitem t
								 WHERE t.m_product_id = v_productid
									 AND t.m_attributesetinstance_id = v_attributesetinstanceid
									 AND t.m_transfer_id = v_id;

								IF v_count = 0 THEN
										raise_application_error(-20001,
																						'条码：' || v_no || ',在单据明细里不存在！');
								END IF;

								INSERT INTO m_tran_mscodeitem
										(id, ad_client_id, ad_org_id, m_transfer_id, mastercode,
										 m_productalias_id, m_product_id, m_attributesetinstance_id, ownerid,
										 modifierid, creationdate, modifieddate, isactive)
										SELECT get_sequences('m_tran_mscodeitem'), t.ad_client_id, t.ad_org_id,
													 v_id, v_mastercode, v_pdaid, v_productid,
													 v_attributesetinstanceid, :NEW.ownerid, :NEW.modifierid,
													 SYSDATE, SYSDATE, 'Y'
											FROM m_transfer t
										 WHERE rownum = 1;
						WHEN v_billtype = 'D' THEN

								SELECT COUNT(*)
									INTO v_count
									FROM m_ret_saleitem t
								 WHERE t.m_product_id = v_productid
									 AND t.m_attributesetinstance_id = v_attributesetinstanceid
									 AND t.m_ret_sale_id = v_id;

								IF v_count = 0 THEN
										raise_application_error(-20001,
																						'条码：' || v_no || ',在单据明细里不存在！');
								END IF;

								INSERT INTO m_retsale_mscodeitem
										(id, ad_client_id, ad_org_id, m_ret_sale_id, mastercode,
										 m_productalias_id, m_product_id, m_attributesetinstance_id, ownerid,
										 modifierid, creationdate, modifieddate, isactive)
										SELECT get_sequences('m_retsale_mscodeitem'), t.ad_client_id,
													 t.ad_org_id, v_id, v_mastercode, v_pdaid, v_productid,
													 v_attributesetinstanceid, :NEW.ownerid, :NEW.modifierid,
													 SYSDATE, SYSDATE, 'Y'
											FROM m_ret_sale t
										 WHERE rownum = 1;
						WHEN v_billtype = 'E' THEN

								SELECT COUNT(*)
									INTO v_count
									FROM m_ret_puritem t
								 WHERE t.m_product_id = v_productid
									 AND t.m_attributesetinstance_id = v_attributesetinstanceid
									 AND t.m_ret_pur_id = v_id;

								IF v_count = 0 THEN
										raise_application_error(-20001,
																						'条码：' || v_no || ',在单据明细里不存在！');
								END IF;

								INSERT INTO m_retpur_mscodeitem
										(id, ad_client_id, ad_org_id, m_ret_pur_id, mastercode,
										 m_productalias_id, m_product_id, m_attributesetinstance_id, ownerid,
										 modifierid, creationdate, modifieddate, isactive)
										SELECT get_sequences('m_retpur_mscodeitem'), t.ad_client_id,
													 t.ad_org_id, v_id, v_mastercode, v_pdaid, v_productid,
													 v_attributesetinstanceid, :NEW.ownerid, :NEW.modifierid,
													 SYSDATE, SYSDATE, 'Y'
											FROM m_ret_pur t
										 WHERE rownum = 1;

				END CASE;

		END IF;
END;
/
