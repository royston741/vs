package com.showroom.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.showroom.constants.Color;
import com.showroom.constants.FuelType;
import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator-v")
    @SequenceGenerator(
            name = "sequence-generator-v",
            sequenceName = "vehicle_sequence",
            allocationSize = 1
    )
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private int id;

    @Column(name = "vehicle_name")
    @NotBlank(message = "name must not be blank")
    @Size(min = 3,message = "name should be of at least of 3 letters")
    private String vehicleName;

    @Column(name = "vehicle_price")
//    @NotBlank(message = "price must not be blank")
    private Double price;

    @Column(name = "vehicle_model_no")
    private String vehicleModelNo;

    @Column(name = "vehicle_quantity")
//    @NotBlank(message = "quantity must not be blank")
    private int quantity;

    @Temporal(TemporalType.DATE)
    @Column(name = "vehicle_manufacture_date")
    private Date manufactureDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
//    @NotBlank(message = "Vehicle Type must not be blank")
//    @Size(min = 3,message = "Vehicle Type should be of at least of 3 letters")
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_color")
//    @NotBlank(message = "Vehicle Color must not be blank")
//    @Size(min = 3,message = "Vehicle Color should be of at least of 3 letters")
    private Color vehicleColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_fuel_type")
//    @NotBlank(message = "Vehicle Fuel Type must not be blank")
//    @Size(min = 3,message = "Vehicle Fuel Type should be of at least of 3 letters")
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "two_wheeler_type")
//    @NotBlank(message = "Two wheeler Type must not be blank")
//    @Size(min = 3,message = "Two wheeler Type should be of at least of 3 letters")
    private TwoWheelerType twoWheelerType;

    @Transient
    private double discount;

    @Transient
    private double additionalCharges;

    @Transient
    private double initialPrice;

    @JsonIgnore
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    public Vehicle(String vehicleName, Double price, int quantity, VehicleType vehicleType, Color vehicleColor, FuelType fuelType, TwoWheelerType twoWheelerType, Order order) {
        this.vehicleName = vehicleName;
        this.price = price;
        this.quantity = quantity;
        this.vehicleType = vehicleType;
        this.vehicleColor = vehicleColor;
        this.fuelType = fuelType;
        this.twoWheelerType = twoWheelerType;
        this.order = order;
    }

    @Override
    public String toString() {
        return "\nVehicle{" +
                "\n\t id=" + id +
                ",\n\t vehicleName='" + vehicleName + '\'' +
                ",\n\t price=" + price +
                ",\n\t vehicleModelNo='" + vehicleModelNo + '\'' +
                ",\n\t manufactureDate=" + manufactureDate +
                ",\n\t vehicleType=" + vehicleType +
                ",\n\t vehicleColor=" + vehicleColor +
                ",\n\t fuelType=" + fuelType +
                ",\n\t twoWheelerType=" + twoWheelerType +
                "\n}";
    }
}
