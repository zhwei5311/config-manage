package com.bora.basic.service.impl;

import com.bora.basic.dal.dao.ProductionDataMapper;
import com.bora.basic.dal.domain.ProductionDataDo;
import com.bora.basic.service.service.IProductionDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 生产报工记录表 服务实现类
 * </p>
 *
 * @author chengyankai
 * @since 2020-02-18
 */
@Service
public class ProductionDataServiceImpl extends ServiceImpl<ProductionDataMapper, ProductionDataDo> implements IProductionDataService {

}
