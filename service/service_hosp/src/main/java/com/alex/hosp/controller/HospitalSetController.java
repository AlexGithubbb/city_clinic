package com.alex.hosp.controller;


import com.alex.hosp.service.HospitalSetService;
import com.alex.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    HospitalSetService hospitalSetService;

    @RequestMapping("/set")
    public void addHosp(@RequestParam("hosname") String hosname, @RequestParam("hoscode") String hoscode){
        HospitalSet hospitalSet = new HospitalSet();
        hospitalSet.setHosname(hosname);
        hospitalSet.setHoscode(hoscode);
        boolean save = hospitalSetService.save(hospitalSet);

        System.out.println("inserted? " + save);
    }

    @RequestMapping("/getall")
    public List<HospitalSet> getAllHospSet(){

        List<HospitalSet> list = hospitalSetService.list();
        return list;

    }


}
