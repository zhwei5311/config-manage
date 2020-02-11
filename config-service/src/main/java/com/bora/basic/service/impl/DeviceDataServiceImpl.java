package com.bora.basic.service.impl;

import com.bora.basic.dal.dao.DeviceDataMapper;
import com.bora.basic.dal.domain.DeviceDataDo;
import com.bora.basic.service.service.IDeviceDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备数据记录表 服务实现类
 * </p>
 *
 * @author zhwei
 * @since 2020-02-11
 */
@Service
public class DeviceDataServiceImpl extends ServiceImpl<DeviceDataMapper, DeviceDataDo> implements IDeviceDataService {

}
