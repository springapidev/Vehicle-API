package com.coderbd.repository;

import com.coderbd.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PriceRepository extends JpaRepository<Price,Long> {
    Set<Price> findByVehicleIdIn(Set<Long> vehicleIds);
}
