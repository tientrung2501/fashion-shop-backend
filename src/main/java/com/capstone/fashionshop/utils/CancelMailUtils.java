package com.capstone.fashionshop.utils;

import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.services.mail.EMailType;
import com.capstone.fashionshop.services.mail.MailService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@Service
@Getter
@Setter
public class CancelMailUtils implements Runnable {
    private MailService mailService;
    private Order order;

    @Override
    @SneakyThrows
    public void run() {
        Map<String, Object> model = new HashMap<>();

        model.put("userName", order.getUser().getName());
        model.put("orderID", order.getId());

        mailService.sendEmail(order.getUser().getEmail(), model, EMailType.CANCEL);
    }
}
