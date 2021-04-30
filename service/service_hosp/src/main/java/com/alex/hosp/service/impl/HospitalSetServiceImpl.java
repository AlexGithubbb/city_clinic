package com.alex.hosp.service.impl;

import com.alex.hosp.mapper.HospitalSetMapper;
import com.alex.hosp.service.HospitalSetService;
import com.alex.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
}
