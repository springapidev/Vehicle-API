package com.coderbd.service;


import com.coderbd.entity.Price;
import com.coderbd.exceptions.PriceException;
import com.coderbd.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @apiNote Implements the pricing service to get prices for each vehicle.
 */
@Service
public class PricingService {
    @Autowired
    private PriceRepository repository;
    private static final String FAILURE = "Failure";
    private static final String SUCCESS = "Success";
    /**
     * Holds {ID: Price} pairings (current implementation allows for 20 vehicles)
     */
    private static final Map<Long, Price> PRICES = LongStream
            .range(1, 20)
            .mapToObj(i -> new Price("USD", randomPrice(), i))
            .collect(Collectors.toMap(Price::getVehicleId, p -> p));

    /**
     * If a valid vehicle ID, gets the price of the vehicle from the stored array.
     * @param vehicleId ID number of the vehicle the price is requested for.
     * @return price of the requested vehicle
     * @throws PriceException vehicleID was not found
     */
    public static Price getPrice(Long vehicleId) throws PriceException {

        if (!PRICES.containsKey(vehicleId)) {
            throw new PriceException("Cannot find price for Vehicle " + vehicleId);
        }

        return PRICES.get(vehicleId);
    }

    /**
     * Gets a random price to fill in for a given vehicle ID.
     * @return random price for a vehicle
     */
    private static BigDecimal randomPrice() {
        return new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 5))
                .multiply(new BigDecimal(5000d)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Saves price in DB
     *
     * @param price  Object.
     * @return saved Price object
     */
    public Price setPrice(Price price)  {
        return repository.save(price);
    }

    /**
     * Creates new random price
     *
     * @param vehicleId ID number of the vehicle the price is requested for.
     * @return saved Price object
     */
    public Price setNewPrice(Long vehicleId, String currency, Optional<BigDecimal> amount) {
        Optional<Price> presentPrice = repository.findById(vehicleId);
        Price updatedPrice;
        if (presentPrice.isPresent()) {
            updatedPrice = presentPrice.get();
            updatedPrice.setCurrency((currency != null && currency.trim().length() > 0) ? currency : "USD");
            updatedPrice.setPrice(amount.orElseGet(PricingService::randomPrice));
        } else {
            updatedPrice = new Price((currency != null && currency.trim().length() > 0) ? currency : "USD", amount.orElseGet(PricingService::randomPrice),vehicleId);
        }

        return repository.save(updatedPrice);
    }



    /**
     * Deletes the price entry for given vehicleId
     *
     * @param vehicleId ID number of the vehicle the price is requested for.
     * @return saved Price object
     */
    public String delete(Long vehicleId) {
        try{
            Optional<Price> price = repository.findById(vehicleId);
            if (price.isEmpty()) {
                throw new PriceException("Cannot find price for Vehicle " + vehicleId);
            }else{
                repository.delete(price.get());
            }
        }catch (Exception ex){
            return FAILURE;
        }
        return SUCCESS;

    }

    public Set<Price> getPriceList(Set<Long> vehicleList) {
        return repository.findByVehicleIdIn(vehicleList);
    }
}


