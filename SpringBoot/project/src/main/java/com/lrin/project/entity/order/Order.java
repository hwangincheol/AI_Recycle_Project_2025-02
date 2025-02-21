package com.lrin.project.entity.order;

import com.lrin.project.constant.OrderStatus;
import com.lrin.project.entity.BaseEntity;
import com.lrin.project.entity.member.MemberEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table (name = "orders")
@Data
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private MemberEntity member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // 주문상태
    
    private int orderPrice; // 주문 총 가격


    // 주문 취소 메서드
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
    }

    public void changeOrder(OrderStatus status) {
        this.orderStatus = status;
    }
}
