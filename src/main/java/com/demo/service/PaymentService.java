package com.demo.service;

import org.springframework.http.ResponseEntity;

import com.demo.dto.PaymentDTO;

import java.util.List;



public interface PaymentService {

    ResponseEntity<PaymentDTO> processPayment(Long orderId, PaymentDTO paymentDTO);

    ResponseEntity<PaymentDTO> getPaymentById(Long paymentId);

    ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(String paymentStatus);

    ResponseEntity<List<PaymentDTO>> getAllPayments();

    ResponseEntity<Void> updatePayment(Long paymentId, PaymentDTO paymentDTO);

    ResponseEntity<Void> deletePayment(Long paymentId);
}

