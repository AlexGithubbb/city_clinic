package com.alex.cmn.service.impl;

import com.alex.cmn.listener.DictListener;
import com.alex.cmn.mapper.DictMapper;
import com.alex.cmn.service.DictService;
import com.alex.model.cmn.Dict;
import com.alex.vo.cmn.DictEeVo;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    // doesn't need to inject DictMapper here, since baseMapper already been injected from ServiceImpl
    /**
     * 根据上级id获取子节点数据列表
     * @param parentId
     */
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    @Override
    public List<Dict> getChildrenData(Long parentId){

        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        List<Dict> children = baseMapper.selectList(wrapper);

        for (Dict child : children) {
            Boolean hasChildren = hasChildren(child.getId());
            child.setHasChildren(hasChildren);
        }
        return children;
    }

    private Boolean hasChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        String fileName = "";
        try {
            // 这里URLEncoder.encode可以防止中文乱码 当然和EasyExcel没有关系
            fileName = URLEncoder.encode("数据字典","UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition","attachment;filename=" + fileName + ".xlsx");

            // 查询数据
            List<Dict> dictList = baseMapper.selectList(null);
            ArrayList<DictEeVo> dictEeVos = new ArrayList<>(dictList.size());

            // Dict -> DictEeVo
            for (Dict dict : dictList) {
                DictEeVo dictEeVo = new DictEeVo();
                // 利用 BeanUtils.copyProperties() 进行类之间的复制
                BeanUtils.copyProperties(dict, dictEeVo);
                dictEeVos.add(dictEeVo);
            }
            // 调用方法进行写操作
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictEeVos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入
     * allEntries = true: 方法调用后清空所有缓存
     * @param file
     */
    @CacheEvict(value = "dict", allEntries=true)
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
