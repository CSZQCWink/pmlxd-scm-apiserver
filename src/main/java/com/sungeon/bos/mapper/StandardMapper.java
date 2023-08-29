package com.sungeon.bos.mapper;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.util.SystemProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author 刘国帅
 * @date 2022-6-17
 **/
@Mapper
public interface StandardMapper {

    /**
     * 查询
     */
    @Select({"${sql}"})
    JSONObject doSelect(@Param("sql") String sql);

    @Select({"${sql}"})
    List<LinkedHashMap<String, Object>> doSelectList(@Param("sql") String sql);

    @SelectProvider(type = SqlProvider.class, method = "select")
    JSONObject select(@Param("tableName") String tableName, @Param("columns") String columns,
                      @Param("condition") JSONObject condition);

    @SelectProvider(type = SqlProvider.class, method = "select")
    List<LinkedHashMap<String, Object>> selectList(@Param("tableName") String tableName, @Param("columns") String columns,
                                                   @Param("condition") JSONObject condition);

    @Select({"SELECT * FROM ${tableName} WHERE id = #{id}"})
    JSONObject selectById(@Param("tableName") String tableName, @Param("id") long id);

    @Select({"SELECT ${columns} FROM ${tableName} WHERE id = #{id}"})
    JSONObject selectColumnsById(@Param("tableName") String tableName, @Param("columns") String columns,
                                 @Param("id") long id);

    @Select({"SELECT ID FROM ${tableName} WHERE ${columnName} = #{columnValue} AND rownum = 1"})
    Long selectIdByColumn(@Param("tableName") String tableName, @Param("columnName") String columnName,
                          @Param("columnValue") Object columnValue);

    /**
     * 新增
     */
    @Insert({"${sql}"})
    int doInsert(@Param("sql") String sql);

    @InsertProvider(type = SqlProvider.class, method = "insert")
    int insert(@Param("tableName") String tableName, @Param("model") JSONObject model);

    @InsertProvider(type = SqlProvider.class, method = "insertBatch")
    int insertBatch(@Param("tableName") String tableName, @Param("models") Collection<JSONObject> models);

    /**
     * 更新
     */
    @Update({"${sql}"})
    int doUpdate(@Param("sql") String sql);

    @UpdateProvider(type = SqlProvider.class, method = "update")
    int update(@Param("tableName") String tableName, @Param("model") JSONObject model, @Param("condition") JSONObject condition);

    @UpdateProvider(type = SqlProvider.class, method = "updateById")
    int updateById(@Param("tableName") String tableName, @Param("model") JSONObject model, @Param("id") long id);

    @UpdateProvider(type = SqlProvider.class, method = "updateBatch")
    int updateBatch(@Param("tableName") String tableName, @Param("models") Collection<JSONObject> models,
                    @Param("condition") String[] condition);

    /**
     * 删除
     */
    @Delete({"${sql}"})
    int doDelete(@Param("sql") String sql);

    @UpdateProvider(type = SqlProvider.class, method = "delete")
    int delete(@Param("tableName") String tableName, @Param("condition") JSONObject condition);

    @Delete({"DELETE FROM ${tableName} WHERE id = #{id}"})
    int deleteById(@Param("tableName") String tableName, @Param("id") long id);

    @DeleteProvider(type = SqlProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("tableName") String tableName, @Param("ids") long[] ids);


    @Select({"SELECT count(1) FROM ${tableName} WHERE ${columnName} = #{columnValue} AND rownum = 1"})
    boolean exists(@Param("tableName") String tableName, @Param("columnName") String columnName,
                   @Param("columnValue") Object columnValue);

    @Select({"SELECT count(1) FROM ${tableName} WHERE id = #{id} AND rownum = 1"})
    boolean existsById(@Param("tableName") String tableName, @Param("id") long id);

    @Select({"SELECT count(1) FROM ${tableName} WHERE id <> #{id} AND ${columnName} = #{value} AND rownum = 1"})
    boolean existsWithoutId(@Param("tableName") String tableName, @Param("id") long id,
                            @Param("columnName") String columnName, @Param("columnValue") Object columnValue);

    @Select("SELECT AD_PARAM_VALUE(37, #{name}, #{defaultValue, jdbcType=VARCHAR}) FROM dual")
    String getParam(@Param("name") String name, @Param("defaultValue") String defaultValue);

    @Options(useCache = false, flushCache = Options.FlushCachePolicy.TRUE)
    @Select({"SELECT get_sequences(#{tableName}) FROM dual"})
    Long getSequences(@Param("tableName") String tableName);

    @Options(useCache = false, flushCache = Options.FlushCachePolicy.TRUE)
    @Select({"SELECT get_sequenceno(#{sequenceHead}, #{clientId}) FROM dual"})
    String getSequenceNo(@Param("sequenceHead") String sequenceHead, @Param("clientId") int clientId);


    @SelectProvider(type = SqlProvider.class, method = "callProcedure")
    @Options(statementType = StatementType.CALLABLE)
    void callProcedure(@Param("procedureName") String procedureName, @Param("params") Map<String, Object> params,
                       @Param("hasOut") boolean hasOut);

    @SelectProvider(type = SqlProvider.class, method = "callFunction")
    @Options(statementType = StatementType.CALLABLE)
    void callFunction(@Param("functionName") String functionName, @Param("params") Map<String, Object> params);

    public static class SqlProvider {

        static final String[] READONLY_COLUMNS = new String[]{"ID", "AD_CLIENT_ID", "AD_ORG_ID", "OWNERID", "CREATIONDATE"};
        static final String[] ADD_DEFAULT_COLUMNS = new String[]{"ID", "AD_CLIENT_ID", "AD_ORG_ID", "OWNERID", "MODIFIERID", "CREATIONDATE", "MODIFIEDDATE", "ISACTIVE"};

        public SqlProvider() {
        }

        private static boolean isReadOnly(String column) {
            for (String readonlyColumn : READONLY_COLUMNS) {
                if (readonlyColumn.equals(column)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isAddDefault(String column) {
            for (String addColumn : ADD_DEFAULT_COLUMNS) {
                if (addColumn.equals(column)) {
                    return true;
                }
            }
            return false;
        }

        private static void keyUpper(JSONObject json) {
            String[] keys = new String[json.size()];
            json.keySet().toArray(keys);
            String[] cloneKeys = keys;

            for (int i = 0; i < keys.length; ++i) {
                String key = cloneKeys[i];
                if (!key.equals(key.toUpperCase())) {
                    json.put(key.toUpperCase(), json.get(key));
                    json.remove(key);
                }
            }
        }

        public String select(Map<String, Object> para) {
            String tableName = (String) para.get("tableName");
            String columns = (String) para.get("columns");
            JSONObject condition = (JSONObject) para.get("condition");
            SQL sql = new SQL();
            if (StringUtils.isBlank(tableName)) {
                throw new IllegalArgumentException("tableName 无效");
            }
            sql.SELECT(columns);
            sql.FROM(tableName);
            if (!CollectionUtils.isEmpty(condition)) {
                keyUpper(condition);
                for (String key : condition.keySet()) {
                    if (condition.get(key) == null || "IS NULL".equals(condition.get(key).toString())) {
                        sql.WHERE(key + " IS NULL");
                    } else if ("IS NOT NULL".equals(condition.get(key).toString())) {
                        sql.WHERE(key + " IS NOT NULL");
                    } else {
                        sql.WHERE(key + " = #{condition." + key + "}");
                    }
                }
            } else {
                throw new IllegalArgumentException("condition 无效");
            }
            return sql.toString();
        }

        public String insert(Map<String, Object> para) {
            String tableName = (String) para.get("tableName");
            JSONObject model = (JSONObject) para.get("model");
            if (StringUtils.isBlank(tableName)) {
                throw new IllegalArgumentException("tableName 无效");
            } else if (CollectionUtils.isEmpty(model)) {
                throw new IllegalArgumentException("model 无效");
            } else {
                SQL sql = new SQL();
                keyUpper(model);
                Set<String> keySet = model.keySet();
                sql.INSERT_INTO(tableName);

                boolean exist = keySet.stream().anyMatch(SqlProvider::isAddDefault);
                if (!exist) {
                    sql.VALUES("ID", "get_sequences('" + tableName + "')");
                    sql.VALUES("AD_CLIENT_ID", "37");
                    sql.VALUES("AD_ORG_ID", "27");
                    sql.VALUES("OWNERID", SystemProperties.ParamDefaultUserId + "");
                    sql.VALUES("MODIFIERID", SystemProperties.ParamDefaultUserId + "");
                    sql.VALUES("CREATIONDATE", "SYSDATE");
                    sql.VALUES("MODIFIEDDATE", "SYSDATE");
                    sql.VALUES("ISACTIVE", "'Y'");
                }

                for (int i = 0; i < model.size(); ++i) {
                    String key = (String) keySet.toArray()[i];
                    sql.VALUES(key, "#{model." + key + "}");
                }
                return sql.toString();
            }
        }

        public String insertBatch(Map<String, Object> para) {
            String tableName = (String) para.get("tableName");
            Collection<JSONObject> models = (Collection<JSONObject>) para.get("models");
            if (StringUtils.isBlank(tableName)) {
                throw new IllegalArgumentException("tableName 无效");
            } else if (!CollectionUtils.isEmpty(models)) {
                Set<String> columns = new HashSet<>();

                for (JSONObject model : models) {
                    if (model == null) {
                        model = new JSONObject();
                    }

                    keyUpper(model);
                    boolean exist = model.keySet().stream().anyMatch(SqlProvider::isAddDefault);
                    if (!exist) {
                        columns.addAll(Arrays.asList(ADD_DEFAULT_COLUMNS));
                    }
                    columns.addAll(model.keySet());
                }

                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO ");
                sql.append(tableName);
                sql.append(" (");
                sql.append(String.join(", ", columns));
                sql.append(") ");

                int i = 0;
                for (JSONObject model : models) {
                    sql.append("SELECT ");

                    for (Iterator<String> iterator = columns.iterator(); iterator.hasNext(); sql.append(", ")) {
                        String column = iterator.next();
                        if (model.containsKey(column)) {
                            sql.append(String.format("#{models[%d].%s}", i, column));
                        } else if (Arrays.asList(ADD_DEFAULT_COLUMNS).contains(column)) {
                            if ("ID".equals(column)) {
                                sql.append("get_sequences('").append(tableName).append("')");
                            } else if ("AD_CLIENT_ID".equals(column)) {
                                sql.append("37");
                            } else if ("AD_ORG_ID".equals(column)) {
                                sql.append("27");
                            } else if ("OWNERID".equals(column)) {
                                sql.append(SystemProperties.ParamDefaultUserId);
                            } else if ("MODIFIERID".equals(column)) {
                                sql.append(SystemProperties.ParamDefaultUserId);
                            } else if ("CREATIONDATE".equals(column)) {
                                sql.append("SYSDATE");
                            } else if ("MODIFIEDDATE".equals(column)) {
                                sql.append("SYSDATE");
                            } else if ("ISACTIVE".equals(column)) {
                                sql.append("'Y'");
                            }
                        } else {
                            sql.append("default");
                        }
                    }
                    sql.delete(sql.length() - 2, sql.length() - 1);
                    sql.append(" FROM dual UNION ALL ");
                    i++;
                }

                sql.delete(sql.length() - 11, sql.length() - 1);
                return sql.toString();
            } else {
                throw new IllegalArgumentException("model 无效");
            }
        }

        public String update(Map<String, Object> para) {
            String tableName = (String) para.get("tableName");
            JSONObject model = (JSONObject) para.get("model");
            JSONObject condition = (JSONObject) para.get("condition");
            SQL sql = new SQL();
            if (StringUtils.isBlank(tableName)) {
                throw new IllegalArgumentException("tableName 无效");
            }
            if (!CollectionUtils.isEmpty(model)) {
                keyUpper(model);
                sql.UPDATE(tableName);
                for (String key : model.keySet()) {
                    if (model.get(key) == null) {
                        sql.SET(key + " = null ");
                    } else {
                        if (model.get(key) instanceof Date) {
                            model.put(key, new Timestamp(((Date) model.get(key)).getTime()));
                        }

                        if (!isReadOnly(key)) {
                            sql.SET(key + "= #{model." + key + "}");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("model 无效");
            }
            if (!CollectionUtils.isEmpty(condition)) {
                keyUpper(condition);
                for (String key : condition.keySet()) {
                    if (condition.get(key) == null || "IS NULL".equals(condition.get(key).toString())) {
                        sql.WHERE(key + " IS NULL");
                    } else if ("IS NOT NULL".equals(condition.get(key).toString())) {
                        sql.WHERE(key + " IS NOT NULL");
                    } else {
                        sql.WHERE(key + " = #{condition." + key + "}");
                    }
                }
            } else {
                throw new IllegalArgumentException("conditions 无效");
            }
            return sql.toString();
        }

        public String updateById(Map<String, Object> para) {
            String tableName = (String) para.get("tableName");
            JSONObject model = (JSONObject) para.get("model");
            // long id = Long.parseLong(para.get("id").toString());
            if (StringUtils.isBlank(tableName)) {
                throw new IllegalArgumentException("tableName 无效");
            } else if (!CollectionUtils.isEmpty(model)) {
                keyUpper(model);
                SQL sql = new SQL();
                sql.UPDATE(tableName);

                for (String key : model.keySet()) {
                    if (!isReadOnly(key)) {
                        if (model.get(key) instanceof Date) {
                            model.put(key, new Timestamp(((Date) model.get(key)).getTime()));
                        }
                        if (model.get(key) != null) {
                            sql.SET(key + " = #{model." + key + "}");
                        } else {
                            sql.SET(key + " = null ");
                        }
                    }
                }

                sql.WHERE(" ID = #{id} ");
                return sql.toString();
            } else {
                throw new IllegalArgumentException("model 无效");
            }
        }

        public String updateBatch(Map<String, Object> para) {
            String tableName = (String) para.get("tableName");
            Collection<JSONObject> models = (Collection<JSONObject>) para.get("models");
            String[] condition = (String[]) para.get("condition");
            if (StringUtils.isBlank(tableName)) {
                throw new IllegalArgumentException("tableName 无效");
            } else if (!CollectionUtils.isEmpty(models)) {
                if (condition.length == 0) {
                    throw new IllegalArgumentException("condition 无效");
                }
                JSONObject columnAll = new JSONObject(true);
                for (JSONObject model : models) {
                    if (model == null) {
                        model = new JSONObject(true);
                    }
                    keyUpper(model);
                    columnAll.putAll(model);
                }
                List<String> conditions = new ArrayList<>();
                for (String con : condition) {
                    conditions.add(con.toUpperCase());
                }

                JSONObject cloneKey = columnAll.clone();
                Set<String> keys = columnAll.keySet();

                for (String column : keys) {
                    if (isReadOnly(column)) {
                        cloneKey.remove(column);
                    }
                }

                Set<String> firstKeys = cloneKey.keySet();
                StringBuffer sql = new StringBuffer();
                sql.append("MERGE INTO ").append(tableName).append(" a USING (");
                int i = -1;
                Iterator<JSONObject> jsonObjectIterator = models.iterator();
                Iterator<String> iterator;
                while (jsonObjectIterator.hasNext()) {
                    JSONObject model = jsonObjectIterator.next();
                    ++i;
                    String key;
                    sql.append(" SELECT ");
                    iterator = firstKeys.iterator();

                    while (iterator.hasNext()) {
                        key = iterator.next();
                        model.putIfAbsent(key, null);

                        sql.append(String.format("#{models[%d].%s} AS %s", i, key, key)).append(",");
                    }

                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(" FROM dual");
                    if (i < models.size() - 1) {
                        sql.append(" UNION ALL");
                    }
                }

                sql.append(") b ON ( ");
                for (String con : conditions) {
                    sql.append(" a.").append(con).append(" = b.").append(con);
                }
                sql.append(") WHEN MATCHED THEN UPDATE SET ");
                iterator = firstKeys.iterator();

                while (iterator.hasNext()) {
                    String value = iterator.next();
                    if (!conditions.contains(value)) {
                        sql.append(" a.").append(value).append(" = ");
                        sql.append(" nvl(b.").append(value).append(", ").append("a.").append(value).append(")").append(",");
                    }
                }

                sql.deleteCharAt(sql.length() - 1);
                System.out.println(sql);
                return sql.toString();
            } else {
                throw new IllegalArgumentException("models 无效");
            }
        }

        public String deleteByIds(Map<String, Object> para) {
            String tableName = (String) para.get("tableName");
            long[] ids = (long[]) para.get("ids");
            if (StringUtils.isBlank(tableName)) {
                throw new IllegalArgumentException("tableName 无效");
            } else if (ids == null) {
                throw new IllegalArgumentException("ids 无效");
            } else if (ids.length == 0) {
                return "";
            } else if (ids.length == 1) {
                return "DELETE FROM " + tableName + " WHERE ID = #{ids[0]}";
            } else {
                StringBuilder sql = new StringBuilder();
                sql.append("DELETE FROM ");
                sql.append(tableName);
                sql.append(" WHERE ");
                sql.append("ID in (");

                for (int i = 0; i < ids.length; ++i) {
                    sql.append(String.format("#{ids[%d]}", i));
                    sql.append(",");
                }

                sql.setCharAt(sql.length() - 1, ')');
                return sql.toString();
            }
        }

        public String delete(Map<String, Object> para) {
            String tableName = (String) para.get("tableName");
            JSONObject condition = (JSONObject) para.get("condition");
            if (StringUtils.isBlank(tableName)) {
                throw new IllegalArgumentException("tableName 无效");
            } else if (!CollectionUtils.isEmpty(condition)) {
                keyUpper(condition);
                SQL sql = new SQL();
                sql.DELETE_FROM(tableName);
                for (String key : condition.keySet()) {
                    if (condition.get(key) == null || "IS NULL".equals(condition.get(key).toString())) {
                        sql.WHERE(key + " IS NULL");
                    } else if ("IS NOT NULL".equals(condition.get(key).toString())) {
                        sql.WHERE(key + " IS NOT NULL");
                    } else {
                        sql.WHERE(key + " = #{condition." + key + "}");
                    }
                }
                return sql.toString();
            } else {
                throw new IllegalArgumentException("conditions 无效");
            }
        }

        public String callProcedure(Map<String, Object> para) {
            String procedureName = (String) para.get("procedureName");
            Map<String, Object> params = (Map<String, Object>) para.get("params");
            boolean hasOut = (boolean) para.get("hasOut");
            if (StringUtils.isBlank(procedureName)) {
                throw new IllegalArgumentException("procedureName 无效");
            } else {
                StringBuilder sql = new StringBuilder();
                sql.append("{call ").append(procedureName).append("(");
                if (params.isEmpty()) {
                    sql.append(")}");
                } else {
                    for (String key : params.keySet()) {
                        Object value = params.get(key);
                        if (value instanceof Number) {
                            sql.append(value).append(",");
                        } else if (value instanceof CharSequence || value instanceof Character) {
                            sql.append("'").append(value).append("',");
                        }
                    }
                    if (hasOut) {
                        sql.append("#{params.code, mode=OUT, jdbcType=INTEGER},");
                        sql.append("#{params.message, mode=OUT, jdbcType=VARCHAR},");
                    }
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")}");
                }
                return sql.toString();
            }
        }

        public String callFunction(Map<String, Object> para) {
            String functionName = (String) para.get("functionName");
            Map<String, Object> params = (Map<String, Object>) para.get("params");
            if (StringUtils.isBlank(functionName)) {
                throw new IllegalArgumentException("procedureName 无效");
            } else {
                StringBuilder sql = new StringBuilder();
                sql.append("{#{params.data, mode=OUT, jdbcType=VARCHAR} = call ").append(functionName).append("(");
                if (params.isEmpty()) {
                    sql.append(")}");
                } else {
                    for (String key : params.keySet()) {
                        Object value = params.get(key);
                        if (value instanceof Number) {
                            sql.append(value).append(",");
                        } else if (value instanceof CharSequence || value instanceof Character) {
                            sql.append("'").append(value).append("',");
                        }
                    }
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(")}");
                }
                return sql.toString();
            }
        }

    }

}
