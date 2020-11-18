package com.coderbd.controller;

import com.coderbd.entity.Price;
import com.coderbd.exceptions.PriceException;
import com.coderbd.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

/**
 * Implements a REST-based controller for the pricing service.
 */
@RestController
@RequestMapping("/services/price")
public class PricingController {



    /**
     * @param vehicleId Unique number of the vehicle
     * @return price of the vehicle like {"currency":"USD","price":6952.93,"vehicleId":1}, or error that it was not found.
     * @apiNote Open the URL at Browser: http://localhost:8082/services/price?vehicleId=1
     * provide vehicleId from 1 to 20 for this application
     * @Apinote Gets the price for a requested vehicle providing vehicleId as parameter
     */
    @GetMapping
    public ResponseEntity<Price> get(@RequestParam Long vehicleId) {
        try {
            return new ResponseEntity<Price>(PricingService.getPrice(vehicleId), HttpStatus.OK);
        } catch (PriceException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Price Not Found", ex);
        }

    }


}
