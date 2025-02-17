package com.lrin.project.entity.order;

import com.lrin.project.entity.BaseEntity;
import com.lrin.project.entity.item.Item;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_name")
    private Item item;      // 하나의 상품은 여러 주문 상품으로 들어갈 수 있음

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;        // 한번의 주문에 여러개의 상품을 주문할 수 있음

    private int count; //수량

}
