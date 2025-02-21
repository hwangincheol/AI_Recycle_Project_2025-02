package com.lrin.project.service.order;

import com.lrin.project.constant.OrderStatus;
import com.lrin.project.dto.order.OrderItemDTO;
import com.lrin.project.dto.order.OrderListDTO;
import com.lrin.project.entity.item.Item;
import com.lrin.project.entity.member.MemberEntity;
import com.lrin.project.entity.order.Order;
import com.lrin.project.entity.order.OrderItem;
import com.lrin.project.repository.item.ItemRepository;
import com.lrin.project.repository.member.MemberRepository;
import com.lrin.project.repository.order.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    //주문 등록
    @Override
    public Order createOrder(List<OrderItemDTO> orderItemsDTO, String userId) {

        Order order = new Order();

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO dto : orderItemsDTO) {
            // Item 엔티티 찾기
            Item item = itemRepository.findById(dto.getItemName())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

            // OrderItem 엔티티 생성 및 값 설정
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(dto.getCount());
            orderItem.setOrder(order);

            // OrderItem을 Order에 추가
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        MemberEntity member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        order.setMember(member);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderPrice(orderItemsDTO.getFirst().getOrderPrice());

        // Order 저장
        return orderRepository.save(order);
    }

    //오더 리스트 불러오기
    public Page<OrderListDTO> getOrderList(String userId, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(userId, pageable);
        Long totalCount = orderRepository.countOrder(userId);

        List<OrderListDTO> orderList = new ArrayList<>();

        for (Order order : orders) {
            OrderListDTO orderListDto = new OrderListDTO(order);
            orderList.add(orderListDto);
        }

        return new PageImpl<OrderListDTO>(orderList, pageable, totalCount);
    }

    //현재 로그인 사용자와 주문 데이터를 생성한 사용자가 같은지 검사용
    public boolean validateOrder(Long orderId, String userId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        MemberEntity savedMember = order.getMember();

        if(!StringUtils.equals(userId, savedMember.getId())){
            return false;
        }
        return true;
    }

    // 주문 취소
    public void cancelOrder(Long orderId){ 
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();    //주문 취소
    }

    @Override
    public Page<OrderListDTO> getOrderListByStatus(String userId, Pageable pageable, OrderStatus status1, OrderStatus status2) {

        List<Order> orders = orderRepository.findOrdersByStatus(userId, status1, status2, pageable);
        Long totalCount = orderRepository.countOrdersByStatus(userId, status1, status2);

        List<OrderListDTO> orderList = new ArrayList<>();

        for (Order order : orders) {
            OrderListDTO orderListDto = new OrderListDTO(order);
            orderList.add(orderListDto);
        }

        return new PageImpl<OrderListDTO>(orderList, pageable, totalCount);
    }

    @Override
    public Page<OrderListDTO> getOrderAdminList(Pageable pageable) {
        List<Order> orders = orderRepository.findAllOrders(pageable);
        Long totalCount = orderRepository.countAllOrder();

        List<OrderListDTO> orderList = new ArrayList<>();

        for (Order order : orders) {
            OrderListDTO orderListDto = new OrderListDTO(order);
            orderList.add(orderListDto);
        }

        return new PageImpl<OrderListDTO>(orderList, pageable, totalCount);
    }

    @Override
    public Page<OrderListDTO> getOrderListAdminByStatus(Pageable pageable, OrderStatus status) {

        List<Order> orders = orderRepository.findAllOrdersByStatus(status, pageable);
        Long totalCount = orderRepository.countAllOrdersByStatus(status);

        List<OrderListDTO> orderList = new ArrayList<>();

        for (Order order : orders) {
            OrderListDTO orderListDto = new OrderListDTO(order);
            orderList.add(orderListDto);
        }

        return new PageImpl<OrderListDTO>(orderList, pageable, totalCount);
    }

    @Override
    public void changeOrder(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.changeOrder(status);
    }


}
