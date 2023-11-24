package com.showroom.ServiceImpl;


import com.showroom.Entity.AvailableVehicle;
import com.showroom.Entity.Customer;
import com.showroom.Entity.Response;
import com.showroom.Entity.Vehicle;
import com.showroom.Repository.AvailableVehicleRepository;
import com.showroom.Service.AvailableVehicleService;
import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;
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

@Slf4j
@Service
public class AvailableVehicleServiceImpl implements AvailableVehicleService {

    @Autowired
    AvailableVehicleRepository availableVehicleRepository;

    public Response getAllAvailableVehicleVehicle(String columnToSort, String sortDirection, VehicleType vehicleType, TwoWheelerType twoWheelerType, Double startPrice, Double endPrice) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            Sort sort = Sort.by(sortDirection.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, columnToSort);

//            Pageable page = PageRequest.of(pageNumber, pageSize, sort);
            List<AvailableVehicle> availableVehicleList = availableVehicleRepository.findAll(sort);

            if (startPrice >= 0 && endPrice >= 0) {
                availableVehicleList = availableVehicleRepository.findAllByPriceBetween(startPrice, endPrice,sort);
            }

            if(vehicleType!=null){
                availableVehicleList = availableVehicleRepository.findAllByPriceBetweenAndVehicleType(startPrice, endPrice,vehicleType,sort);
                if(twoWheelerType!=null){
                    availableVehicleList = availableVehicleRepository.findAllByPriceBetweenAndVehicleTypeAndTwoWheelerType(startPrice, endPrice,vehicleType,twoWheelerType,sort);
                }
            }

            if (availableVehicleList.size() > 0) {
                response.setResponseData(availableVehicleList);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("No vehicles to show");
            }
        } catch (Exception e) {
            response.getErrMessage().add("Vehicle not found");
            log.error("Error in getAllVehicle {}", e);
        }
        return response;
    }

    ;


    public Response getVehicleById(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<AvailableVehicle> vehicle = availableVehicleRepository.findById(id);
            // if exist
            if (vehicle.isPresent()) {
                response.setResponseData(vehicle);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("Vehicle does not exist");
            }
        } catch (Exception e) {
            response.getErrMessage().add("Vehicle not found");
            log.error("Error in getVehicleById {}", e);
        }
        return response;
    }

    public Response getMaxAndMinPriceOAvailableVehicles() {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Double maxPrice = availableVehicleRepository.findHighestPrice();
            Double minPrice = availableVehicleRepository.findLowestPrice();
            response.setResponseData(List.of(maxPrice,minPrice));
            response.setSuccess(true);
        } catch (Exception e) {
            response.getErrMessage().add("No value");
            log.error("Error in getMaxAndMinPriceOAvailableVehicles {}", e);
        }
        return response;
    }

}
