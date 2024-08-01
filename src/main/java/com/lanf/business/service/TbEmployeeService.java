package com.lanf.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lanf.business.model.TbEmployee;
import com.lanf.business.vo.TbEmployeeQueryVo;
import java.util.List;
/**
* @author code
* @version 1.0
* @description 员工信息 Service接口
* @date 2023-11-29 21:17:16
*/
public interface TbEmployeeService extends IService<TbEmployee> {
    IPage<TbEmployee> selectPage(Page<TbEmployee> pageParam, TbEmployeeQueryVo queryVo);
    List<TbEmployee> queryList(TbEmployeeQueryVo queryVo);
    public boolean save(TbEmployee tbEmployee);
    public boolean updateById(TbEmployee tbEmployee);
    public TbEmployee getById(String id);
    public List<TbEmployee> getByIds(List<String> ids);
}
