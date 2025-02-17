package com.lrin.project.controller;

import com.lrin.project.constant.OrderStatus;
import com.lrin.project.dto.item.ItemDTO;
import com.lrin.project.dto.order.OrderDTO;
import com.lrin.project.dto.order.OrderItemDTO;
import com.lrin.project.dto.order.OrderListDTO;
import com.lrin.project.entity.order.Order;
import com.lrin.project.service.item.ItemService;
import com.lrin.project.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final ItemService itemService;
    private final OrderService orderService;

    // 수거 페이지로 가기
    @GetMapping(value = "/collect")
    public String index(Model model) {

        List<ItemDTO> items = itemService.getItems();

        model.addAttribute("items", items);
        model.addAttribute("cssPath", "order/collect");
        model.addAttribute("jsPath", "order/collect");
        model.addAttribute("pageTitle", "수거 페이지");
        return "order/collect";
    }

    @PostMapping("/order")
    public String saveOrder(@ModelAttribute OrderDTO orderDTO,  Principal principal) {
        List<OrderItemDTO> orderItemsDTO = orderDTO.getOrderItems();
        String userId = principal.getName();

        Order order = orderService.createOrder(orderItemsDTO, userId);

        return "redirect:/collect";
    }

    // 주문 목록 페이지로 가기 (일반 사용자)
    @GetMapping(value = {"/orderList", "/orderList/{page}"})
    public String orderList(@PathVariable("page") Optional<Integer> page, @RequestParam("status") Optional<String> status, Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        String userId = principal.getName();

        Page<OrderListDTO> ordersListDto;
        if (status.isPresent()) {
            String orderStatus = status.get();
            if ("ING".equals(orderStatus)) {
                // 진행 중인 주문 (ORDER, MOVE 상태)
                ordersListDto = orderService.getOrderListByStatus(userId, pageable, OrderStatus.ORDER, OrderStatus.MOVE);
                model.addAttribute("ING", "ING");
            } else if ("OK".equals(orderStatus)) {
                // 처리된 주문 (CANCEL, COLLECT 상태)
                ordersListDto = orderService.getOrderListByStatus(userId, pageable, OrderStatus.CANCEL, OrderStatus.COLLECT);
                model.addAttribute("OK", "OK");
            } else {
                ordersListDto = orderService.getOrderList(userId, pageable);
            }
        } else {
            ordersListDto = orderService.getOrderList(userId, pageable);
        }

        // 잘못된 페이지 요청 방지
        int currentPage = page.orElse(0);
        if (currentPage >= ordersListDto.getTotalPages() && ordersListDto.getTotalPages() > 0) {
            return "redirect:/orderList";
        }
        if (currentPage < 0) {
            return "redirect:/orderList";
        }

        model.addAttribute("orders", ordersListDto);
        model.addAttribute("maxPage", 10);
        model.addAttribute("cssPath", "order/orderList");
        model.addAttribute("pageTitle", "주문 목록");
        model.addAttribute("jsPath", "order/orderList");

        return "order/orderList";
    }

    @PostMapping("/orderCancel/{orderId}") //주문 취소용 메서드 (비동기 처리)
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){

        String userId = principal.getName();
        if(!orderService.validateOrder(orderId, userId)){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        } // 다른 사람이 url로 주문 취소 못하도록 설정

        orderService.cancelOrder(orderId);  // 주문 취소
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    // 주문 목록 관리 페이지로 (관리자 용)
    @GetMapping(value = {"/admin/orderAdmin", "/admin/orderAdmin/{page}"})
    public String orderListAdmin(@PathVariable("page") Optional<Integer> page, @RequestParam("status") Optional<String> status, Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        Page<OrderListDTO> ordersListDto;
        if (status.isPresent()) {
            String orderStatus = status.get();
            if ("ORDER".equals(orderStatus)) {
                ordersListDto = orderService.getOrderListAdminByStatus(pageable, OrderStatus.ORDER);
                model.addAttribute("ORDER", "ORDER");
            } else if ("CANCEL".equals(orderStatus)) {
                ordersListDto = orderService.getOrderListAdminByStatus(pageable, OrderStatus.CANCEL);
                model.addAttribute("CANCEL", "CANCEL");
            } else if ("MOVE".equals(orderStatus)) {
                ordersListDto = orderService.getOrderListAdminByStatus(pageable, OrderStatus.MOVE);
                model.addAttribute("MOVE", "MOVE");
            } else if ("COLLECT".equals(orderStatus)) {
                ordersListDto = orderService.getOrderListAdminByStatus(pageable, OrderStatus.COLLECT);
                model.addAttribute("COLLECT", "COLLECT");
            } else {
                ordersListDto = orderService.getOrderAdminList(pageable);
            }
        } else {
            ordersListDto = orderService.getOrderAdminList(pageable);
        }

        // 잘못된 페이지 요청 방지
        int currentPage = page.orElse(0);
        if (currentPage >= ordersListDto.getTotalPages() && ordersListDto.getTotalPages() > 0) {
            return "redirect:/admin/orderAdmin";
        }
        if (currentPage < 0) {
            return "redirect:/admin/orderAdmin";
        }

        model.addAttribute("orders", ordersListDto);
        model.addAttribute("maxPage", 10);
        model.addAttribute("cssPath", "order/orderList");   // orderList.css 사용
        model.addAttribute("pageTitle", "주문 목록 관리");
        model.addAttribute("jsPath", "order/orderAdmin");

        return "order/orderAdmin";
    }

    @PostMapping("/admin/orderChange") //주문 취소용 메서드 (비동기 처리)
    public @ResponseBody ResponseEntity changeOrderStatus(@RequestBody Map<String, String> requestData, Principal principal){

        Long orderId = Long.valueOf(requestData.get("orderId"));
        OrderStatus status = OrderStatus.valueOf(requestData.get("status"));

        orderService.changeOrder(orderId, status);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }







}
