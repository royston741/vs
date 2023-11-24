package com.showroom.Repository;

import com.showroom.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("from Order o where o.customer.name=:name and o.customer.phoneNo=:phoneNo")
    public List<Order> getOrderByCustomerNameAndPassword(@Param("name") String name, @Param("phoneNo") String phoneNo);

    @Query("from Order o where o.customer.id=:id")
    public List<Order> getOrderByCustomerId(@Param("id") int id);
}
