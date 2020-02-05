package com.bora.basic.service.service;

import com.bora.basic.dal.domain.BasicDefineDo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangjialei
 * @since 2020-02-05
 */
public interface IBasicDefineService extends IService<BasicDefineDo> {
    /**
     * 通过功能标识获取所有字段
     * @param mark
     * @return
     */
     List<BasicDefineDo> getDefineByMark(String mark) ;
}
