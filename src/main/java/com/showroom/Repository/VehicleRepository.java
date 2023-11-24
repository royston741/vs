package com.showroom.Repository;

import com.showroom.Entity.Customer;
import com.showroom.Entity.Vehicle;
import com.showroom.constants.Color;
import com.showroom.constants.FuelType;
import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    @Query("from Vehicle v where order.customer.name=:name AND order.customer.phoneNo=:phoneNo")
    Page<Vehicle> getAllVehiclesByCustomerNameAndPhoneNo(@Param("name") String name, @Param("phoneNo") String phoneNo, Pageable page);
    public List<Vehicle> findVehicleByVehicleType(VehicleType type);
    public List<Vehicle> findVehicleByTwoWheelerType(TwoWheelerType type);
    public List<Vehicle> findVehicleByFuelType(FuelType type);
    public List<Vehicle> findVehicleByVehicleColor(Color color);

    public List<Vehicle> findVehicleByVehicleName(String name);

    @Query("from Vehicle v where order.customer.name=:name AND order.customer.phoneNo=:phoneNo AND v.vehicleName=:vehicleName")
    Page<Vehicle> findAllVehiclesByCustomerNameAndPhoneNoAndVehicleName(@Param("name") String name, @Param("phoneNo") String phoneNo,@Param("vehicleName") String vehicleName,  Pageable page);

    @Query(value = "Select order_id from showroom2.vehicle where vehicle_id=:id",nativeQuery = true)
    public int getOrderIfOfVehicle(@Param("id") int id);

}
