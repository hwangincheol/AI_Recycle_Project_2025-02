package com.lrin.project.dto.order;

import com.lrin.project.constant.OrderStatus;
import com.lrin.project.entity.order.Order;
import com.lrin.project.entity.order.OrderItem;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderListDTO {

    private Long orderId; //주문번호

    private String orderMemberId; //주문유저

    private String orderDate; //주문날짜

    private OrderStatus orderStatus; //주문 상태

    private List<OrderItem> orderItemList = new ArrayList<>(); //주문 아이템들
    
    private int orderPrice; //주문 총 가격

    public OrderListDTO(Order order){
        this.orderId = order.getId();
        this.orderMemberId = order.getMember().getId();
        this.orderDate = order.getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
        this.orderItemList = order.getOrderItems();
        this.orderPrice = order.getOrderPrice();
    }



}
