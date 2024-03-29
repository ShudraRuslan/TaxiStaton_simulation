package com.example.demo.Services.ServicesRealization;


import com.example.demo.Services.MainClasses.OrderInfo.OrderStatus;
import com.example.demo.Services.MainClasses.OrderInfo.Orders;
import com.example.demo.Services.MainClasses.Roles.User;
import com.example.demo.Services.MainClasses.repos.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class OrderFulfillmentService {

    private final OrderRepo repos;

    OrderFulfillmentService(OrderRepo repos) {
        this.repos = repos;
    }

    public Orders createOrder(int amountOfPassengers, double distance, User client) {
        Orders order = new Orders(amountOfPassengers, distance, client);
        repos.save(order);
        return order;
    }

    public boolean checkIfIsCompleted(Long orderId) {
        Orders order = repos.getOrderByOrderId(orderId);
        return order.getStatus() == OrderStatus.isCompleted;

    }

    public void setOrderStatus(Long id, OrderStatus status) {
        Orders order = repos.getOrderByOrderId(id);
        order.setStatus(status);
        repos.save(order);
    }

    public void serCarId(Long orderId, Long carId) {
        Orders order = repos.getOrderByOrderId(orderId);
        order.setCarId(carId);
        repos.save(order);
    }

    public void serDriverId(Long orderId, Long driverId) {
        Orders order = repos.getOrderByOrderId(orderId);
        order.setDriverId(driverId);
        repos.save(order);
    }

    public OrderStatus getOrderStatus(Long orderId) {
        return repos.getOrderByOrderId(orderId).getStatus();
    }

    public Orders getOrderById(Long id) {
        return repos.getOrderByOrderId(id);
    }


    public List<Orders> orderReport() {
        return (List<Orders>) repos.findAll();
    }

    public List<Orders> getOrdersByStatus(OrderStatus status) {
        return repos.getAllOrdersByStatus(status);
    }

    private void deleteOperation(List<Orders> list) {
        if (list.size() == 0) return;
        int iterator = 0;
        int iterationSize = list.size();

        for (iterator = 0; iterator < iterationSize; iterator++) {

            repos.delete(list.get(iterator));
        }
    }

    public void deleteOrdersByStatus(OrderStatus status) {
        List<Orders> orders = repos.getAllOrdersByStatus(status);
        deleteOperation(orders);
    }


    public int getNumberOfOrdersByStatus(OrderStatus status) {
        if (status == OrderStatus.isCancelled) {
            return repos.numberOfCancelledOrders();
        } else return repos.numberOfCompletedOrders();
    }

    public List<Orders> getOrdersWithDriver(Long driverId) {
        return repos.getAllOrdersByDriverId(driverId);
    }

    public List<Orders> getOrdersWithCar(Long carId) {
        return repos.getAllOrdersByCarId(carId);
    }

}


