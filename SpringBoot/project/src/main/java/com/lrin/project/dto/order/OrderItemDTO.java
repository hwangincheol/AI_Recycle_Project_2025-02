package com.lrin.project.dto.order;

import lombok.Data;

@Data
public class OrderItemDTO {

    private String itemName;    //상품 이름

    private int count; //주문 수량

    private int orderPrice; //주문 금액


}