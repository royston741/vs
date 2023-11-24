package com.showroom.conroller;


import com.showroom.Entity.AvailableVehicle;
import com.showroom.Entity.Response;
import com.showroom.Service.AvailableVehicleService;
import com.showroom.ServiceImpl.AvailableVehicleServiceImpl;
import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("availableVehicle")
public class AvailableVehicleController {

    @Autowired
    AvailableVehicleService availableVehicleService;

    @GetMapping("getAllVehicles")
    public ResponseEntity<Response> getAllVehicles(
            @RequestParam(name = "column", defaultValue = "id") String column,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            @RequestParam(name = "vehicleType",defaultValue = "") VehicleType vehicleType,
            @RequestParam(name = "twoWheelerType",defaultValue = "") TwoWheelerType twoWheelerType,
            @RequestParam(name = "startPrice",defaultValue = "0") Double startPrice,
            @RequestParam(name = "endPrice",defaultValue = "0") Double endPrice) {
        Response response = availableVehicleService.getAllAvailableVehicleVehicle(column, direction,vehicleType,twoWheelerType,startPrice,endPrice);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("getVehicleById")
    public ResponseEntity<Response> getVehicleById(@RequestParam(name = "vehicleId") int id) {
        Response response = availableVehicleService.getVehicleById(id);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("getMaxAndMinPrice")
    public ResponseEntity<Response> getMaxAndMinPrice() {
        Response response = availableVehicleService.getMaxAndMinPriceOAvailableVehicles();
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
