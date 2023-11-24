package com.showroom.Service;

import com.showroom.Entity.Order;
import com.showroom.Entity.Response;
import com.showroom.Entity.Vehicle;
import com.showroom.constants.Color;
import com.showroom.constants.FuelType;
import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;

import java.util.List;

public interface VehicleService {
    public Vehicle createVehicle(Vehicle vehicle, Order order);

    public Response getVehicleById(int id);

    public Response getVehiclesByVehicleTypeOrName(VehicleType vehicleType,String name);

    public Response getAllVehicle();

    public Response getAllVehiclesByCustomerNameAndPhoneNo(String name,String phoneNo,int pageNumber,int pageSize,String columnToSort, String sortDirection,String filterValue);

    public void printVehicles(Vehicle vehicle);

    public void deleteVehicle(Vehicle vehicle);

    public Response getOrderIdOfVehicleId(int id);

    public Response generateExcelOfOrderedVehicle(VehicleType vehicleType, TwoWheelerType twoWheelerType, FuelType fuelType, Color color) ;
    }