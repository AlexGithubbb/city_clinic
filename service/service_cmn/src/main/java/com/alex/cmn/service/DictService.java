package com.alex.cmn.service;

import com.alex.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface DictService extends IService<Dict> {
    List<Dict> getChildrenData(Long id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
}
