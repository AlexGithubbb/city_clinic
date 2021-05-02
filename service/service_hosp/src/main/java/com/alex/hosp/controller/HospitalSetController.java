package com.alex.hosp.controller;


import com.alex.common.result.Result;
import com.alex.common.utils.MD5;
import com.alex.hosp.service.HospitalSetService;
import com.alex.model.hosp.HospitalSet;
import com.alex.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    HospitalSetService hospitalSetService;



    @RequestMapping("/getAll")
    public  Result getAllHospSet(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    @DeleteMapping("/{id}")
    public Result testDeleteById(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        return Result.flag(flag);
    }

    /**
     *
     * 条件查询 + 接口
     * @ReqestBody is bonded with @PostMapping
     * @param current
     * @param limit
     * @param hospitalSetQueryVo
     * @return
     */
    @PostMapping("/page/{current}/{limit}")
    public Result queryByPage(
                             @PathVariable("current") long current,
                             @PathVariable("limit") long limit,
                            @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> hospitalSetPage = new Page<>(current, limit);

        // config wrapper

        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();

        String hosname="";
        String hoscode="";
        if(hospitalSetQueryVo != null){

           hosname = hospitalSetQueryVo.getHosname();
           hoscode = hospitalSetQueryVo.getHoscode();
        }

        // decide if the input value is valid or not, this logic should be put in service level
        if(!StringUtils.isEmpty(hosname) ){
            wrapper.like("hosname",hosname);
        }

        if(!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode",hoscode);
        }

        Page<HospitalSet> page = hospitalSetService.page(hospitalSetPage, wrapper);
//
//        long total = page.getTotal();
//        long currentPage = page.getCurrent();
//        List<HospitalSet> records = page.getRecords();
//        boolean b = page.hasNext();
//        boolean hasPrevious = page.hasPrevious();

        return Result.ok(page);

    }

    @PostMapping("/save")
    public Result addHospSet(@RequestBody HospitalSet hospitalSet){

        // 后台设置初始状态 1=可用 0=不可用
        hospitalSet.setStatus(1);

        // 后台设置密钥
        Random random = new Random();

        String s = System.currentTimeMillis() + "" + random.nextInt(1000);
        String encrypt = MD5.encrypt(s);

        hospitalSet.setSignKey(encrypt);

        boolean save = hospitalSetService.save(hospitalSet);

        System.out.println("inserted? " + save);

        return Result.flag(save);
    }

}