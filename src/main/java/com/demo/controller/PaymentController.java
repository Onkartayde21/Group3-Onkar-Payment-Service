package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.dto.PaymentDTO;
import com.demo.service.PaymentNotFoundException;
import com.demo.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process/{orderId}")
    public ResponseEntity<PaymentDTO> processPayment(@PathVariable Long orderId, @RequestBody PaymentDTO paymentDTO) {
        return paymentService.processPayment(orderId, paymentDTO);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long paymentId) {
        return paymentService.getPaymentById(paymentId);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/status/{paymentStatus}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@PathVariable String paymentStatus) {
        return paymentService.getPaymentsByStatus(paymentStatus);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<Void> updatePayment(@PathVariable Long paymentId, @RequestBody PaymentDTO paymentDTO) {
        return paymentService.updatePayment(paymentId, paymentDTO);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long paymentId) {
        return paymentService.deletePayment(paymentId);
    }
    
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFounException(PaymentNotFoundException e)
    {
    	return ResponseEntity.status(404).body(e.getMessage());
    }
}
