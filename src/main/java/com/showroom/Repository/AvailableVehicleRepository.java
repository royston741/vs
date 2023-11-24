package com.showroom.Repository;

import com.showroom.Entity.AvailableVehicle;
import com.showroom.constants.FuelType;
import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableVehicleRepository extends JpaRepository<AvailableVehicle,Integer> {

    public List<AvailableVehicle> findAllByPriceBetween(Double startPrice, Double endPrice, Sort sort);

    public List<AvailableVehicle> findAllByPriceBetweenAndVehicleType(Double startPrice, Double endPrice, VehicleType vehicleType, Sort sort);

    public List<AvailableVehicle> findAllByPriceBetweenAndVehicleTypeAndTwoWheelerType(Double startPrice, Double endPrice, VehicleType vehicleType, TwoWheelerType twoWheelerType, Sort sort);

    @Query("select max(price) from AvailableVehicle")
    public Double findHighestPrice();

    @Query("select min(price) from AvailableVehicle")
    public Double findLowestPrice();
}
