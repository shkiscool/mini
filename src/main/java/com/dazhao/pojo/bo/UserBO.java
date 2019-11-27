package com.dazhao.pojo.bo;

import com.dazhao.pojo.dao.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.Data;

@Data
@Api(description = "用户注册对象")
public class UserBO extends User {

    @ApiModelProperty(notes = "具有的权限idList")
    private List<Integer> permission;
}
