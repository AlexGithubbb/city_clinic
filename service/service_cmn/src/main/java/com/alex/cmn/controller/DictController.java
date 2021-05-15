package com.alex.cmn.controller;


import com.alex.cmn.service.DictService;
import com.alex.common.result.Result;
import com.alex.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/getChildren/{id}")
    public Result getChildren(@PathVariable("id") Long id){
        List<Dict> list = dictService.getChildrenData(id);
        return Result.ok(list);
    }

    // 导入数据接口
    @PostMapping("/importData")
    public Result importDict(MultipartFile file){
        dictService.importData(file);
        return Result.ok();
    }

    // 导出数据接口
    @GetMapping("/exportDict")
    public void exportDict(HttpServletResponse response){
        dictService.exportData(response);
    }
}
