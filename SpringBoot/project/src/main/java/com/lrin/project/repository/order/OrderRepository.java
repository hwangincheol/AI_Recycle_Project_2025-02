package com.lrin.project.repository.order;

import com.lrin.project.constant.OrderStatus;
import com.lrin.project.entity.order.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.member.id = :id order by o.regTime desc" )
    List<Order> findOrders(@Param("id") String userId, Pageable pageable);

    @Query("select count(o) from Order o where o.member.id = :id" )
    Long countOrder(@Param("id") String userId);

    @Query("SELECT o FROM Order o WHERE o.member.id = :id AND o.orderStatus IN (:status1, :status2) order by o.regTime desc")
    List<Order> findOrdersByStatus(@Param("id") String userId, @Param("status1") OrderStatus status1, @Param("status2") OrderStatus status2, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.member.id = :id AND o.orderStatus IN (:status1, :status2)")
    Long countOrdersByStatus(@Param("id") String userId, @Param("status1") OrderStatus status1, @Param("status2") OrderStatus status2);

    @Query("select o from Order o order by o.regTime desc" )
    List<Order> findAllOrders(Pageable pageable);

    @Query("select count(o) from Order o" )
    Long countAllOrder();

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status order by o.regTime desc")
    List<Order> findAllOrdersByStatus(@Param("status") OrderStatus status, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus = :status")
    Long countAllOrdersByStatus(@Param("status") OrderStatus status);

}
