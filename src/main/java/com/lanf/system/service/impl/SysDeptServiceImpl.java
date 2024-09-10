package com.lanf.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanf.common.exception.GlobalExpiredException;
import com.lanf.common.result.ResultCodeEnum;
import com.lanf.system.model.SysDept;
import com.lanf.system.model.SysUser;
import com.lanf.system.vo.SysDeptQueryVo;
import com.lanf.system.mapper.SysDeptMapper;
import com.lanf.system.service.SysDeptService;
import com.lanf.common.helper.DeptHelper;
import com.lanf.common.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public boolean save(SysDept sysDept) {
        this.sysDeptMapper.insert(sysDept);
        if ("0".equals(sysDept.getParentId())) {
            sysDept.setLevel(1);
        } else {
            SysDept sysDept1 = this.getById(sysDept.getParentId());
            sysDept.setLevel(sysDept1.getLevel() + 1);
        }
        String treePath = this.getTreePath(sysDept);
        sysDept.setTreePath(treePath);
        this.sysDeptMapper.updateById(sysDept);
        return true;
    }

    public boolean updateById(SysDept sysDept) {
        if ("0".equals(sysDept.getParentId()) || StringUtils.isEmpty(sysDept.getParentId())) {
            sysDept.setLevel(1);
        } else {
            SysDept sysDept1 = this.getById(sysDept.getParentId());
            sysDept.setLevel(sysDept1.getLevel() + 1);
        }
        String treePath = this.getTreePath(sysDept);
        sysDept.setTreePath(treePath);
        this.sysDeptMapper.updateById(sysDept);
        return true;
    }

    private String getTreePath(SysDept sysDept) {
        if (sysDept.getParentId() == null || "0".equals(sysDept.getParentId())) {
            return "," + sysDept.getId() + ",";
        } else {
            SysDept sysDept1 = this.getById(sysDept.getParentId());
            if (!StringUtils.isEmpty(sysDept1.getTreePath())) {
                return sysDept1.getTreePath() + sysDept.getId() + ",";
            }
            return getTreePath(sysDept1) + sysDept.getId() + ",";
        }
    }

    @Override
    public List<SysDept> findNodes(SysDeptQueryVo sysDeptQueryVo) {
        //全部部门列表
        SysUser sysUser = UserUtil.getUserInfo();
        if ("1".equals(sysUser.getId())) {
            sysDeptQueryVo.setDeptId(null);
        } else {
            if (StringUtils.isEmpty(sysUser.getDeptId())) {
                return null;
            }
            sysDeptQueryVo.setDeptId(sysUser.getDeptId());
        }
        List<SysDept> sysDptList = sysDeptMapper.queryList(sysDeptQueryVo);
        if (CollectionUtils.isEmpty(sysDptList)) return null;
        //构建树形数据
        List<SysDept> result = DeptHelper.buildTree(sysDptList);
        return result;
    }

    @Override
    public List<SysDept> findNodesByParent(String parentId) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        SysUser sysUser = UserUtil.getUserInfo();
        String deptId = sysUser.getDeptId();
        if (!"1".equals(sysUser.getId()) && "0".equals(parentId)) {
            if (StringUtils.isEmpty(deptId)) {
                return null;
            }
            //找到当前用户所属部门根节点
            QueryWrapper<SysDept> findWrapper = new QueryWrapper<>();
            findWrapper.eq("id", deptId);
            findWrapper.orderByAsc("level");
            List<SysDept> findList = this.list(findWrapper);
            if (!CollectionUtils.isEmpty(findList)) {
                return null;
            }
            SysDept sysDept = findList.get(0);
            queryWrapper.eq("id", sysDept.getId());
        } else {
            queryWrapper.eq("parent_id", parentId);
        }
        List<SysDept> sysDptList = this.list(queryWrapper);
        return sysDptList;
    }

    @Override
    public List<Map> findSelectNodes() {
        //全部部门列表
        SysDeptQueryVo sysDeptQueryVo = new SysDeptQueryVo();
        SysUser sysUser = UserUtil.getUserInfo();
        if ("1".equals(sysUser.getId())) {
            sysDeptQueryVo.setDeptId(null);
        } else {
            if (StringUtils.isEmpty(sysUser.getDeptId())) {
                return null;
            }
            sysDeptQueryVo.setDeptId(sysUser.getDeptId());
        }
        List<Map> sysDptList = sysDeptMapper.findList(sysDeptQueryVo);
        if (CollectionUtils.isEmpty(sysDptList)) return null;
        //构建树形数据
        List<Map> result = DeptHelper.buildTreeMap(sysDptList);
        return result;
    }

    @Override
    public boolean removeById(Serializable id) {
        int count = this.count(new QueryWrapper<SysDept>().eq("parent_id", id));
        if (count > 0) {
            throw new GlobalExpiredException(ResultCodeEnum.NODE_ERROR);
        }
        sysDeptMapper.deleteById(id);
        return false;
    }
}
