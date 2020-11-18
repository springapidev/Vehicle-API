package com.udacity.vehicles.client.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implements a class to interface with the Pricing Client for price data.
 */
@Component
public class PriceClient {

    private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

    private final WebClient client;

    public PriceClient(WebClient pricing) {
        this.client = pricing;
    }


    /**
     * When vehicle api will provide a vehicle id, then this method will return price
     * error message that the vehicle ID is invalid Or Service May down
     *
     * @param vehicleId
     * @return
     */
    public Price getPrice(Long vehicleId) {
        Price price = new Price();
        if (vehicleId != null) {
            try {
                price = client
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("services/price/")
                                .queryParam("vehicleId", vehicleId)
                                .build()
                        )
                        .retrieve().bodyToMono(Price.class).block();
//String.format("%s %s", price.getCurrency(), price.getPrice())
                return price;

            } catch (Exception e) {
                log.error("Unexpected Unexception getting price for vehicle {}", vehicleId, e);
                return price;
            }
        } else {
           return new Price();
        }

    }
}
