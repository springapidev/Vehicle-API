package com.udacity.boogle.maps;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maps")
public class MapsController {
    /**
     * @Note Open at browser and change lat, lon value and see results: http://localhost:9191/maps?lat=125.32552&lon=90.25457
     * @param lat takes double value like 125.32552
     * @param lon takes double value like 0.25457
     * @return a json array like {"address":"41301 US Hwy 280","city":"Sylacauga","state":"AL","zip":"35150"}
     */
    @GetMapping
    public Address get(@RequestParam Double lat, @RequestParam Double lon) {
        return MockAddressRepository.getRandom();
    }
}
