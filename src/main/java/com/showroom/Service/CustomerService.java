package com.showroom.Service;

import com.showroom.Entity.Customer;
import com.showroom.Entity.Response;
import com.showroom.constants.UserType;

public interface CustomerService {
    public Response createCustomer(Customer customer, UserType userType);
    public Response updateCustomer(Customer customer);
    public Response deleteCustomerById(int id);
    public Response LogInCustomer(String name, String password) ;
    public Response getCustomerByNameAndPhoneNo(String name,String phoneNo);

    public Response findCustomerById(int id);

    public Response getAllCustomer(int pageNumber, int pageSize, String columnToSort,String sortDirection,String filterValue ) ;


    }
