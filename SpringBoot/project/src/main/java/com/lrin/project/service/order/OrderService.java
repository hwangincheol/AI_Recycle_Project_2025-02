package com.lrin.project.service.order;

import com.lrin.project.constant.OrderStatus;
import com.lrin.project.dto.order.OrderItemDTO;
import com.lrin.project.dto.order.OrderListDTO;
import com.lrin.project.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    Order createOrder(List<OrderItemDTO> orderItemsDTO, String userId);

    Page<OrderListDTO> getOrderList(String userId, Pageable pageable);

    boolean validateOrder(Long orderId, String name);

    void cancelOrder(Long orderId);

    Page<OrderListDTO> getOrderListByStatus(String userId, Pageable pageable, OrderStatus status1, OrderStatus status2);

    Page<OrderListDTO> getOrderAdminList(Pageable pageable);

    Page<OrderListDTO> getOrderListAdminByStatus(Pageable pageable, OrderStatus status);

    void changeOrder(Long orderId, OrderStatus status);
}
