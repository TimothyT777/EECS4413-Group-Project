package com.example.clothing4413.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.clothing4413.dto.AdminSalesOrderResponse;
import com.example.clothing4413.service.OrderService;

@RestController
@RequestMapping("/api/admin/sales-history")
public class AdminSalesController {

    private final OrderService orderService;

    public AdminSalesController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<AdminSalesOrderResponse> getSalesHistory(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return orderService.getSalesHistory(customerId, productId, startDate, endDate);
    }
}