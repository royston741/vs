package com.showroom.ServiceImpl;

import com.itextpdf.layout.element.Cell;
import com.showroom.Entity.*;
import com.showroom.Repository.VehicleRepository;
import com.showroom.Service.VehicleService;
import com.showroom.constants.Color;
import com.showroom.constants.FuelType;
import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public Vehicle createVehicle(Vehicle vehicle, Order order) {
        Vehicle newVehicle = new Vehicle();
        try {
            vehicle.setOrder(order);
            vehicle.setVehicleModelNo(vehicle.getVehicleName().split(" ")[0] + "1234");
            vehicle.setManufactureDate(new Date());
            vehicle.setInitialPrice(vehicle.getPrice());
            Price vehiclePriceCalculation = calculateTotalPriceOfVehicle(vehicle);
            double newPrice = calculateTotalPriceOfVehicle(vehicle).getTotal();
            double discount = vehiclePriceCalculation.getDiscount();
            double additionalCharges = vehiclePriceCalculation.getAdditionalCharges();
            vehicle.setDiscount(discount);
            vehicle.setAdditionalCharges(additionalCharges);
//            newPrice *= vehicle.getQuantity();
            vehicle.setPrice(newPrice);
            newVehicle = vehicleRepository.save(vehicle);
        } catch (Exception e) {
            log.error("Error in createVehicle {}", e);
        }
        return newVehicle;
    }

    @Override
    public Response getVehicleById(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        System.out.println(id);
        try {
            // check if exist
            Optional<Vehicle> vehicle = vehicleRepository.findById(id);
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

    @Override
    public Response getVehiclesByVehicleTypeOrName(VehicleType vehicleType, String name) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            List<Vehicle> vehicleList = new ArrayList();
            if (vehicleType != null) {
                vehicleList = vehicleRepository.findVehicleByVehicleType(vehicleType);
            } else if (name != null) {
                vehicleList = vehicleRepository.findVehicleByVehicleName(name);
            }
            if (vehicleList.size() > 0) {
                response.setResponseData(vehicleList);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("No vehicles to show");
            }
        } catch (Exception e) {
            response.getErrMessage().add("Vehicles not found");
            log.error("Error in getVehiclesByVehicleType {}", e);
        }
        return response;
    }

    @Override
    public Response getAllVehicle() {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            List<Vehicle> vehicleList = vehicleRepository.findAll();
            if (vehicleList.size() > 0) {
                response.setResponseData(vehicleList);
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

    @Override
    public Response getAllVehiclesByCustomerNameAndPhoneNo(String name, String phoneNo, int pageNumber, int pageSize, String columnToSort, String sortDirection, String filterValue) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            Sort sort = Sort.by(columnToSort);
            if (sortDirection.equals("DESC")) {
                sort = Sort.by(Sort.Direction.DESC, columnToSort);
            }
            Pageable page = PageRequest.of(pageNumber, pageSize, sort);
            Page<Vehicle> vehiclePage = vehicleRepository.getAllVehiclesByCustomerNameAndPhoneNo(name, phoneNo, page);
            response.setResponseData(vehiclePage);
            response.setSuccess(true);

            if (filterValue.length() > 0) {
                vehiclePage = vehicleRepository.findAllVehiclesByCustomerNameAndPhoneNoAndVehicleName(name, phoneNo, filterValue, page);
                if (vehiclePage.getTotalElements() == 0) {
                    response.setResponseData(vehiclePage);
                    response.getErrMessage().add("no entry by filter " + filterValue);
                    response.setSuccess(false);
                } else {
                    response.setResponseData(vehiclePage);
                    response.setSuccess(true);
                }
            }
        } catch (Exception e) {
            response.getErrMessage().add("Vehicle not found");
            log.error("Error in getAllVehiclesByCustomerNameAndPhoneNo {}", e);
        }
        return response;
    }

    @Override
    public void deleteVehicle(Vehicle vehicle) {
        try {
            vehicleRepository.delete(vehicle);
        } catch (Exception e) {
            log.error("Error in deleteVehicle {}", e);
        }
    }

    public Price calculateTotalPriceOfVehicle(Vehicle vehicle) {
        // get the price of vehicle
        double vehiclePrice = vehicle.getPrice();
        // add charges as per color
        double additionalChargesOfColor = vehicle.getVehicleColor().getAdditionalCharges(vehiclePrice);
        // add discount
        double discount = 0;
        switch (vehicle.getVehicleType()) {
            case CAR -> discount = vehicle.getFuelType().getDiscountedPrice(vehiclePrice);
            case BIKE -> discount = vehicle.getTwoWheelerType().getDiscountedPrice(vehiclePrice);
        }
//        System.out.println(discount);
//        System.out.println(additionalChargesOfColor);
//        System.out.println(vehiclePrice);
        vehiclePrice += additionalChargesOfColor;
//        System.out.println(vehiclePrice);
        vehiclePrice -= discount;
        double finalPrice = vehiclePrice * vehicle.getQuantity();

        Price price = new Price(finalPrice, (int) additionalChargesOfColor, (int) discount);
        return price;
    }

    public Response getOrderIdOfVehicleId(int id) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            // check if exist
            Optional<Vehicle> vehicle = vehicleRepository.findById(id);
            // if exist
            if (vehicle.isPresent()) {
                int orderId = vehicleRepository.getOrderIfOfVehicle(id);
                response.setResponseData(orderId);
                response.setSuccess(true);
            } else {
                response.getErrMessage().add("vehicle does not exist");
            }
        } catch (Exception e) {
            response.getErrMessage().add("vehicle not found");
            log.error("Error in getOrderIdFromVehicleId {}", e);
        }
        return response;
    }

    @Override
    public Response generateExcelOfOrderedVehicle(VehicleType vehicleType, TwoWheelerType twoWheelerType, FuelType fuelType, Color color) {
        Response response = new Response(false, new ArrayList<>(), null);
        try {
            List<Vehicle> orderedVehicles = new ArrayList<>();
            orderedVehicles = vehicleRepository.findAll();
            if (vehicleType != null) {
                if (twoWheelerType != null) {
                    orderedVehicles = vehicleRepository.findVehicleByTwoWheelerType(twoWheelerType);
                } else {
                    // findAll by vehicle type
                    orderedVehicles = vehicleRepository.findVehicleByVehicleType(vehicleType);
                }
            } else if (fuelType != null) {
                //find all by fuel type
                orderedVehicles = vehicleRepository.findVehicleByFuelType(fuelType);
            } else if (color != null) {
                // find all by color
                orderedVehicles = vehicleRepository.findVehicleByVehicleColor(color);
            }

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("ordered vehicles");

            HSSFRow row = sheet.createRow(0);
            List<String> rowHeader=List.of("Vehicle Name","price","quantity","Vehicle type","Vehicle color","Fuel type","Two wheeler type");


//            rowHeader.forEach((header)->{
//            });

            int rowHeaderCellIndex=0;
            while (rowHeaderCellIndex<rowHeader.size()){
                row.createCell(rowHeaderCellIndex).setCellValue(rowHeader.get(rowHeaderCellIndex));
                rowHeaderCellIndex++;
            }

            int rowDataIndex=1;
            for (Vehicle vehicle:orderedVehicles){
                HSSFRow dataRow = sheet.createRow(rowDataIndex);
                dataRow.createCell(0).setCellValue(vehicle.getVehicleName());
                dataRow.createCell(1).setCellValue(vehicle.getPrice());
                dataRow.createCell(2).setCellValue(vehicle.getQuantity());
                dataRow.createCell(3).setCellValue(String.valueOf(vehicle.getVehicleType()));
                dataRow.createCell(4).setCellValue(String.valueOf(vehicle.getVehicleColor()));
                dataRow.createCell(5).setCellValue(String.valueOf(vehicle.getFuelType()));
                dataRow.createCell(6).setCellValue(String.valueOf(vehicle.getTwoWheelerType()));
                rowDataIndex++;
            }

//            row.createCell(0).setCellValue("quantity");
//            row.createCell(0);
//            row.createCell(0);
//            row.createCell(0);
            FileOutputStream out = new FileOutputStream(
                    "C:/Users/r.rodrigues/Downloads/order-items.xls");

            workbook.write(out);
            out.close();

            response.setResponseData(orderedVehicles);
            response.setSuccess(true);
        } catch (Exception e) {
            log.error("Error in generateExcelOfOrderedVehicle ", e);
        }
        return response;
    }

//    public Cell geneRateCell(int i, String cellName){
//
//    }
//    @Override
//    public Vehicle getVehicleById(int id) {
//        return vehicleDao.getVehicleById(id);
//    }
//
//    @Override
//    public List<Vehicle> getVehicleByVehicleName(String name) {
//        List<Vehicle> vehicles = null;
//        // if vehicle type selected
//        if (name != null) {
//            vehicles = vehicleDao.getAllVehicleByVehicleName(name);
//            log.info("Name    |     " +
//                    "Quantity    |     " +
//                    "Price    |     " +
//                    "Model no.     |     " +
//                    "Color       |    " +
//                    "Fuel type     |   " +
//                    "Two wheeler    |    "
//            );
//            vehicles.forEach(vehicle -> printVehicles(vehicle));
//        }
//        return vehicles;
//    }
//
//    @Override
//    public List<Vehicle> getVehiclesByVehicleType(VehicleType vehicleType) {
//        List<Vehicle> vehicles = null;
//        // if vehicle type selected
//        if (vehicleType != null) {
//            vehicles = vehicleDao.getVehiclesByVehicleType(vehicleType);
//            log.info("Name    |     " +
//                    "Quantity    |     " +
//                    "Price    |     " +
//                    "Model no.     |     " +
//                    "Color       |    " +
//                    "Fuel type     |   " +
//                    "Two wheeler    |    "
//            );
//            vehicles.forEach(vehicle -> printVehicles(vehicle));
//        }
//        return vehicles;
//    }
//
//    @Override
//    public List<Vehicle> getAllVehicle() {
//        List<Vehicle> vehicles = vehicleDao.getAllVehicle();
//        log.info("Name    |     " +
//                "Quantity    |     " +
//                "Price    |     " +
//                "Model no.     |     " +
//                "Color       |    " +
//                "Fuel type     |   " +
//                "Two wheeler    |    "
//        );
//        vehicles.forEach(vehicle -> printVehicles(vehicle));
//        return vehicles;
//    }

    @Override
    public void printVehicles(Vehicle vehicle) {
        String twoWheelerType = vehicle.getTwoWheelerType() != null ? String.valueOf(vehicle.getTwoWheelerType()) : "";
        log.info(vehicle.getVehicleName() + "        " +
                vehicle.getQuantity() + "        " +
                vehicle.getPrice() + "        " +
                vehicle.getVehicleModelNo() + "        " +
                vehicle.getVehicleColor() + "        " +
                vehicle.getFuelType() + "        " +
                twoWheelerType);
    }

}
