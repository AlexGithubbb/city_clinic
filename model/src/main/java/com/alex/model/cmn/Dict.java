package com.alex.model.cmn;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Dict
 * </p>
 *
 * @author qy
 */
@Data
@TableName("dict")
public class Dict {

    private static final long serialVersionUID = 1L;

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted = 0; // default value is 1, need to initialize with 0 (not deleted)

    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();

    @TableField("parent_id")
    private Long parentId;

    @TableField("name")
    private String name;

    @TableField("value")
    private String value;

    @TableField("dict_code")
    private String dictCode;

    @TableField(exist = false)
    private boolean hasChildren;

}