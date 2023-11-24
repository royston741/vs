package com.showroom.Service;

import com.showroom.Entity.Customer;
import com.showroom.Entity.Order;
import com.showroom.Entity.Response;
import com.showroom.Entity.Vehicle;

import java.util.List;

public interface OrderService {
    public Response createOrder(Order order);

    public Response updateOrder(Order order);

    public Response updateOrder(int id, Vehicle newVehicle, String action);

    public Response deleteOrder(Order order);

    public Response deleteOrderById(int id);

    public Response getOrderById(int id);

    public Response getAllOrdersByCustomerId(int id);

  public Response  getOrderTotalAndDiscountAndAdditionalCharges(List<Vehicle> vehicleList);
    public Response getAllOrdersByCustomerNameAndPhoneNo(String name, String phoneNo);

}
