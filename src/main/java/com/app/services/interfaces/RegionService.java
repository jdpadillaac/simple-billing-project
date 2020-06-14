package com.app.services.interfaces;

import java.util.List;

import com.app.models.Region;

public interface RegionService {

    public Region save(Region region);

    public List<Region> findAll();
    
}