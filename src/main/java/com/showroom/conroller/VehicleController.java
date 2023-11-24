package com.showroom.conroller;

import com.showroom.Entity.MiscellaneousCost;
import com.showroom.Entity.Response;
import com.showroom.Service.VehicleService;
import com.showroom.constants.Color;
import com.showroom.constants.FuelType;
import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @GetMapping("getAllVehiclesByCustomerNameAndPhoneNo")
    public ResponseEntity<Response> getAllVehiclesByCustomerNameAndPhoneNo(@RequestParam(name = "name", defaultValue = "") String name,
                                                                           @RequestParam(name = "phoneNo", defaultValue = "") String phoneNo,
                                                                           @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                                           @RequestParam(name = "size", defaultValue = "5") int pageSize,
                                                                           @RequestParam(name = "column", defaultValue = "name") String column,
                                                                           @RequestParam(name = "direction", defaultValue = "ASC") String direction,
                                                                           @RequestParam(name = "filter", defaultValue = "") String filterValue
    ) {
        Response response = vehicleService.getAllVehiclesByCustomerNameAndPhoneNo(name, phoneNo, pageNumber, pageSize, column, direction, filterValue);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("getVehicleById")
    public ResponseEntity<Response> getVehicleById(@RequestParam(name = "vehicleId") int id) {
        Response response = vehicleService.getVehicleById(id);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("getVehiclesByVehicleTypeOrName")
    public ResponseEntity<Response> getVehiclesByVehicleType(@RequestParam(name = "type", defaultValue = "") VehicleType type, @RequestParam(name = "name", defaultValue = "") String name) {
        Response response = vehicleService.getVehiclesByVehicleTypeOrName(type, name);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("getAllVehicles")
    public ResponseEntity<Response> getAllVehicles() {
        Response response = vehicleService.getAllVehicle();
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getExtraChargeByColor")
    public ResponseEntity<Response> getExtraChargeByColor() {
        List<MiscellaneousCost> colorCharges = Arrays.stream(Color.values()).map(c -> new MiscellaneousCost(c.name(), Double.toString(c.getCharges()))).toList();
        Response response = new Response(true, new ArrayList<>(), colorCharges);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getDiscountByFuelType")
    public ResponseEntity<Response> getDiscountByFuelType() {
        List<MiscellaneousCost> fuelTypeDiscount = Arrays.stream(FuelType.values()).map(c -> new MiscellaneousCost(c.name(), Double.toString(c.getDiscount()))).toList();
        Response response = new Response(true, new ArrayList<>(), fuelTypeDiscount);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getOrderIdOfVehicleId/{id}")
    public ResponseEntity<Response> getOrderIdOfVehicleId(@PathVariable int id) {
        Response response = vehicleService.getOrderIdOfVehicleId(id);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("getExelOfOrderedVehicle")
    public ResponseEntity<Response> generateExcelOfOrderedVehicle(@RequestParam(name = "vehicleType", defaultValue = "") VehicleType vehicleType,
                                                                  @RequestParam(name = "twoWheelerType", defaultValue = "") TwoWheelerType twoWheelerType,
                                                                  @RequestParam(name = "fuelType", defaultValue = "") FuelType fuelType,
                                                                  @RequestParam(name = "color", defaultValue = "") Color color) {
        Response response = vehicleService.generateExcelOfOrderedVehicle(vehicleType, twoWheelerType, fuelType, color);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
