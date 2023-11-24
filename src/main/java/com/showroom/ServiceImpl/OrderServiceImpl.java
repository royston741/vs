package com.showroom.ServiceImpl;

import com.showroom.Entity.*;
import com.showroom.Repository.CustomerRepository;
import com.showroom.Repository.OrderRepository;
import com.showroom.Service.*;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    EmailMailService emailService;
    @Autowired
    PdfGeneratorService pdfGeneratorService;

    @Override
    @Transactional
    public Response createOrder(Order order) {
        Response response = new Response(false);
        try {
            List<Vehicle> orderedVehicleList = order.getVehicles().stream().map(vehicle -> vehicleService.createVehicle(vehicle, order)).collect(Collectors.toList());
            Integer orderTotal = orderedVehicleList.stream().map(vehicle -> {
                        return (vehicle.getPrice()).intValue();
                    }).
                    reduce(0, Integer::sum);
            order.setVehicles(orderedVehicleList);
            order.setOrderTotal(orderTotal);
            Order insertedOrder = orderRepository.save(order);
            response.setResponseData(insertedOrder);
            response.setSuccess(true);

            Customer customer = customerRepository.findById(order.getCustomer().getId()).get();

            String email = customer.getEmail();
            List<String> textList = order.getVehicles().stream().map(vehicle -> {
                return "vehicle name :" + vehicle.getVehicleName() + "\n" +
                        "vehicle color : " + vehicle.getVehicleColor() + "\n" +
                        "vehicle model no. : " + vehicle.getVehicleModelNo() + "\n" +
                        "vehicle quantity : " + vehicle.getQuantity() + "\n" +
                        "vehicle fuel type : " + vehicle.getFuelType() + "\n" +
                        "-----------------------\n";
            }).collect(Collectors.toList());
            String mailText = textList.stream().reduce("", String::concat) + "\n" + "Order total :" + order.getOrderTotal();
            byte[] document = pdfGeneratorService.generatePdf(customer, order);
//            emailService.sendMail(email, "MyDrive order Booked", mailText,document);
        } catch (Exception e) {
//            if (e.getCause().getCause() instanceof ConstraintViolationException cv) {
//                response.getErrMessage().addAll(cv.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
//            }
            response.getErrMessage().add("Order not added");
            log.error("Error in createOrder {}", e);
        }
        return response;
    }

    @Override
    public Response updateOrder(Order order) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Order> existingOrder = orderRepository.findById(order.getId());
            // if exist
            if (existingOrder.isPresent()) {
                //update order
                Order updatedOrder = orderRepository.save(order);
                response.setResponseData(updatedOrder);
                response.setSuccess(true);
            } else {

                response.getErrMessage().add("Order does not exist");
            }
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException cv) {
                response.getErrMessage().addAll(cv.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
            }
            response.getErrMessage().add("Order not updated");
            log.error("Error in updateOrder {}", e);
        }
        return response;
    }

    @Override
    public Response updateOrder(int id, Vehicle vehicle, String action) {
        System.out.println("update order");
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Order> existingOrder = orderRepository.findById(id);
            System.out.println(existingOrder);
            // if exist
            if (existingOrder.isPresent()) {
                Order order = existingOrder.get();
                if (action.equals("add")) {
                    vehicleService.createVehicle(vehicle, order);
                } else if (action.equals("delete")) {
                    vehicleService.deleteVehicle(vehicle);
                }
                Integer orderTotal = order.getVehicles().stream().map(v -> {
                            return (v.getPrice()).intValue();
                        }).
                        reduce(0, Integer::sum);
                order.setOrderTotal(orderTotal);
                Order updatedOrder = orderRepository.save(order);
                response.setSuccess(true);
                response.setResponseData(updatedOrder);
            } else {
                response.getErrMessage().add("Order does not exist");
            }
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException cv) {
                response.getErrMessage().addAll(cv.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
            }
            response.getErrMessage().add("Order not updated");
            log.error("Error in updateOrder {}", e);
        }
        return response;
    }

    @Override
    public Response deleteOrder(Order order) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Order> existingOrder = orderRepository.findById(order.getId());
            // if exist
            if (existingOrder.isPresent()) {
                orderRepository.delete(existingOrder.get());
                response.setResponseData("order deleted");
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Order does not exist with id " + order.getId());
            }
        } catch (Exception e) {
            response.getErrMessage().add("Order not deleted");
            log.error("Error in deleteOrder {}", e);
        }
        return response;
    }

    @Override
    public Response deleteOrderById(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Order> existingOrder = orderRepository.findById(id);
            // if exist
            if (existingOrder.isPresent()) {
                orderRepository.delete(existingOrder.get());
                response.setResponseData("order deleted");
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Order does not exist with id " + id);
            }
        } catch (Exception e) {
            response.getErrMessage().add("Order not deleted");
            log.error("Error in deleteOrderById {}", e);
        }
        return response;
    }

    @Override
    public Response getOrderById(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Order> order = orderRepository.findById(id);
            // if exist
            if (order.isPresent()) {
                response.setResponseData(order);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Order does not exist");
            }
        } catch (Exception e) {
            response.getErrMessage().add("Order not found");
            log.error("Error in getOrderById {}", e);
        }
        return response;
    }

    @Override
    public Response getAllOrdersByCustomerNameAndPhoneNo(String name, String phoneNo) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            List<Order> orders = orderRepository.getOrderByCustomerNameAndPassword(name, phoneNo);
            if (orders.size() > 0) {
                response.setResponseData(orders);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Orders not found for given customer name and password");
            }
        } catch (Exception e) {
            response.getErrMessage().add("Orders not found");
            log.error("Error in getAllOrdersByCustomerNameAndPhoneNo {}", e);
        }
        return response;
    }

    @Override
    public Response getAllOrdersByCustomerId(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            List<Order> orders = orderRepository.getOrderByCustomerId(id);
            if (orders.size() > 0) {
                response.setResponseData(orders);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Orders not found for given customer id");
            }
        } catch (Exception e) {
            response.getErrMessage().add("Orders not found");
            log.error("Error in getAllOrdersByCustomerId {}", e);
        }
        return response;
    }

    @Override
    public Response getOrderTotalAndDiscountAndAdditionalCharges(List<Vehicle> vehicleList) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            List<Price> vehiclePrices = vehicleList.stream().map(vehicle -> new VehicleServiceImpl().calculateTotalPriceOfVehicle(vehicle)).collect(Collectors.toList());
            int total = vehiclePrices.stream().map(price -> price.getTotal().intValue()).reduce(0, Integer::sum);
            response.setResponseData(List.of(total, vehiclePrices));
            response.setSuccess(true);

        } catch (Exception e) {
            response.getErrMessage().add("Order not found");
            log.error("Error in getOrderById {}", e);
        }
        return response;
    }


    //    @Override
//    public Order updateOrder(Order order) {
//        return orderDao.updateOrder(order);
//    }
//    @Override
//    public Order updateOrderByAddingVehicle(Order order, Vehicle newVehicle) {
//        order.addItem(vehicleService.createVehicle(newVehicle));
//        Integer orderTotal = order.getVehicles().stream().map(vehicle -> {
//                    return (vehicle.getPrice()).intValue();
//                }).
//                reduce(0, Integer::sum);
//        order.setOrderTotal(orderTotal);
//        Order updatedOrder = orderDao.updateOrder(order);
//        log.info("------ UPDATED ORDERS ------");
//        log.info("Name    |     " +
//                "Quantity    |     " +
//                "Price    |     " +
//                "Model no.    |   " +
//                "Color     |   " +
//                "Fuel type     |   " +
//                "Two wheeler    |    "
//        );
//        updatedOrder.getVehicles().stream().forEach(vehicle -> {
//            vehicleService.printVehicles(vehicle);
//        });
//        log.info("Order total :" + updatedOrder.getOrderTotal());
//
//        return updatedOrder;
//    }
//    @Override
//    public Order updateOrderByDeletingVehicle(Order order, Vehicle removeVehicle) {
//        order.removeItem(removeVehicle);
//        Integer orderTotal = order.getVehicles().stream().map(vehicle -> {
//                    return (vehicle.getPrice()).intValue();
//                }).
//                reduce(0, Integer::sum);
//        order.setOrderTotal(orderTotal);
//        Order updatedOrder = orderDao.updateOrder(order);
//
//        log.info("------ UPDATED ORDERS ------");
//        log.info("Name    |     " +
//                "Quantity    |     " +
//                "Price    |     " +
//                "Model no.    |   " +
//                "Color     |   " +
//                "Fuel type     |   " +
//                "Two wheeler    |    "
//        );
//        updatedOrder.getVehicles().stream().forEach(vehicle -> {
//            vehicleService.printVehicles(vehicle);
//        });
//        log.info("Order total :" + updatedOrder.getOrderTotal());
//
//
//        return updatedOrder;
//    }
//    @Override
//    public void deleteOrder(Order order) {
//        orderDao.deleteOrder(order);
//        log.info("Above order is deleted");
//    }
//    @Override
//    public void deleteOrderById(int id) {
//        orderDao.deleteOrder(id);
//    }
//    @Override
//    public Order getOrderById(int id) {
//        Order order = orderDao.getOrderById(id);
//        List<Vehicle> vehicles = order.getVehicles();
//        log.info("Name    |     " +
//                "Quantity    |     " +
//                "Price    |     " +
//                "Model no.     |     " +
//                "Color       |    " +
//                "Fuel type     |   " +
//                "Two wheeler    |    "
//        );
//        vehicles.forEach(vehicle -> {
//            String twoWheelerType = vehicle.getTwoWheelerType() != null ? String.valueOf(vehicle.getTwoWheelerType()) : "";
//            log.info(vehicle.getVehicleName() + "        " +
//                    vehicle.getQuantity() + "        " +
//                    vehicle.getPrice() + "        " +
//                    vehicle.getVehicleModelNo() + "        " +
//                    vehicle.getVehicleColor() + "        " +
//                    vehicle.getFuelType() + "        " +
//                    twoWheelerType);
//        });
//        log.info("Order total : " + order.getOrderTotal());
//        return order;
//    }
//    @Override
//    public List<Order> getAllOrdersByCustomerNameAndPhoneNo(Customer customer) {
//        List<Order> orderList = orderDao.getOrdersByCustomerNameAndPhoneNo(customer.getName(), customer.getPhoneNo());
//      log.info("------ ALL ORDERS ------");
//        orderList
//                .forEach(order -> {
//                    log.info("--------- Order id " + order.getId() + "---------");
//                    List<Vehicle> vehicles = order.getVehicles();
//                    log.info("Name    |     " +
//                            "Quantity    |     " +
//                            "Price    |     " +
//                            "Model no.     |     " +
//                            "Color       |    " +
//                            "Fuel type     |   " +
//                            "Two wheeler    |    "
//                    );
//                    vehicles.forEach(vehicle -> {
//                        String twoWheelerType = vehicle.getTwoWheelerType() != null ? String.valueOf(vehicle.getTwoWheelerType()) : "";
//                        log.info(vehicle.getVehicleName() + "        " +
//                                vehicle.getQuantity() + "        " +
//                                vehicle.getPrice() + "        " +
//                                vehicle.getVehicleModelNo() + "        " +
//                                vehicle.getVehicleColor() + "        " +
//                                vehicle.getFuelType() + "        " +
//                                twoWheelerType);
//                    });
//                    log.info("Order total : " + order.getOrderTotal());
//                });
//        return orderList;
//    }

}
