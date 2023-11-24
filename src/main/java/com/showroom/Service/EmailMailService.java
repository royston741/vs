package com.showroom.Service;

public interface EmailMailService {
    public void sendMail(String toMail,String subject,String message,byte[] attachment);
}
