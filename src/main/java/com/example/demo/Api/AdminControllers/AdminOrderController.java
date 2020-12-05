package com.example.demo.Api.AdminControllers;

import com.example.demo.Services.MainClasses.OrderInfo.OrderStatus;
import com.example.demo.Services.MainClasses.OrderInfo.Orders;
import com.example.demo.Services.ServicesRealization.OrderFulfillmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin/orders")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminOrderController {

    private final OrderFulfillmentService service;

    @Autowired
    public AdminOrderController(OrderFulfillmentService service) {
        this.service = service;
    }

    @GetMapping
    public String orderReport(Map<String, Object> model) {
        model.put("orders", service.orderReport());
        model.put("cancelled", service.getNumberOfOrdersByStatus(OrderStatus.isCancelled));
        model.put("completed", service.getNumberOfOrdersByStatus(OrderStatus.isCompleted));
        return "AdminOrderListPage";
    }

    @PostMapping
    public String orderFilter(Map<String, Object> model,
                              @RequestParam String select) {
        if (select.equals("s1")) {
            model.put("orders", service.orderReport());
            model.put("cancelled", service.getNumberOfOrdersByStatus(OrderStatus.isCancelled));
            model.put("completed", service.getNumberOfOrdersByStatus(OrderStatus.isCompleted));
        } else if (select.equals("s2")) {
            model.put("orders", service.getOrdersByStatus(OrderStatus.isCompleted));
            model.put("completed", service.getNumberOfOrdersByStatus(OrderStatus.isCompleted));
        } else {
            model.put("orders", service.getOrdersByStatus(OrderStatus.isCancelled));
            model.put("cancelled", service.getNumberOfOrdersByStatus(OrderStatus.isCancelled));
        }

        return "AdminOrderListPage";

    }

    @GetMapping("/deleteCancelled")
    public String deleteCancelledOrders() {
        service.deleteOrdersByStatus(OrderStatus.isCancelled);
        return "redirect:/admin/orders";

    }

    @GetMapping("/deleteAll")
    public String deleteAllOrders() {
        service.deleteOrdersByStatus(OrderStatus.isCancelled);
        service.deleteOrdersByStatus(OrderStatus.isCompleted);
        return "redirect:/admin/orders";

    }

    @GetMapping("{order}")
    public String currentOrderPage(@PathVariable Orders order,
                                   Map<String, Object> model) {

        if (order.getDriverId() != 0)
            model.put("driverId", order.getDriverId());
        if (order.getCarId() != 0)
            model.put("carId", order.getCarId());
        model.put("userId", order.getClientId());
        Long cashId = service.getCashierIdFromOrder(order.getOrderId());
        if (cashId != 0)
            model.put("cashId", cashId);
        return "AdminCurrentOrderPage";

    }
}
