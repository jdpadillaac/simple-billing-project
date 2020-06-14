package com.app.dao;

import com.app.models.Region;

import org.springframework.data.repository.CrudRepository;

public interface RegionDao extends CrudRepository<Region, Long> {
    
}