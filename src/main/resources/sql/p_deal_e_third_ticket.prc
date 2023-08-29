CREATE OR REPLACE PROCEDURE p_deal_e_third_ticket(p_brand IN VARCHAR2) AS
    --create by GuoS@20160720
    --处理线上优惠券临时表到贵宾券
    v_ad_client_id NUMBER(10) := 37;
    v_ad_org_id    NUMBER(10) := 27;
    v_userid       NUMBER(10) := 893;
    v_ticket_id    NUMBER(10);
    v_vip_id       NUMBER(10);
    v_datebeg      NUMBER(8);
    v_dateend      NUMBER(8);
    v_deal_msg     VARCHAR2(500);
    v_sqlerr       VARCHAR2(4000);
    v_count        NUMBER(10);
BEGIN
    UPDATE e_third_ticket t
    SET t.deal_code = 1
    WHERE nvl(t.brand, '0') = nvl(p_brand, '0')
    AND t.deal_code IN (0, 3);

    UPDATE e_third_ticketitem t
    SET t.deal_code = 1
    WHERE t.deal_code IN (0, 3)
    AND nvl(t.brand, '0') = nvl(p_brand, '0');

    UPDATE e_third_ticketitem t
    SET t.deal_msg = '商品不存在!', t.deal_code = 3
    WHERE t.deal_code = 1
    AND NOT EXISTS (SELECT 1
           FROM m_product m
           WHERE m.NAME = t.product_code);

    UPDATE e_third_ticketitem t
    SET t.e_third_ticket_id = (SELECT a.id
                                FROM e_third_ticket a
                                WHERE a.ticketno = t.ticketno
                                AND a.creationdate = t.createdate
                                AND a.deal_code = 1
                                AND nvl(a.brand, 0) = nvl(t.brand, 0))
    WHERE nvl(t.brand, '0') = nvl(p_brand, '0')
    AND t.deal_code = 1;

    FOR v IN (SELECT t.*
              FROM e_third_ticket t
              WHERE nvl(t.brand, '0') = nvl(p_brand, '0')
              AND t.deal_code = 1
              ORDER BY t.id) LOOP
        BEGIN
            SAVEPOINT beg;
            --处理开始时间
            BEGIN
                --12小时制
                v_datebeg := to_number(to_char(to_date(v.datebeg,
                                                       'yyyy-mm-dd hh:mi:ss'),
                                               'yyyymmdd'));
            EXCEPTION
                WHEN OTHERS THEN
                    BEGIN
                        --24小时制
                        v_datebeg := to_number(to_char(to_date(v.datebeg,
                                                               'yyyy-mm-dd hh24:mi:ss'),
                                                       'yyyymmdd'));
                    EXCEPTION
                        WHEN OTHERS THEN
                            v_datebeg := to_number(to_char(SYSDATE, 'yyyymmdd'));
                    END;
            END;

            --处理截止时间
            BEGIN
                --12小时制
                v_dateend := to_number(to_char(to_date(v.dateend,
                                                       'yyyy-mm-dd hh:mi:ss'),
                                               'yyyymmdd'));
            EXCEPTION
                WHEN OTHERS THEN
                    BEGIN
                        --24小时制
                        v_dateend := to_number(to_char(to_date(v.dateend,
                                                               'yyyy-mm-dd hh24:mi:ss'),
                                                       'yyyymmdd'));
                    EXCEPTION
                        WHEN OTHERS THEN
                            v_dateend := to_number(to_char(SYSDATE, 'yyyymmdd'));
                    END;
            END;

            SELECT MAX(t.id)
            INTO v_vip_id
            FROM c_client_vip t
            WHERE t.evipid = v.evipid
            AND nvl(t.brand, '0') = nvl(v.brand, '0');

            SELECT MAX(t.id)
            INTO v_ticket_id
            FROM tdefticket t
            WHERE t.ticketno = v.ticketno
            AND nvl(t.brand, '0') = nvl(v.brand, '0');

            IF ad_param_value(37, 'portal.isshopping', 'false') = 'false' THEN

               if nvl(v.type,'to_use') in ('to_use','gift') then
                IF v_ticket_id IS NULL THEN
                    v_deal_msg := '优惠券新增';

                    v_ticket_id := get_sequences('c_v_tdefticket');
                    INSERT INTO tdefticket
                        (id, ad_client_id, ad_org_id, creationdate,
                         modifieddate, ownerid, modifierid, isactive, ticketno,
                         checkno, datebeg, dateend, parvalue, remark, quovalue,
                         c_vip_id, is_fromonline, verifyed, verifydate,
                         verifyremark, brand)
                    VALUES
                        (v_ticket_id, v_ad_client_id, v_ad_org_id, SYSDATE,
                         SYSDATE, v_userid, v_userid, 'Y', v.ticketno,
                         v.ticketno, v_datebeg, v_dateend, v.parvalue, v.remark,
                         v.quovalue, v_vip_id, 1,
                         (CASE WHEN v.status = 1 THEN 'Y' ELSE 'N' END),
                         (CASE WHEN v.status = 1 THEN
                           to_number(to_char(SYSDATE, 'YYYYMMDD')) ELSE NULL END),
                         (CASE WHEN v.status = 1 THEN '线上核销' ELSE NULL END),
                         v.brand);
                ELSE
                    v_deal_msg := '优惠券更新';

                    UPDATE tdefticket t
                    SET t.datebeg = v_datebeg, t.dateend = v_dateend,
                        t.parvalue = v.parvalue, t.quovalue = v.quovalue,
                        t.remark = v.remark, t.c_vip_id = v_vip_id,
                        t.is_fromonline = 1,
                        t.verifyed = (CASE WHEN v.status = 1 THEN 'Y' ELSE 'N' END),
                        t.verifydate = (CASE WHEN v.status = 1 THEN to_number(to_char(SYSDATE, 'YYYYMMDD')) ELSE NULL END),
                        t.verifyremark = (CASE WHEN v.status = 1 THEN '线上已使用' ELSE NULL END),
                        t.is_verifyed_propel = (CASE WHEN v.status = 1 THEN 1 ELSE 0 END)
                    WHERE t.id = v_ticket_id;
                END IF;

                end if;
            ELSE
                --查询购物券线上是否已存在
                SELECT MAX(t.id)
                INTO v_ticket_id
                FROM c_vouchers t
                WHERE t.vouchers_no = v.ticketno
                AND nvl(t.brand, '0') = nvl(v.brand, '0');

                IF v_ticket_id IS NULL THEN
                    v_deal_msg := '购物券新增';
                    v_ticket_id := get_sequences('c_v_tdefticket');
                    INSERT INTO c_vouchers
                        (id, ad_client_id, ad_org_id, ownerid, modifierid,
                         creationdate, modifieddate, isactive, vouchers_no,
                         vou_type, start_date, valid_date, c_vip_id,
                         is_allstore，is_verifyed, verifyed_time, brand, is_valid,
                         is_onetime, is_delaccount, isshare_paytype,
                         is_fromonline, is_exceptdis, is_mdamt, deltype,
                         source_no)
                    VALUES
                        (v_ticket_id, v_ad_client_id, v_ad_org_id, v_userid,
                         v_userid, SYSDATE, SYSDATE, 'Y',
                         v.ticketno || '(' || v.NAME || ')', v.TYPE, v_datebeg,
                         v_dateend, v_vip_id, 'Y',
                         (CASE WHEN v.status = 1 THEN 'Y' ELSE 'N' END),
                         (CASE WHEN v.status = 1 THEN SYSDATE ELSE NULL END),
                         v.brand, 'Y', 'Y', 'Y', 'Y', 1, 'Y', 'N', 'AND',
                         v.ticketno);

                    --不能使用购物券明细
                    INSERT INTO c_v_vouchers_unuseitem
                        (id, ad_client_id, ad_org_id, ownerid, modifierid,
                         creationdate, modifieddate, c_v_vouchers_id, distype,
                         discount, relatetype, amttype, amt)
                    VALUES
                        (get_sequences('C_V_VOUCHERS_UNUSEITEM'),
                         v_ad_client_id, v_ad_org_id, v_userid, v_userid,
                         SYSDATE, SYSDATE, v_ticket_id, 'L', v.limit_discount,
                         'OR', 'L', v.limit_price);
                    --为当购物券为礼品券时带商品明细
                    IF v.TYPE = 'gift' THEN
                        SELECT COUNT(1)
                        INTO v_count
                        FROM e_third_ticketitem t
                        WHERE t.e_third_ticket_id = v.id;
                        IF v_count > 0 THEN
                            FOR i IN (SELECT *
                                      FROM e_third_ticketitem t
                                      WHERE t.e_third_ticket_id = v.id) LOOP
                                INSERT INTO c_voudislimit
                                    (id, ad_client_id, ad_org_id, ownerid,
                                     modifierid, creationdate, modifieddate,
                                     c_vouchers_id, m_product_id)
                                VALUES
                                    (get_sequences('C_VOUDISLIMIT'),
                                     v_ad_client_id, v_ad_org_id, v_userid,
                                     v_userid, SYSDATE, SYSDATE, v_ticket_id,
                                     (SELECT a.id
                                       FROM m_product a
                                       WHERE a.NAME = i.product_code));
                                UPDATE e_third_ticketitem t
                                SET t.deal_msg = '商品明细新增成功!', t.deal_code = 2
                                WHERE t.id = i.id;
                            END LOOP;
                            --更新礼品券金额，礼品券类型（线下对应优惠券）
                            UPDATE c_vouchers t
                            SET t.amt_discount = (SELECT MAX(b.precost)
                                                   FROM c_voudislimit a,
                                                        m_product b
                                                   WHERE a.m_product_id = b.id
                                                   AND a.c_vouchers_id =
                                                         v_ticket_id),
                                t.vou_type = 'VOU5'
                            WHERE t.id = v_ticket_id;
                        ELSE
                            --无明细则给礼品券一个较大的金额，且限制只能兑换一件商品
                            UPDATE c_vouchers t
                            SET t.amt_discount = 9999, t.vou_type = 'VOU5',
                                t.qty_limit = 1
                            WHERE t.id = v_ticket_id;
                        END IF;
                    ELSIF v.TYPE = 'to_use' THEN
                        --更新购物券可使用余额,购物券类型(线下对应现金券)
                        UPDATE c_vouchers t
                        SET t.amt_acount = v.parvalue, t.vou_type = 'VOU6'
                        WHERE t.id = v_ticket_id;
                    ELSE
                        --更新折扣，购物券类型(线下对应折扣券)
                        UPDATE c_vouchers t
                        SET t.vou_dis = v.parvalue / 100, t.vou_type = 'VOU4'
                        WHERE t.id = v_ticket_id;
                    END IF;
                ELSE
                    v_deal_msg := '购物券更新';
                    IF v.TYPE = 'gift' THEN
                        UPDATE c_vouchers t
                        SET t.start_date = v_datebeg, t.valid_date = v_dateend,
                            t.amt_discount = (SELECT MAX(b.precost)
                                               FROM c_voudislimit a, m_product b
                                               WHERE a.m_product_id = b.id
                                               AND a.c_vouchers_id = v_ticket_id),
                            t.c_vip_id = v_vip_id, t.is_fromonline = 1,
                            t.is_verifyed = (CASE WHEN v.status = 1 THEN 'Y' ELSE 'N' END),
                            t.verifyed_time = (CASE WHEN v.status = 1 THEN SYSDATE ELSE NULL END),
                            t.is_verifyed_propel = (CASE WHEN v.status = 1 THEN 1 ELSE 0 END)
                        WHERE t.id = v_ticket_id;
                    ELSIF v.TYPE = 'to_use' THEN
                        UPDATE c_vouchers t
                        SET t.start_date = v_datebeg, t.valid_date = v_dateend,
                            t.amt_acount = v.parvalue, t.c_vip_id = v_vip_id,
                            t.is_fromonline = 1,
                            t.is_verifyed = (CASE WHEN v.status = 1 THEN 'Y' ELSE 'N' END),
                            t.verifyed_time = (CASE WHEN v.status = 1 THEN SYSDATE ELSE NULL END),
                            t.is_verifyed_propel = (CASE WHEN v.status = 1 THEN 1 ELSE 0 END)
                        WHERE t.id = v_ticket_id;
                    ELSE
                        UPDATE c_vouchers t
                        SET t.start_date = v_datebeg, t.valid_date = v_dateend,
                            t.vou_dis = v.parvalue / 100, t.c_vip_id = v_vip_id,
                            t.is_fromonline = 1,
                            t.is_verifyed = (CASE WHEN v.status = 1 THEN 'Y' ELSE 'N' END),
                            t.verifyed_time = (CASE WHEN v.status = 1 THEN SYSDATE ELSE NULL END),
                            t.is_verifyed_propel = (CASE WHEN v.status = 1 THEN 1 ELSE 0 END)
                        WHERE t.id = v_ticket_id;
                    END IF;
                END IF;
            END IF;

            UPDATE e_third_ticket t
            SET t.deal_code = 2, t.deal_msg = v_deal_msg || '成功'
            WHERE t.id = v.id;

            COMMIT;

        EXCEPTION
            WHEN OTHERS THEN
                ROLLBACK TO SAVEPOINT beg;
                v_sqlerr := SQLERRM;

                UPDATE e_third_ticket t
                SET t.deal_code = 3,
                    t.deal_msg = v_deal_msg || '失败 ' || v_sqlerr
                WHERE t.id = v.id;
        END;
    END LOOP;

    --将已做同步操作的数据写入历史表中，并将中间表已同步数据删除。
    INSERT INTO e_third_ticket_his
        (id, creationdate, ticketno, parvalue, datebeg, dateend, remark,
         quovalue, status, giventime, SOURCE, TYPE, category, NAME, verifystore,
         verifytime, verifyremark, eticketid, evipid, deal_code, deal_msg, brand,
         limit_discount, limit_price)
        SELECT id, creationdate, ticketno, parvalue, datebeg, dateend, remark,
               quovalue, status, giventime, SOURCE, TYPE, category, NAME,
               verifystore, verifytime, verifyremark, eticketid, evipid,
               deal_code, deal_msg, brand, t.limit_discount, t.limit_price
        FROM e_third_ticket t
        WHERE nvl(t.brand, '0') = nvl(p_brand, '0')
        AND t.deal_code = 2;

    INSERT INTO e_third_ticketitem_his
        (id, product_code, remark, e_third_ticket_id, ticketno, createdate,
         deal_msg, deal_code, brand)
        SELECT t.id, t.product_code, t.remark, t.e_third_ticket_id, t.ticketno,
               t.createdate, t.deal_msg, t.deal_code, t.brand
        FROM e_third_ticketitem t
        WHERE nvl(t.brand, '0') = nvl(p_brand, '0')
        AND t.deal_code = 2;

    DELETE FROM e_third_ticket t
    WHERE nvl(t.brand, '0') = nvl(p_brand, '0')
    AND t.deal_code = 2;

    DELETE FROM e_third_ticketitem t
    WHERE nvl(t.brand, '0') = nvl(p_brand, '0')
    AND t.deal_code = 2;

END;
