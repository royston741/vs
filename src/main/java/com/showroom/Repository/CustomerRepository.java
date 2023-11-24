package com.showroom.Repository;

import com.showroom.Entity.Customer;
import com.showroom.constants.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("select (count(id)>0) from Customer where name= :name and phoneNo= :phoneNo")
    boolean checkIfExistByNameAndPhoneNo(@Param("name") String name, @Param("phoneNo") String phoneNo);
    Optional<Customer> findByNameAndPhoneNo(String name, String phoneNo);
    Optional<Customer> findByNameAndPassword(String name, String password);
    Page<Customer> findAllByUserType(UserType userType, Pageable page);
    Page<Customer> findAllByNameOrPhoneNoOrAddressAndUserType(String name,String phoneNo,String filterValue,UserType userType,Pageable page);
}
