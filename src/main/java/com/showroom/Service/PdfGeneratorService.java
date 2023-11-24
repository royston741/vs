package com.showroom.Service;

import com.showroom.Entity.Customer;
import com.showroom.Entity.Order;

import java.io.FileNotFoundException;

public interface PdfGeneratorService {

    public byte[] generatePdf(Customer customer, Order order) throws FileNotFoundException;
}
