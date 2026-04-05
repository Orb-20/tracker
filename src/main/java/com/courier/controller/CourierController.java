package com.courier.controller;

import com.courier.dto.CourierRequest;
import com.courier.dto.StatusUpdateRequest;
import com.courier.model.Courier;
import com.courier.service.CourierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courier")
public class CourierController {

    private final CourierService courierService;

    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @PostMapping
    public ResponseEntity<Courier> createCourier(@Valid @RequestBody CourierRequest request) {
        Courier savedCourier = courierService.createCourier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourier);
    }

    @GetMapping("/{trackingNumber}")
    public ResponseEntity<Courier> getCourierByTrackingNumber(@PathVariable String trackingNumber) {
        Courier courier = courierService.getByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(courier);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Courier> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest statusRequest) {
        Courier updatedCourier = courierService.updateStatus(id, statusRequest);
        return ResponseEntity.ok(updatedCourier);
    }
}
