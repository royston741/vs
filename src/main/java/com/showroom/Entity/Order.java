package com.showroom.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator-o")
    @SequenceGenerator(
            name = "sequence-generator-o",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @Column(name = "order_id")
    private int id;

    @Column(name = "order_total")
    private double orderTotal;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    //    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
//    @JoinTable(name = "order_vehicle",
//            joinColumns =
//                    {@JoinColumn(name = "order_id", referencedColumnName = "order_id")},
//            inverseJoinColumns =
//                    {@JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")})
    @OneToMany( fetch = FetchType.LAZY,mappedBy = "order",orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    public void addItem(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void removeItem(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    @Override
    public String toString() {
        return "\nOrder{" +
                ",\n\tid=" + id +
                ",\n\t orderTotal=" + orderTotal +
                ",\n\t vehicles=" + vehicles +
                "\n}";
    }
}
