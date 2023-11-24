package com.showroom.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.showroom.constants.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator")
    @SequenceGenerator(
            name = "sequence-generator",
            sequenceName = "customer_sequence",
            allocationSize = 1
    )
    @Column(name = "customer_id")
    private int id;

    @Column(name = "customer_name")
    @NotBlank(message = "name must not be blank")
    @Size(min = 3,message = "name should be of at least of 3 letters")
    private String name;

    @Column(name = "customer_phone_no")
    @NotBlank(message = "phone no. must not be blank")
    @Size(min = 10, max = 10,message = "Phone no should be of length 10")
    private String phoneNo;

    @Column(name = "customer_email")
    @NotBlank(message = "email must not be blank")
    @Size(min = 6 ,message = "Please enter valid email")
    private String email;

    @Column(name = "customer_address")
    @NotBlank(message = "address must not be blank")
    @Size(min = 3,message = "address should be of at least of 3 letters")
    private String address;

    @Column(name = "customer_password")
    @NotNull(message = "password must not be empty")
    @NotBlank(message = "password must not be blank")
    @Size(min = 8,message = "password should be of at least of 8 letters")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Customer(String name, String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
