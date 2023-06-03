package com.capstone.fashionshop.services.order;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.OrderMapper;
import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.CreateShippingReq;
import com.capstone.fashionshop.payload.response.OrderRes;
import com.capstone.fashionshop.repository.OrderRepository;
import com.capstone.fashionshop.services.payment.PaymentUtils;
import com.capstone.fashionshop.services.shipping.ShippingAPIService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.cloudinary.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentUtils paymentUtils;
    private final ShippingAPIService shippingAPIService;

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
                    order.get().getState().equals(Constants.ORDER_STATE_PROCESS)) {
                String checkUpdateQuantityProduct = paymentUtils.checkingUpdateQuantityProduct(order.get(), false);
                order.get().setState(Constants.ORDER_STATE_CANCEL);
                orderRepository.save(order.get());
                if (checkUpdateQuantityProduct == null) {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject(true, "Cancel order successfully", ""));
                }
            } else throw new AppException(HttpStatus.BAD_REQUEST.value(),
                    "You cannot cancel while the order is still processing!");
        }
        throw new NotFoundException("Can not found order with id: " + id);
    }

    @Override
    public ResponseEntity<?> createShip(CreateShippingReq req, String orderId) {
        Optional<Order> order = orderRepository.findOrderByIdAndState(orderId, Constants.ORDER_STATE_PREPARE);
        if (order.isPresent()) {
            order.get().setState(Constants.ORDER_STATE_DELIVERY);
            HttpResponse<?> response = shippingAPIService.create(req, order.get());
            JSONObject objectRes = new JSONObject(response.body().toString()).getJSONObject("data");
            order.get().getDeliveryDetail().getDeliveryInfo().put("orderCode", objectRes.getString("order_code"));
            order.get().getDeliveryDetail().getDeliveryInfo().put("fee", objectRes.getLong("total_fee"));
            order.get().getDeliveryDetail().getDeliveryInfo().put("expectedDeliveryTime", objectRes.getString("expected_delivery_time"));
            orderRepository.save(order.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Create shipping order successfully", order.get().getDeliveryDetail().getDeliveryInfo()));
        } else throw new NotFoundException("Can not found order with id: " + orderId + " is prepare");
    }

    @Override
    public ResponseEntity<?> changeState(String state, String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            if (Constants.ORDER_STATE_DELIVERED.equals(state)) {
                if (order.get().getState().equals(Constants.ORDER_STATE_DELIVERY)) {
                    order.get().setState(Constants.ORDER_STATE_DELIVERED);
                    order.get().getDeliveryDetail().getDeliveryInfo().put("deliveredAt", LocalDateTime.now(Clock.systemUTC()));
                } else throw new AppException(HttpStatus.BAD_REQUEST.value(), "Order have not been delivering");
            } else {
                throw new AppException(HttpStatus.BAD_REQUEST.value(), "Invalid state");
            }
            orderRepository.save(order.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Change state of order successfully", state));
        } else throw new NotFoundException("Can not found order with id: " + orderId);
    }

    @Override
    public ResponseEntity<?> changeState(String state, String orderId, String userId) {
        Optional<Order> order = orderRepository.findOrderByIdAndUser_Id(orderId, new ObjectId(userId));
        if (order.isPresent()) {
            if (Constants.ORDER_STATE_DONE.equals(state)) {
                if (order.get().getState().equals(Constants.ORDER_STATE_DELIVERED)){
                    order.get().setState(Constants.ORDER_STATE_DONE);
                    order.get().getPaymentDetail().getPaymentInfo().put("isPaid", true);
                }
                else throw new AppException(HttpStatus.BAD_REQUEST.value(), "Order have not been delivered");
            } else {
                throw new AppException(HttpStatus.BAD_REQUEST.value(), "Invalid state");
            }
            orderRepository.save(order.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Finish order successfully", state));
        } else throw new NotFoundException("Can not found order with id: " + orderId);
    }
}
