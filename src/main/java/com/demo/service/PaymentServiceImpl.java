package com.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.demo.dto.PaymentDTO;
import com.demo.entity.Payment;
import com.demo.entity.PaymentStatus;
import com.demo.repository.PaymentRepository;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<PaymentDTO> processPayment(Long orderId, PaymentDTO paymentDTO) {

        // Check if the payment amount is positive
        if (paymentDTO.getAmount() <= 0) {
            return null;
        }

        // Assuming a successful payment
        paymentDTO.setPaymentStatus(PaymentStatus.SUCCESS);

        // Save the payment to the database
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        Payment savedPayment = paymentRepository.save(payment);

        PaymentDTO savedPaymentDTO = modelMapper.map(savedPayment, PaymentDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentDTO);
    }

    @Override
    public ResponseEntity<PaymentDTO> getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));

        PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);
        return ResponseEntity.ok(paymentDTO);
    }

    @Override
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(String paymentStatus) {
        try {
            PaymentStatus status = PaymentStatus.valueOf(paymentStatus.toUpperCase());

            List<Payment> payments = paymentRepository.findByPaymentStatus(status);
            List<PaymentDTO> paymentDTOs = payments.stream()
                    .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(paymentDTOs);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentDTO> paymentDTOs = payments.stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(paymentDTOs);
    }

    @Override
    public ResponseEntity<Void> updatePayment(Long paymentId, PaymentDTO paymentDTO) {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));

        // Update existingPayment fields with values from paymentDTO
        modelMapper.map(paymentDTO, existingPayment);

        paymentRepository.save(existingPayment);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
        return ResponseEntity.noContent().build();
    }
}
