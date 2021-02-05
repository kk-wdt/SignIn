package com.kktt.jesus.controller.group;

import com.kktt.jesus.controller.BaseController;
import com.kktt.jesus.controller.viewobject.GroupVO;
import com.kktt.jesus.dao.source1.GotenProductDao;
import com.kktt.jesus.dataobject.GotenProduct;
import com.kktt.jesus.errror.BusinessException;
import com.kktt.jesus.errror.EmBusinessError;
import com.kktt.jesus.response.CommonReturnType;
import com.kktt.jesus.service.GroupService;
import com.kktt.jesus.service.model.GroupModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController extends BaseController {

    @Autowired
    private GroupService groupService;
    @Resource
    private GotenProductDao gotenProductDao;

    @RequestMapping("/add")
    public CommonReturnType login(@RequestParam(value = "groupName") String groupName ) throws BusinessException {
        if(groupName == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"组名不能为空");
        }
        GroupModel groupModel = new GroupModel();
        groupModel.setName(groupName);
        groupModel.setNumber("110");
        GroupModel groupModelForReturn = groupService.add(groupModel);
        return CommonReturnType.create(convertGroupVOFromGroupModel(groupModelForReturn));
    }

    @RequestMapping("/test")
    public CommonReturnType test() throws BusinessException {
        List<GotenProduct> xx = gotenProductDao.selectAll();
        return CommonReturnType.create(xx);
    }


    private GroupVO convertGroupVOFromGroupModel(GroupModel groupModel){
        if(groupModel == null){
            return null;
        }
        GroupVO groupVO = new GroupVO();
        BeanUtils.copyProperties(groupModel,groupVO);
        return groupVO;
    }

}
