package com.project.TECHGEAR.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.project.TECHGEAR.Model.OrderItem;
import com.project.TECHGEAR.Services.OrderItemService;

@Controller
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    public OrderItem guardarItem(OrderItem item) {
        return orderItemService.guardar(item);
    }

}
