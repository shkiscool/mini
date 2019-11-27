package com.dazhao.service.impl;

import com.dazhao.common.OperationLogEnum;
import com.dazhao.common.Page;
import com.dazhao.pojo.dao.Location;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.mapper.LocationMapper;
import com.dazhao.pojo.mapper.OperationLogMapper;
import com.dazhao.service.LocationService;
import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public List<Location> queryLocationList(Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return locationMapper.queryLocationList();
    }

    @Override
    public List<String> queryLocationMaterialGroupByid(Integer locationId) {
        return locationMapper.queryLocationMaterialGroupByid(locationId);
    }

    @Override
    public Boolean isExistLocationByid(Integer locationId) {
        return locationMapper.selectByPrimaryKey(locationId) != null ? true : false;
    }

    @Override
    @Transactional
    public void deleteLocationByid(Integer locationId, int userId, String ipAddress) {
        Location location = locationMapper.selectByPrimaryKey(locationId);
        locationMapper.deleteLocationByid(locationId);
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.DELETE_LOCATION.getModuleName(),
                OperationLogEnum.DELETE_LOCATION.getContent() + location.getLocationNumber(), new Date());
        operationLogMapper.insert(operationLog);
    }

    @Override
    public Boolean isExistUserLocationByid(Integer locationId) {
        return locationMapper.isExistUserLocationByid(locationId) > 0;

    }

    @Override
    public Boolean isExistLocationByLocationNumber(String locationNumber) {
        return locationMapper.isExistLocationByLocationNumber(locationNumber) > 0;
    }

    @Override
    public void insertLocation(Location location, int userId, String ipAddress) {
        location.setCreateTime(new Date());
        locationMapper.insertSelective(location);
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.ADD_LOCATION.getModuleName(),
                OperationLogEnum.ADD_LOCATION.getContent() + location.getLocationNumber(), new Date());
        operationLogMapper.insert(operationLog);
    }

    @Override
    public void updateLocationFillFlag(Integer locationId, Integer fillFlag) {
        locationMapper.updateLocationFillFlag(locationId, fillFlag);
    }

    @Override
    public Location queryDefaultLocation() {
        return locationMapper.queryDefaultLocation();
    }

}
