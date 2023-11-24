package com.showroom.conroller;

import com.showroom.Entity.Order;
import com.showroom.Entity.Request;
import com.showroom.Entity.Response;
import com.showroom.Entity.Vehicle;
import com.showroom.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<Response> createOrder(@RequestBody Order order) {
        Response response = orderService.createOrder(order);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getOrderById/{id}")
    public ResponseEntity<Response> getOrder(@PathVariable(name = "id") int orderId) {
        Response response = orderService.getOrderById(orderId);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/getOrderTotalAndDiscountAndAdditionalCharges")
    public ResponseEntity<Response> getOrderTotalAndDiscountAndAdditionalCharges(@RequestBody List<Vehicle> vehicleList) {
        Response response = orderService.getOrderTotalAndDiscountAndAdditionalCharges(vehicleList);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


    @GetMapping("/getOrdersByCustomerNameAndPhoneNo")
    public ResponseEntity<Response> getOrdersByCustomerNameAndPhoneNo(@RequestParam(name = "name", defaultValue = "") String name, @RequestParam(name = "phoneNo", defaultValue = "") String phoneNo) {
        Response response = orderService.getAllOrdersByCustomerNameAndPhoneNo(name, phoneNo);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getOrdersByCustomerId/{id}")
    public ResponseEntity<Response> getOrdersByCustomerId(@PathVariable("id") int id) {
        Response response = orderService.getAllOrdersByCustomerId(id);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updateOrder")
    public ResponseEntity<Response> updateOrder(@RequestBody Order order) {
        Response response = orderService.updateOrder(order);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updateCustomerOrder")
    public ResponseEntity<Response> updateCustomerOrder(@RequestParam("orderId") int id, @RequestBody Vehicle vehicle, @RequestParam(name = "operation", defaultValue = "") String operation) {
//        System.out.println(id);
//        System.out.println(vehicle);
        Response response = orderService.updateOrder(id, vehicle, operation);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteOrder")
    public ResponseEntity<Response> deleteOrder(@RequestBody Order order) {
        Response response = orderService.deleteOrder(order);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteOrderById/{id}")
    public ResponseEntity<Response> deleteOrderById(@PathVariable(name="id") int customerId) {
        Response response = orderService.deleteOrderById(customerId);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
