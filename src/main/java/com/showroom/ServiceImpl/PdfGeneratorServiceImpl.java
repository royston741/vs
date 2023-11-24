package com.showroom.ServiceImpl;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.showroom.Entity.Customer;
import com.showroom.Entity.Order;
import com.showroom.Entity.ShowRoomDetails;
import com.showroom.Service.PdfGeneratorService;
import com.sun.mail.iap.ByteArray;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {
    @Override
    public byte[] generatePdf(Customer customer, Order order) throws FileNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileOutputStream fileOutputStream = new FileOutputStream("example.pdf");
        PdfWriter pdfWriter = new PdfWriter(fileOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        // heading
        document.add(((Paragraph) createParagraph("MyDrive")).setFontColor(Color.BLUE).setFontSize(40).setMarginBottom(10).setItalic());

        //page title
        document.add(((Paragraph) createParagraph("Billing Details")).setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(20));

        // Billing details
        document.add(((Paragraph) createParagraph("Bill id : " + order.getId())).setMargin(0).setPadding(0));
        document.add(((Paragraph) createParagraph("Bill date : " + LocalDate.now())).setMargin(0).setPadding(0));

        // Supplier details
        document.add(((Paragraph) createParagraph("Supplier details :")).setPadding(0).setMargin(0).setMarginTop(20).setBold());
//        document.add(((Paragraph) createParagraph("Supplier : MyDrive Pvt Ltd")).setPadding(0).setMargin(0));
//        document.add(((Paragraph) createParagraph("Address : \nOshiwara Mhada Complex, 40/102, 1st Floor,\n Sargam Society, New Link Rd,\n Andheri West, Mumbai, Maharashtra 400102, India")).setPadding(0).setMargin(0));

        Table showroomTable = new Table(new float[]{100, 100});
        ShowRoomDetails showroom=new ShowRoomDetails();
        Map<String, String> showroomTableColNames = new HashMap<>();
        showroomTableColNames.put("name ",showroom.getName());
        showroomTableColNames.put("Address ",showroom.getAddress());
        showroomTableColNames.forEach((k,v)->{
            showroomTable.addCell(createCell(k,Map.of("textAlign", TextAlignment.CENTER)));
            showroomTable.addCell(createCell(v,Map.of("textAlign", TextAlignment.CENTER)));
        });

        // Customer details
        document.add(((Paragraph) createParagraph("Customer details :")).setPadding(0).setMargin(0).setMarginTop(20).setBold());
        Table customerTable = new Table(new float[]{100, 100});
        Map<String, String> customerTableColNames = new HashMap<>();
        customerTableColNames.put("Customer name",customer.getName());
        customerTableColNames.put("Phone no", customer.getPhoneNo());
        customerTableColNames.put("Email",customer.getPhoneNo());
        customerTableColNames.put("Address",customer.getEmail());

        customerTableColNames.forEach((k,v)->{
            customerTable.addCell(createCell(k,Map.of("textAlign", TextAlignment.CENTER)));
            customerTable.addCell(createCell(v,Map.of("textAlign", TextAlignment.CENTER)));
        });
//        String customerDetails = "Name : " + customer.getName() + "\n" + "Phone no. : " + customer.getPhoneNo() + "\n" + "Email : " + customer.getEmail() + "\n" + "Address : " + customer.getAddress();
//        document.add(((Paragraph) createParagraph(customerDetails)).setMargin(0).setPadding(0));

        // order details
        Table table = new Table(new float[]{100, 100, 100, 100, 100, 300, 100, 300});
        Map<String, Map<String, Object>> colNames = new HashMap<>();
        colNames.put("Vehicle Name", Map.of("textAlign", TextAlignment.CENTER));
        colNames.put("Quantity", Map.of("textAlign", TextAlignment.CENTER));
        colNames.put("vehicle", Map.of("textAlign", TextAlignment.CENTER));
        colNames.put("Color", Map.of("textAlign", TextAlignment.CENTER));
        colNames.put("Initial price", Map.of("textAlign", TextAlignment.CENTER));
        colNames.put("Discount", Map.of("textAlign", TextAlignment.CENTER));
        colNames.put("additional Charges", Map.of("textAlign", TextAlignment.CENTER));
        colNames.forEach((k, v) -> {
            table.addHeaderCell(createCell(k, v));
        });
//        table.addHeaderCell( createCell("Vehicle name"));
//        table.addHeaderCell(createCell("Quantity"));
//        table.addHeaderCell(createCell("vehicle"));
//        table.addHeaderCell(createCell("Color"));
//        table.addHeaderCell(createCell("Fuel"));
//        table.addHeaderCell(createCell("Initial price"));
//        table.addHeaderCell(createCell("Discount"));
//        table.addHeaderCell(createCell("additional Charges"));

        order.getVehicles().forEach(vehicle -> {
            Map<String, Map<String, Object>> bodyColumn = new HashMap<>();
            bodyColumn.put(vehicle.getVehicleName(), Map.of("textAlign", TextAlignment.CENTER));
            bodyColumn.put(String.valueOf(vehicle.getQuantity()), Map.of("textAlign", TextAlignment.CENTER));
            bodyColumn.put(vehicle.getVehicleType().name(), Map.of("textAlign", TextAlignment.CENTER));
            bodyColumn.put(vehicle.getVehicleColor().name(), Map.of("textAlign", TextAlignment.CENTER));
            bodyColumn.put(String.valueOf(vehicle.getInitialPrice()), Map.of("textAlign", TextAlignment.CENTER));
            bodyColumn.put(String.valueOf(vehicle.getDiscount()), Map.of("textAlign", TextAlignment.CENTER));
            bodyColumn.put(String.valueOf(vehicle.getDiscount()), Map.of("textAlign", TextAlignment.CENTER));
            bodyColumn.forEach((k, v) -> {
                table.addCell(createCell(k, v));
            });
//            table.addCell(createCell(vehicle.getVehicleName()));
//            table.addCell(createCell(String.valueOf(vehicle.getQuantity())));
//            table.addCell(createCell(String.valueOf(vehicle.getVehicleType())));
//            table.addCell(createCell(String.valueOf(vehicle.getVehicleColor())));
//            table.addCell(createCell(vehicle.getFuelType().name(), null);
//            table.addCell(createCell(String.valueOf(vehicle.getInitialPrice())));
//            table.addCell(createCell(String.valueOf(vehicle.getDiscount())));
//            table.addCell(createCell(String.valueOf(vehicle.getAdditionalCharges())));
        });
        table.setMarginTop(30);
        table.setMarginBottom(20);
        document.add(table);
        // order total
        document.add(((Paragraph) createParagraph("Order total : Rs " + order.getOrderTotal())).setTextAlignment(TextAlignment.RIGHT).setFontSize(20));

        document.close();
        return byteArrayOutputStream.toByteArray();
    }


    private IBlockElement createParagraph(String text) {
        return new Paragraph(text);
    }

    private Cell createCell(String text, Map<String, Object> properties) {
        Cell cell = new Cell();
        cell.add(text);
        properties.forEach((k, v) -> {
            switch (k) {
                case "textAlignment" -> cell.setTextAlignment((TextAlignment) v);
            }
        });
        return cell;
//        return new Cell().add(text).setTextAlignment(TextAlignment.CENTER);
    }

}
