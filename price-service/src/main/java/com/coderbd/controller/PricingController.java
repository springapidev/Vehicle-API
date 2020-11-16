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
    @Autowired
    private PricingService service;

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

    /**
     * @Apinote Gets the price for a requested list of vehicles.
     *
     * @param vehicleList Set of Id numbers of the vehicle for which the price is requested
     * @return price of the vehicle, or error that it was not found.
     */
    @GetMapping("/getPrices")
    public ResponseEntity<Set<Price>> getList(@RequestParam(name = "vehicleList") Set<Long> vehicleList) {
        return new ResponseEntity<>(service.getPriceList(vehicleList), HttpStatus.CREATED);
    }

    /**
     * @Apinote Sets the price for a requested vehicle.
     * @param price object
     * @return price of the vehicle, or error that it was not found.
     */
    @PostMapping
    public ResponseEntity<Price> setPrice(@RequestBody Price price) {
        return new ResponseEntity<Price>(service.setPrice(price), HttpStatus.CREATED);
    }

    /**
     * we can set the price for a requested vehicle.
     *
     * @param vehicleId ID number of the vehicle for which the price is requested
     * @param currency  may be USd or BDT OR EUR etc
     * @param amount    may be any like 1000,25000,2360.25 etc
     * @return price of the vehicle, or error if it was not found.
     * @Apinote url should be like this http://localhost:8082/services/price/getNewPrice?vehicleId=100&currency=BDT&amount=20000
     */
    @GetMapping("/getNewPrice")
    public Price getNewPrice(@RequestParam(name = "vehicleId") Long vehicleId,
                             @RequestParam(name = "currency") String currency,
                             @RequestParam(name = "amount") Optional<BigDecimal> amount) {
        return service.setNewPrice(vehicleId, currency, amount);
    }

    /**
     * Sets the price for a requested vehicle.
     *
     * @param vehicleId ID number of the vehicle for which the price is to be deleted
     * @return price of the vehicle, or error that it was not found.
     */
    @DeleteMapping
    public String delete(@RequestParam(name = "vehicleId") Long vehicleId) {
        return service.delete(vehicleId);
    }
}
