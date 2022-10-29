package world.hzq.easysql.resolve.sql.entity;

import world.hzq.easysql.resolve.sql.common.OrderType;
import world.hzq.easysql.resolve.sql.common.SQLEntity;

public class OrderEntity extends SQLEntity {
    private OrderType orderType;

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderType=" + orderType +
                ", database='" + database + '\'' +
                ", tables=" + tables +
                '}';
    }
}
