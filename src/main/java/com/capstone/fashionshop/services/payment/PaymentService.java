package com.capstone.fashionshop.services.payment;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.models.entities.user.User;
import com.capstone.fashionshop.models.entities.order.DeliveryDetail;
import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.models.entities.order.PaymentDetail;
import com.capstone.fashionshop.payload.request.CheckoutReq;
import com.capstone.fashionshop.repository.OrderRepository;
import com.capstone.fashionshop.repository.UserRepository;
import com.capstone.fashionshop.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    public static String CLIENT_REDIRECT = "http://localhost:3000/redirect/payment?success=";

    private final ApplicationContext context;
    private final OrderRepository orderRepository;
    private final JwtUtils jwtTokenUtil;
    private final UserRepository userRepository;

    public PaymentFactory getPaymentMethod(String methodName) {
        switch (methodName) {
            case Constants.PAYMENT_PAYPAL: return context.getBean(PaypalService.class);
            case Constants.PAYMENT_VNPAY: return context.getBean(VNPayService.class);
            case Constants.PAYMENT_COD: return context.getBean(CODService.class);
            default: return null;
        }
    }

    @Transactional
    public ResponseEntity<?> createPayment(HttpServletRequest request, String id, String paymentType, CheckoutReq req) {
        String user_id = jwtTokenUtil.getUserFromJWT(jwtTokenUtil.getJwtFromHeader(request)).getId();
        if (user_id.isBlank()) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Token is invalid");
        Optional<Order> order;
        try {
            order = orderRepository.findOrderByUser_IdAndState(new ObjectId(user_id), Constants.ORDER_STATE_ENABLE);
            if (order.isEmpty() || !order.get().getId().equals(id)) {
                throw new NotFoundException("Can not found any order with id: " + id);
            }
            PaymentDetail paymentDetail = new PaymentDetail(null,paymentType.toUpperCase(),"", null);
            order.get().setPaymentDetail(paymentDetail);
            DeliveryDetail deliveryDetail = new DeliveryDetail(req.getName(), req.getPhone(),
                    req.getProvince(), req.getDistrict(), req.getWard(),req.getAddress());
            order.get().setDeliveryDetail(deliveryDetail);
            order.get().setState(Constants.ORDER_STATE_PROCESS);
            orderRepository.save(order.get());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            throw new NotFoundException(e.getMessage());
        }catch (AppException e) {
            throw new AppException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "More than one cart with user id: "+ user_id);
        }
        PaymentFactory paymentFactory = getPaymentMethod(paymentType);
        return paymentFactory.createPayment(request, order.get());
    }

    @Transactional
    public ResponseEntity<?> executePayment(String paymentId, String payerId, String responseCode,
                                            String id, HttpServletRequest request, HttpServletResponse response) {
        if (paymentId != null && payerId != null ) {
            PaymentFactory paymentFactory = getPaymentMethod(Constants.PAYMENT_PAYPAL);
            return paymentFactory.executePayment(paymentId, payerId, null,null, request, response);
        } else if (responseCode != null) {
            PaymentFactory paymentFactory = getPaymentMethod(Constants.PAYMENT_VNPAY);
            return paymentFactory.executePayment(null, null, responseCode, id, request, response);
        } else {
            checkRoleForCODPayment(request);
            PaymentFactory paymentFactory = getPaymentMethod(Constants.PAYMENT_COD);
            return paymentFactory.executePayment(paymentId, null, null,null, request, response);
        }
    }

    @Transactional
    public ResponseEntity<?> cancelPayment(String id, String responseCode, HttpServletRequest request, HttpServletResponse response) {
        String check = id.split("-")[0];
        if (check.equals("EC")) {
            PaymentFactory paymentFactory = getPaymentMethod(Constants.PAYMENT_PAYPAL);
            return paymentFactory.cancelPayment(id, null, response);
        } else if (responseCode != null) {
            PaymentFactory paymentFactory = getPaymentMethod(Constants.PAYMENT_VNPAY);
            return paymentFactory.cancelPayment(id, responseCode, response);
        } else {
            checkRoleForCODPayment(request);
            PaymentFactory paymentFactory = getPaymentMethod(Constants.PAYMENT_COD);
            return paymentFactory.cancelPayment(id, null, response);
        }
    }

    private void checkRoleForCODPayment(HttpServletRequest request) {
        String userId = jwtTokenUtil.getUserFromJWT(jwtTokenUtil.getJwtFromHeader(request)).getId();
        Optional<User> user = userRepository.findUserByIdAndState(userId, Constants.USER_STATE_ACTIVATED);
        if (user.isEmpty() ||
                !(user.get().getRole().equals(Constants.ROLE_ADMIN) || user.get().getRole().equals(Constants.ROLE_STAFF)))
            throw new AppException(HttpStatus.FORBIDDEN.value(), "You don't have permission!");
    }
}
