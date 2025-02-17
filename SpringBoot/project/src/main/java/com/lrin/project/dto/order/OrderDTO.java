package com.lrin.project.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {

    private List<OrderItemDTO> orderItems;

}
