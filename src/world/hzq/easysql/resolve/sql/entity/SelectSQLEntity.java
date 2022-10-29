package world.hzq.easysql.resolve.sql.entity;

import world.hzq.easysql.resolve.sql.common.SQLEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询SQL语句信息存储实体
 *
 */
public class SelectSQLEntity extends SQLEntity implements Serializable {
    //标准SQL 只做对比用
    public static transient final String standardSQL = "select distinct ? from ? ? join ? on ? where ? group by ? having ? order by ?";
    //group by条件字符串
    private String groupByCondition;
    //having 条件字符串
    private String havingCondition;
    //order by条件字符串
    private String orderByCondition;
    //去重标记
    private boolean isDistinct;
    //查询字段
    private List<String> queryFields;
    //select后的子查询存储实体
    private final List<SelectSQLEntity> selectSubQueryEntities = new ArrayList<>();
    //form后的子查询实体
    private final List<SelectSQLEntity> formSubQueryEntities = new ArrayList<>();
    //where后的子查询实体
    private final List<SelectSQLEntity> whereSubQueryEntities = new ArrayList<>();
    //having后的子查询实体
    private final List<SelectSQLEntity> havingSubQueryEntities = new ArrayList<>();

    public List<String> getQueryFields() {
        return queryFields;
    }

    public void setDistinct(boolean distinct) {
        isDistinct = distinct;
    }

    public void setOrderByCondition(String orderByCondition) {
        this.orderByCondition = orderByCondition;
    }

    public void setHavingCondition(String havingCondition) {
        this.havingCondition = havingCondition;
    }

    public void setGroupByCondition(String groupByCondition) {
        this.groupByCondition = groupByCondition;
    }

    public void setQueryFields(List<String> queryFields) {
        this.queryFields = queryFields;
    }

    public String getGroupByCondition() {
        return groupByCondition;
    }

    public String getHavingCondition() {
        return havingCondition;
    }

    public String getOrderByCondition() {
        return orderByCondition;
    }

    public boolean isDistinct() {
        return isDistinct;
    }

    /**
     * 获取查询类型
     * 子查询情况
     * 1、select (select ...) from ...
     * 2、select ... from (select ...)
     * 3、select ... from ... where ... (select...)
     * 4、select ... from ... group by ... having ... (select...)
     * 只对select列表和form列表进行解析,剩下的分情况解析
     * 共有12种合法SQL形式
     * 1、select ... from ...
     * 2、select ... from ... where ...
     * 3、select ... from ... where ... order by ...
     * 4、select ... from ... where ... group by ...
     * 5、select ... from ... where ... group by ... having ...
     * 6、select ... from ... where ... group by ... having ... order by ...
     * 7、select ... from ... where ... group by ... order by ...
     * 8、select ... from ... order by ...
     * 9、select ... from ... group by ...
     * 10、select ... from ... group by ... having ...
     * 11、select ... from ... group by ... order by ...
     * 12、select ... from ... group by ... having ... order by ...
     */
    public int getSelectType(){
        int type = -1;
        //where条件
        String whereCondition = getWhereCondition();
        //having条件
        String havingCondition = getHavingCondition();
        //group by条件
        String groupByCondition = getGroupByCondition();
        //order by条件
        String orderByCondition = getOrderByCondition();
        if(whereCondition == null){
            //只存在1,9,8
            if(groupByCondition == null){
                //只存在1,8
                if(orderByCondition == null){
                    //只存在1的情况
                    type = 1;
                }else {
                    //只存在8的情况
                    type = 8;
                }
            }else {
                //存在9,10,11,12的情况
                if(havingCondition == null){
                    //存在9,11的情况
                    if(orderByCondition == null){
                        //只存在9情况
                        type = 9;
                    }else {
                        type = 11;
                    }
                }else {
                    //存在10,12
                    if(orderByCondition == null){
                        //存在10
                        type = 10;
                    }else {
                        type = 12;
                    }
                }
            }
        }else{
            //存在2,3
            if(groupByCondition == null){
                if(orderByCondition == null){
                    //存在2
                    type = 2;
                }else{
                    type = 3;
                }
            }else{
                //存在4,7
                if(havingCondition == null){
                    //存在4
                    if(orderByCondition == null){
                        type = 4;
                    }else{
                        type = 7;
                    }
                }else{
                    //存在5,6
                    if(orderByCondition == null){
                        //存在5
                        type = 5;
                    }else{
                        type = 6;
                    }
                }
            }
        }
        return type;
    }

    /**
     * 是否存在子查询
     */
    public boolean existsSubQuery(){
        return subQuerySQLList.size() != 0;
    }

    public List<SelectSQLEntity> getSelectSubQueryEntities() {
        return selectSubQueryEntities;
    }

    public List<SelectSQLEntity> getFormSubQueryEntities() {
        return formSubQueryEntities;
    }

    public List<SelectSQLEntity> getWhereSubQueryEntities() {
        return whereSubQueryEntities;
    }

    public List<SelectSQLEntity> getHavingSubQueryEntities() {
        return havingSubQueryEntities;
    }

    @Override
    public String toString() {
        return "SelectSQLEntity{" +
                "relation=" + relation +
                ", whereCondition='" + whereCondition + '\'' +
                ", groupByCondition='" + groupByCondition + '\'' +
                ", havingCondition='" + havingCondition + '\'' +
                ", orderByCondition='" + orderByCondition + '\'' +
                ", isDistinct=" + isDistinct +
                ", queryFields=" + queryFields +
                ", tables=" + tables +
                ", selectSubQueryEntities=" + selectSubQueryEntities +
                ", formSubQueryEntities=" + formSubQueryEntities +
                ", whereSubQueryEntities=" + whereSubQueryEntities +
                ", havingSubQueryEntities=" + havingSubQueryEntities +
                '}';
    }
}
