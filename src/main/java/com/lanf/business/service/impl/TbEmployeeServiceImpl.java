package com.lanf.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanf.business.model.TbEmployee;
import com.lanf.business.vo.TbEmployeeQueryVo;
import com.lanf.business.mapper.TbEmployeeMapper;
import com.lanf.business.service.TbEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.lanf.common.result.ResultCodeEnum;
import com.lanf.system.exception.LanfException;
/**
* @author code
* @version 1.0
* @description 员工信息 Service实现类
* @date 2023-11-29 21:17:16
*/
@Transactional
@Service
public class TbEmployeeServiceImpl extends ServiceImpl
<TbEmployeeMapper, TbEmployee> implements TbEmployeeService {
    @Autowired
    private TbEmployeeMapper tbEmployeeMapper;

    @Override
    public IPage<TbEmployee> selectPage(Page<TbEmployee> pageParam, TbEmployeeQueryVo tbEmployeeQueryVo) {
        //QueryWrapper<TbEmployee> queryWrapper = new QueryWrapper<>();
        //return tbEmployeeMapper.selectPage(pageParam,queryWrapper);
        return tbEmployeeMapper.selectPage(pageParam,tbEmployeeQueryVo);
    }

    @Override
    public List<TbEmployee> queryList(TbEmployeeQueryVo tbEmployeeQueryVo){
     List<TbEmployee> result = tbEmployeeMapper.queryList(tbEmployeeQueryVo);
     return result;
    }


    @Override
    public boolean save(TbEmployee tbEmployee){
        int result = this.tbEmployeeMapper.insert(tbEmployee);
        return result>0;
    }
    @Override
    public boolean updateById(TbEmployee tbEmployee){
        int row = this.tbEmployeeMapper.updateById(tbEmployee);
        if(row <= 0){
            throw new LanfException(ResultCodeEnum.UPDATE_ERROR);
         }
        return row>0;
    }
    @Override
    public TbEmployee getById(String id){
         TbEmployee tbEmployee = tbEmployeeMapper.selectById(id);
         return tbEmployee;
    }
   @Override
   public List<TbEmployee> getByIds(List<String> ids) {
      List<TbEmployee> list = this.tbEmployeeMapper.selectBatchIds(ids);
      return list;
   }
}
