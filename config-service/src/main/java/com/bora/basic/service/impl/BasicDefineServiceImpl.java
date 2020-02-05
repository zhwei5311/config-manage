package com.bora.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bora.basic.dal.dao.BasicDefineMapper;
import com.bora.basic.dal.domain.BasicDefineDo;
import com.bora.basic.service.service.IBasicDefineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangjialei
 * @since 2020-02-05
 */
@Service
public class BasicDefineServiceImpl extends ServiceImpl<BasicDefineMapper, BasicDefineDo> implements IBasicDefineService {
    @Override
    public List<BasicDefineDo> getDefineByMark(String mark) {
        LambdaQueryWrapper<BasicDefineDo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BasicDefineDo::getMark,mark);
        return this.list(queryWrapper);
    }
}
