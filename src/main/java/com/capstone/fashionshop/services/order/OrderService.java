package com.capstone.fashionshop.services.order;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.OrderMapper;
import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.response.OrderRes;
import com.capstone.fashionshop.payload.response.OrdersSaleRes;
import com.capstone.fashionshop.repository.OrderRepository;
import com.capstone.fashionshop.services.payment.PaymentUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentUtils paymentUtils;

    @Override
    public ResponseEntity<?> findAll(String state, Pageable pageable) {
        Page<Order> orders;
        if (state.isBlank()) orders = orderRepository.findAll(pageable);
        else orders = orderRepository.findAllByState(state, pageable);
        if (orders.isEmpty()) throw new NotFoundException("Can not found any orders");
        List<OrderRes> resList = orders.stream().map(orderMapper::toOrderRes).collect(Collectors.toList());
        Map<String, Object> resp = new HashMap<>();
        resp.put("list", resList);
        resp.put("totalQuantity", orders.getTotalElements());
        resp.put("totalPage", orders.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(true, "Get orders success", resp));
    }

    @Override
    public ResponseEntity<?> findOrderById(String id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            OrderRes orderRes = orderMapper.toOrderDetailRes(order.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get order success", orderRes));
        }
        throw new NotFoundException("Can not found order with id: " + id);
    }

    @Override
    public ResponseEntity<?> findOrderById(String id, String userId) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent() && order.get().getUser().getId().equals(userId)) {
            OrderRes orderRes = orderMapper.toOrderDetailRes(order.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get order success", orderRes));
        }
        throw new NotFoundException("Can not found order with id: " + id);
    }

    @Override
    public ResponseEntity<?> cancelOrder(String id, String userId) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent() && order.get().getUser().getId().equals(userId)) {
            if (order.get().getState().equals(Constants.ORDER_STATE_PENDING) ||
                    order.get().getState().equals(Constants.ORDER_STATE_PROCESS) ||
                    order.get().getState().equals(Constants.ORDER_STATE_PAID)) {
                order.get().setState(Constants.ORDER_STATE_CANCEL);
                if (order.get().getState().equals(Constants.ORDER_STATE_PAID)) {
                    //refund
                    order.get().getPaymentDetail().getPaymentInfo().put("refund", true);
                }
                orderRepository.save(order.get());
                String checkUpdateQuantityProduct = paymentUtils.checkingUpdateQuantityProduct(order.get(), false);
                if (checkUpdateQuantityProduct == null) {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject(true, "Cancel order successfully", ""));
                }
            } else throw new AppException(HttpStatus.BAD_REQUEST.value(),
                    "You cannot cancel or refund while the order is still processing!");
        }
        throw new NotFoundException("Can not found order with id: " + id);
    }

    @Override
    public ResponseEntity<?> getOrderStatistical(String from, String to, String type) {
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime toDate = LocalDateTime.now();
        String pattern = "dd-MM-yyyy";
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        try {
            if (!from.isBlank()) fromDate = LocalDate.parse(from, df).atStartOfDay();
            if (!to.isBlank()) toDate = LocalDate.parse(to, df).atStartOfDay();
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "Incorrect date format");
        }
        Page<Order> orderList = orderRepository.findAllByCreatedDateBetweenAndState(fromDate, toDate, Constants.ORDER_STATE_PAID, Pageable.unpaged());
        switch (type) {
            case "all":
                orderList = orderRepository.findAllByState(Constants.ORDER_STATE_PAID, PageRequest.of(0, Integer.MAX_VALUE, Sort.by("lastModifiedDate").ascending()));
                pattern = "";
                break;
            case "month":
                pattern = "MM-yyyy";
                break;
            case "year":
                pattern = "yyyy";
                break;
        }
        List<OrdersSaleRes> ordersSaleResList = getSaleAmount(orderList, pattern);
        return ordersSaleResList.size() > 0 ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(true, "Get orders sale successful", ordersSaleResList)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject(false, "Can not found any order", "")
                );
    }

    public List<OrdersSaleRes> getSaleAmount(Page<Order> orderList, String pattern) {
        List<OrdersSaleRes> ordersSaleResList = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        if (orderList.getSize() > 0) {
            OrdersSaleRes ordersSaleRes = new OrdersSaleRes();
            int quantity = 1;
            for (int i = 0; i <= orderList.getSize() - 1; i++) {
                String dateFormat = df.format(orderList.getContent().get(i).getLastModifiedDate());
                if (i == 0 || !ordersSaleRes.getDate().equals(dateFormat)) {
                    if (i > 0) ordersSaleResList.add(ordersSaleRes);
                    if (dateFormat.isBlank()) dateFormat = "all";
                    ordersSaleRes = new OrdersSaleRes(dateFormat,
                            orderList.getContent().get(i).getTotalPrice(), quantity);
                } else {
                    quantity++;
                    ordersSaleRes.setAmount(ordersSaleRes.getAmount().add(orderList.getContent().get(i).getTotalPrice()));
                    ordersSaleRes.setQuantity(quantity);
                }
                if (i == orderList.getSize() - 1) ordersSaleResList.add(ordersSaleRes);
            }
        }
        return ordersSaleResList;
    }
}
