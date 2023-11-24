package com.showroom.Entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    private Double total;
    private int additionalCharges;
    private int discount;
}
