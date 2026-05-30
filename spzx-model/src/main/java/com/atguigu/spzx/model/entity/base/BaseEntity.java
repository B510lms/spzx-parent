package com.atguigu.spzx.model.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    @Schema(description = "唯一标识")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // 只在响应中显示，请求参数不出现
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // 只在响应中显示，请求参数不出现
    @Schema(description = "修改时间", accessMode = Schema.AccessMode.READ_ONLY)
    private Date updateTime;

    // 只在响应中显示，内部逻辑删除标记，请求参数不出现
    @Schema(description = "是否删除", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer isDeleted;

}