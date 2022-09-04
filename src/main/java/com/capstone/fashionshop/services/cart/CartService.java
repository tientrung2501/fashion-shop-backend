package com.capstone.fashionshop.services.cart;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.CartMapper;
import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.models.entities.order.OrderItem;
import com.capstone.fashionshop.models.entities.product.ProductOption;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.CartReq;
import com.capstone.fashionshop.payload.response.CartItemRes;
import com.capstone.fashionshop.payload.response.CartRes;
import com.capstone.fashionshop.repository.OrderItemRepository;
import com.capstone.fashionshop.repository.OrderRepository;
import com.capstone.fashionshop.repository.ProductOptionRepository;
import com.capstone.fashionshop.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService implements ICartService{
    private final UserRepository userRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartMapper cartMapper;

    @Override
    public ResponseEntity<?> getProductFromCart(String userId) {
        Optional<User> user = userRepository.findUserByIdAndState(userId, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            Optional<Order> order = orderRepository.findOrderByUser_IdAndState(new ObjectId(userId), Constants.ORDER_STATE_ENABLE);
            if (order.isPresent()) {
                CartRes res = cartMapper.toCartRes(order.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Get cart success", res));
            } throw new NotFoundException("Can not found any order with user id: "+userId);
        } throw new NotFoundException("Can not found user with id: "+userId);
    }

    @Override
    @Transactional
    public ResponseEntity<?> addAndUpdateProductToCart(String userId, CartReq req) {
        Optional<User> user = userRepository.findUserByIdAndState(userId, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            Optional<Order> order = orderRepository.findOrderByUser_IdAndState(new ObjectId(userId), Constants.ORDER_STATE_ENABLE);
            if (order.isPresent()) {
                //Check if order already has product option with color
                Optional<OrderItem> item = order.get().getItems().stream().filter(
                        p -> p.getItem().getId().equals(req.getProductOptionId())
                                && p.getColor().equals(req.getColor())).findFirst();
                if (item.isPresent()) return processUpdateProductInCart(item.get(), req);
                else return processAddProductToExistOrder(order.get(), req);
            } else return processAddProductToOrder(user.get(), req);
        } throw new NotFoundException("Can not found user with id: "+userId);
    }

    @Transactional
    @Synchronized
    ResponseEntity<?> processAddProductToOrder(User user, CartReq req) {
        if (req.getQuantity() <= 0) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Invalid quantity");        Optional<ProductOption> productOption = productOptionRepository.findByIdAndVariantColor(req.getProductOptionId(), req.getColor());
        if (productOption.isPresent()) {
            checkProductQuantity(productOption.get(), req);
            Order order = new Order(user, Constants.ORDER_STATE_ENABLE);
            orderRepository.insert(order);
            OrderItem item = new OrderItem(productOption.get(), req.getColor(), req.getQuantity(), order);
            orderItemRepository.insert(item);
            CartItemRes res = CartMapper.toCartItemRes(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject(true, "Add product to cart first time success", res));
        } else throw new NotFoundException("Can not found product option with id: "+req.getProductOptionId());
    }

    private ResponseEntity<?> processAddProductToExistOrder(Order order, CartReq req) {
        if (req.getQuantity() <= 0) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Invalid quantity");
        Optional<ProductOption> productOption = productOptionRepository.findByIdAndVariantColor(req.getProductOptionId(), req.getColor());
        if (productOption.isPresent()) {
            checkProductQuantity(productOption.get(), req);
            OrderItem item = new OrderItem(productOption.get(), req.getColor(), req.getQuantity(), order);
            orderItemRepository.insert(item);
            CartItemRes res = CartMapper.toCartItemRes(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject(true, "Add product to cart success", res));
        } else throw new NotFoundException("Can not found product option with id: "+req.getProductOptionId());
    }

    private void checkProductQuantity(ProductOption productOption, CartReq req) {
        productOption.getVariants().forEach(v -> {
            if (v.getColor().equals(req.getColor())) {
                if (v.getStock() < req.getQuantity() ) {
                    throw new AppException(HttpStatus.CONFLICT.value(), "Quantity exceeds stock on product: "+req.getProductOptionId());
                }
            }
        });
    }

    private ResponseEntity<?> processUpdateProductInCart(OrderItem orderItem, CartReq req) {
        if (orderItem.getQuantity() + req.getQuantity() == 0) {
            orderItemRepository.deleteById(orderItem.getId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Delete item "+orderItem.getId()+" in cart success", ""));
        }
        orderItem.getItem().getVariants().forEach(v -> {
            if (v.getColor().equals(req.getColor())) {
                long quantity = orderItem.getQuantity() + req.getQuantity();
                if (v.getStock() >= quantity && quantity > 0) {
                    orderItem.setQuantity(quantity);
                    orderItemRepository.save(orderItem);
                } else throw new AppException(HttpStatus.CONFLICT.value(), "Quantity invalid or exceeds stock on product: "+req.getProductOptionId());
            }
        });
        CartItemRes res = CartMapper.toCartItemRes(orderItem);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(true, "Update product "+req.getProductOptionId()+" in cart success", res));
    }

    @Override
    public ResponseEntity<?> deleteProductFromCart(String userId, String orderItemId) {
        Optional<User> user = userRepository.findUserByIdAndState(userId, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);
            if (orderItem.isPresent() && orderItem.get().getOrder().getUser().getId().equals(userId)){
                orderItemRepository.deleteById(orderItemId);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Delete item "+orderItemId+" in cart success", ""));
            }
            else throw new AppException(HttpStatus.NOT_FOUND.value(), "Can not found product in your cart");
        } throw new NotFoundException("Can not found user with id: "+userId);
    }
}
