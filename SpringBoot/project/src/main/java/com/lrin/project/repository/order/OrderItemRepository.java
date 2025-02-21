package com.lrin.project.repository.order;

import com.lrin.project.entity.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {



}
