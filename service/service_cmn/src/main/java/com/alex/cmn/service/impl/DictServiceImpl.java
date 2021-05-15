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
    @Override
    public List<Dict> getChildrenData(Long id){

        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> children = baseMapper.selectList(wrapper);

        for (Dict child : children) {
            Long parentId = child.getId();
            Boolean hasChildren = hasChildren(parentId);
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

    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
