package com.alex.cmn.controller;


import com.alex.cmn.service.DictService;
import com.alex.common.result.Result;
import com.alex.model.cmn.Dict;
import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/exportDict")
    public void exportDict(HttpServletResponse response){
        dictService.exportData(response);
    }
}
