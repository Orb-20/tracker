package com.courier.service;

import com.courier.dto.CourierRequest;
import com.courier.dto.StatusUpdateRequest;
import com.courier.exception.CourierNotFoundException;
import com.courier.model.Courier;
import com.courier.model.CourierStatus;
import com.courier.repository.CourierRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CourierService {

    private final CourierRepository courierRepository;

    public CourierService(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    public Courier createCourier(CourierRequest request) {
        String trackingNumber = generateTrackingNumber();
        Courier courier = new Courier(
                trackingNumber,
                request.getSenderName(),
                request.getReceiverName(),
                request.getSource(),
                request.getDestination(),
                CourierStatus.CREATED
        );
        return courierRepository.save(courier);
    }

    public Courier getByTrackingNumber(String trackingNumber) {
        return courierRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new CourierNotFoundException("trackingNumber", trackingNumber));
    }

    public Courier updateStatus(Long id, StatusUpdateRequest statusRequest) {
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new CourierNotFoundException("id", String.valueOf(id)));
        courier.setStatus(statusRequest.getStatus());
        return courierRepository.save(courier);
    }

    private String generateTrackingNumber() {
        String trackingNumber;
        do {
            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            trackingNumber = "TRK-" + uuid.substring(0, 8);
        } while (courierRepository.existsByTrackingNumber(trackingNumber));
        return trackingNumber;
    }
}
