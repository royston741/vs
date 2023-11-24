package com.showroom.Entity;

import com.showroom.constants.TwoWheelerType;
import com.showroom.constants.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AvailableVehicle")
public class AvailableVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator-v")
    @SequenceGenerator(
            name = "sequence-generator-v",
            sequenceName = "vehicle_sequence",
            allocationSize = 1
    )
    @Column(name = "vehicle_id")
    private int id;

    @Column(name = "vehicle_name")
    private String vehicleName;

    @Column(name = "vehicle_img_url")
    private String imgUrl;

    @Column(name = "vehicle_price")
    private Double price;

    @Column(name = "rating")
    private float rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "two_wheeler_type")
    private TwoWheelerType twoWheelerType;
}
