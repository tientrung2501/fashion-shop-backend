package com.capstone.fashionshop.utils;

import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.services.mail.EMailType;
import com.capstone.fashionshop.services.mail.MailService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@Service
@Getter
@Setter
public class MailUtils implements Runnable {
    private MailService mailService;
    private Order order;

    @Override
    @SneakyThrows
    public void run() {
        Map<String, Object> model = new HashMap<>();
        Locale locale = new Locale("vn", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        String paid = "Chưa thanh toán";

        model.put("orderId", order.getId());
        model.put("total", currencyFormatter.format(order.getTotalPrice()));
        model.put("paymentType", order.getPaymentDetail().getPaymentType());
        if ((boolean) order.getPaymentDetail().getPaymentInfo().get("isPaid"))
            paid = "Đã thanh toán";
        model.put("isPaid", paid);
        model.put("name", order.getDeliveryDetail().getReceiveName());
        model.put("phone", order.getDeliveryDetail().getReceivePhone());
        model.put("address", order.getDeliveryDetail().getDeliveryInfo().getOrDefault("fullAddress", order.getDeliveryDetail().getReceiveAddress()));
        try {
            model.put("expectedTime", LocalDate.ofInstant
                    (Instant.ofEpochMilli((Long) order.getDeliveryDetail().getDeliveryInfo().get("expectedDeliveryTime")*1000),
                            TimeZone.getDefault().toZoneId()));
        } catch (Exception e) {
            model.put("expectedTime", LocalDate.now().plusDays(3));
        }

        Map<String, String> items = new HashMap<>();
        order.getItems().forEach(item -> items.put(String.format("%s <br/> <b>[%s cái]</b>", item.getItem().getProduct().getName(), item.getQuantity()), currencyFormatter.format(item.getSubPrice())));
        model.put("items", items);

        mailService.sendEmail(order.getUser().getEmail(), model, EMailType.ORDER);
    }
}
