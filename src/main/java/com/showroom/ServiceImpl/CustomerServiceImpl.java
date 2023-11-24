package com.showroom.ServiceImpl;

import com.showroom.Entity.Customer;
import com.showroom.Entity.Response;
import com.showroom.Repository.CustomerRepository;
import com.showroom.Service.CustomerService;
import com.showroom.constants.UserType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
//    CustomerDao customerDao = new CustomerDao();
//    private static final Logger log = Logger.getLogger(ValidationUtil.class);

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Response createCustomer(Customer customer,UserType userType) {
        Response response = new Response(false);
        try {
            // check if exist
            boolean existingCustomer = customerRepository.checkIfExistByNameAndPhoneNo(customer.getName(), customer.getPhoneNo());
            // if does not exist
            if (existingCustomer) {
                response.getErrMessage().add("Customer already exist");
            } else {
                customer.setUserType(userType);
                //save customer
                Customer insertedCustomer = customerRepository.save(customer);
                response.setResponseData(insertedCustomer);
                response.setSuccess(true);
            }
        } catch (Exception e) {
            response.getErrMessage().add("Customer not added");
            if (e.getCause().getCause() instanceof ConstraintViolationException cv) {
                response.getErrMessage().addAll(cv.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
            } else {
                log.error("Error in createCustomer {}", e);
            }
        }
        return response;
    }


    @Override
    public Response updateCustomer(Customer customer) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Customer> existingCustomer = customerRepository.findById(customer.getId());
            // if exist
            if (existingCustomer.isPresent()) {
                //update customer
                Customer updatedCustomer = customerRepository.save(customer);
                response.setResponseData(updatedCustomer);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Customer does not exist");
            }
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ConstraintViolationException cv) {
                response.getErrMessage().addAll(cv.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
            }
            response.getErrMessage().add("Customer not updated");
            log.error("Error in createCustomer {}", e);
        }
        return response;
    }

    @Override
    public Response deleteCustomerById(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Customer> customer = customerRepository.findById(id);
            // if exist
            if (customer.isPresent()) {
                customerRepository.delete(customer.get());
                response.setResponseData("customer deleted");
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Customer does not exist with id " + id);
            }
        } catch (Exception e) {
            response.getErrMessage().add("Customer not deleted");
            log.error("Error in deleteCustomerById {}", e);
        }
        return response;
    }

    @Override
    public Response LogInCustomer(String name, String password) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Customer> customer = customerRepository.findByNameAndPassword(name, password);
            // if exist
            if (customer.isPresent()) {
                response.setResponseData(customer);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Invalid credentials");
            }
        } catch (Exception e) {
            response.getErrMessage().add("Customer not found");
            log.error("Error in findCustomerById {}", e);
        }
        return response;
    }

    @Override
    public Response getCustomerByNameAndPhoneNo(String name, String phoneNo) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Customer> customer = customerRepository.findByNameAndPhoneNo(name, phoneNo);
            // if exist
            if (customer.isPresent()) {
                response.setResponseData(customer);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Customer does not exist");
            }
        } catch (Exception e) {
            response.getErrMessage().add("Customer not found");
            log.error("Error in findCustomerById {}", e);
        }
        return response;
    }

    @Override
    public Response findCustomerById(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Customer> customer = customerRepository.findById(id);
            // if exist
            if (customer.isPresent()) {
                response.setResponseData(customer);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Customer does not exist");
            }
        } catch (Exception e) {
            response.getErrMessage().add("Customer not found");
            log.error("Error in findCustomerById {}", e);
        }
        return response;
    }

    @Override
    public Response getAllCustomer(int pageNumber, int pageSize, String columnToSort, String sortDirection, String filterValue) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            Sort sort = Sort.by(columnToSort);
            if (sortDirection.equals("DESC")) {
                sort = Sort.by(Sort.Direction.DESC, columnToSort);
            }

            Pageable page = PageRequest.of(pageNumber, pageSize, sort);

            Page<Customer> customerPage = customerRepository.findAllByUserType(UserType.CUSTOMER,page);
            response.setResponseData(customerPage);
            response.setSuccess(true);

            if (filterValue.length() > 0) {
                customerPage = customerRepository.findAllByNameOrPhoneNoOrAddressAndUserType(filterValue, filterValue, filterValue,UserType.CUSTOMER, page);
                if (customerPage.getTotalElements() == 0) {
                    response.setResponseData(customerPage);
                    response.getErrMessage().add("no entry by filter " + filterValue);
                    response.setSuccess(false);
                } else {
                    response.setResponseData(customerPage);
                    response.setSuccess(true);
                }
            }

        } catch (Exception e) {
            response.getErrMessage().add("Customers not found");
            log.error("Error in getAllCustomer {}", e);
        }
        return response;
    }


//    @Override
//    public void deleteCustomer(Customer customer) {
//        customerDao.deleteCustomer(customer);
//    }
//
//    @Override
//    public Customer getCustomer(Customer customer) {
//        Customer existingCustomer = customerDao.getCustomerByNameAndPassword(customer.getName(), customer.getPhoneNo());
//        return existingCustomer;
//    }
//
//    @Override
//    public Customer findCustomerById(int id) {
//        return customerDao.getCustomerById(id);
//    }


//    public boolean checkCustomerValidity(Customer customer) {
//        return (customer.getName()!= null
//                && customer.getPhoneNo() != null
//                && (!customer.getName().matches("\\d+")));
//    }
}
