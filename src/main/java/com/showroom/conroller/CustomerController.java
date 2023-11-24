package com.showroom.conroller;

import com.showroom.Entity.Customer;
import com.showroom.Entity.Response;
import com.showroom.Service.CustomerService;
import com.showroom.constants.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<Response> getCustomerById(@PathVariable(name="id") int customerId) {
        Response response = customerService.findCustomerById(customerId);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<Response> getAllCustomer(@RequestParam(name = "page",defaultValue = "0") int pageNumber,
                                                   @RequestParam(name = "pageSize",defaultValue = "5") int pageSize,
                                                   @RequestParam(name = "column",defaultValue = "name") String column,
                                                   @RequestParam(name = "direction",defaultValue = "ASC") String direction,
                                                   @RequestParam(name = "value",defaultValue = "") String value) {
        Response response = customerService.getAllCustomer(pageNumber,pageSize,column,direction,value);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getCustomerByNameAndPhoneNo")
    public ResponseEntity<Response> getCustomerByNameAndPhoneNo(@RequestParam(name="name",defaultValue = "") String name,@RequestParam(name="phoneNo",defaultValue = "") String phoneNo) {
        Response response = customerService.getCustomerByNameAndPhoneNo(name,phoneNo);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/logInCustomer")
    public ResponseEntity<Response> logInCustomer(@RequestParam(name="name",defaultValue = "") String name,@RequestParam(name="password",defaultValue = "") String password) {
        Response response = customerService.LogInCustomer(name,password);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
    @PostMapping("/createCustomer")
    public ResponseEntity<Response> createCustomer(@RequestBody Customer customer,@RequestParam(name = "userType",defaultValue = "CUSTOMER") UserType userType) {
        Response response = customerService.createCustomer(customer,userType);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updateCustomer")
    public ResponseEntity<Response> updateCustomer(@RequestBody Customer customer) {
        Response response = customerService.updateCustomer(customer);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public ResponseEntity<Response> deleteCustomerById(@PathVariable(name = "id") int customerId) {
        Response response = customerService.deleteCustomerById(customerId);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
