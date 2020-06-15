package com.app.services.implementations;

import java.util.List;

import com.app.dao.RegionDao;
import com.app.models.Region;
import com.app.services.interfaces.RegionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionDao regionDao;

    @Override
    public Region save(Region region) {
        return regionDao.save(region);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Region> findAll() {
        return (List<Region>) regionDao.findAll();
    }
 
    @Override
    @Transactional(readOnly = true)
    public Region findById(Long id) {
        return regionDao.findById(id).orElse(null);
    }
    
}