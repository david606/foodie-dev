package com.imooc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@ToString
@Table(name = "order_status")
public class OrderStatus {
    /**
     * 订单ID;对应订单表的主键id
     */
    @Id
    @Column(name = "order_id")
    private String orderId;

    /**
     * 订单状态
     */
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 订单创建时间;对应[10:待付款]状态
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 支付成功时间;对应[20:已付款，待发货]状态
     */
    @Column(name = "pay_time")
    private Date payTime;

    /**
     * 发货时间;对应[30：已发货，待收货]状态
     */
    @Column(name = "deliver_time")
    private Date deliverTime;

    /**
     * 交易成功时间;对应[40：交易成功]状态
     */
    @Column(name = "success_time")
    private Date successTime;

    /**
     * 交易关闭时间;对应[50：交易关闭]状态
     */
    @Column(name = "close_time")
    private Date closeTime;

    /**
     * 留言时间;用户在交易成功后的留言时间
     */
    @Column(name = "comment_time")
    private Date commentTime;
}