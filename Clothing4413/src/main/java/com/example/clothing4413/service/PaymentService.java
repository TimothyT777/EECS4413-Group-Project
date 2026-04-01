package com.example.clothing4413.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private int requestCount = 0; //Going off the outline, where we deny a payment every 3 or so requests

    public boolean processPayment(String cardNumber, String cardExpiry, String cardHolderName, double cost) {
        requestCount++;

        if (requestCount % 3 == 0) { //Reject every third request
            return false;
        }

        //Info Validation
        if (cardNumber == null) {
            return false;
        }
        if (cardExpiry == null) {
            return false;
        }
        if (cardHolderName == null) {
            return false;
        }

        return true;
    }
}