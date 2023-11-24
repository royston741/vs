package com.showroom.Service;

import com.showroom.Entity.Response;
import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;

public interface AvailableVehicleService {
    public Response getAllAvailableVehicleVehicle(String columnToSort, String sortDirection, VehicleType vehicleType, TwoWheelerType twoWheelerType,Double startPrice,Double endPrice);

    public Response getVehicleById(int id) ;
    public Response getMaxAndMinPriceOAvailableVehicles() ;
    }
