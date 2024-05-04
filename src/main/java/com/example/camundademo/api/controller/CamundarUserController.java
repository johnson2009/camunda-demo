package com.example.camundademo.api.controller;

import com.example.camundademo.api.dto.GroupParam;
import com.example.camundademo.api.dto.UserParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "CamundarUser", description = "Camunda User")
@RequestMapping("/camundar/identity")
public class CamundarUserController {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    // 用户列表
    @Operation(summary = "用户列表", description = "用户列表")
    @ApiResponse(responseCode = "2xx",description = "用户列表")
    @SneakyThrows
    @GetMapping("/user/list")
    public List<User> userList() {
        return identityService.createUserQuery().list();
    }

    // 查询用户
    @GetMapping("/user/detail/{id}")
    public User userDetail(@PathVariable("id") String id) {
        return identityService.createUserQuery().userId(id).singleResult();
    }

    // 添加用户
    @PostMapping("/user/create")
    public String create(@RequestBody UserParam param) {
        final User exist = identityService.createUserQuery().userId(param.getId()).singleResult();
        if (exist != null) {
            return "该用户已存在：" + param.getId();
        }

        UserEntity entity = new UserEntity();
        entity.setId(param.getId());
        entity.setFirstName(param.getFirstName());
        entity.setLastName(param.getLastName());
        entity.setEmail(param.getEmail());
        entity.setPassword(param.getPassword());
        identityService.saveUser(entity);
        return param.getId();
    }

    // 修改用户信息
    @PostMapping("/user/update")
    public String update(@RequestBody UserParam param) {
        final User user = identityService.createUserQuery().userId(param.getId()).singleResult();
        if (user == null) {
            return "该用户不存在：" + param.getId();
        }
        user.setFirstName(param.getFirstName());
        user.setLastName(param.getLastName());
        user.setEmail(param.getEmail());
        identityService.saveUser(user);
        return user.getId();
    }

    // 修改用户密码
    @PostMapping("/user/updatePassword")
    public String updatePassword(@RequestBody UserParam param) {
        final User user = identityService.createUserQuery().userId(param.getId()).singleResult();
        if (user == null) {
            return "该用户不存在：" + param.getId();
        }
        user.setPassword(param.getPassword());
        identityService.saveUser(user);
        return user.getId();
    }

    // 删除用户
    @PostMapping("/user/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        identityService.deleteUser(id);
        return id;
    }

    // 组列表
    @GetMapping("/group/list")
    public List<Group> groupList() {
        return identityService.createGroupQuery().list();
    }

    // 查询组
    @GetMapping("/group/detail/{id}")
    public Group groupDetail(@PathVariable("id") String id) {
        return identityService.createGroupQuery().groupId(id).singleResult();
    }

    // 添加组
    @PostMapping("/group/create")
    public String groupCreate(@RequestBody GroupParam param) {
        final Group exist = identityService.createGroupQuery().groupId(param.getId()).singleResult();
        if (exist != null) {
            return "该组已存在：" + param.getId();
        }

        GroupEntity entity = new GroupEntity();
        entity.setId(param.getId());
        entity.setName(param.getName());
        entity.setType(param.getType());
        identityService.saveGroup(entity);

        return param.getId();
    }

    // 修改组信息
    @PostMapping("/group/update")
    public String groupUpdate(@RequestBody GroupParam param) {
        final Group group = identityService.createGroupQuery().groupId(param.getId()).singleResult();
        if (group == null) {
            return "该组不存在：" + param.getId();
        }
        group.setName(param.getName());
        group.setType(param.getType());
        identityService.saveGroup(group);
        return group.getId();
    }

    // 删除组
    @PostMapping("/group/delete/{id}")
    public String groupDelete(@PathVariable("id") String id) {
        identityService.deleteGroup(id);
        return id;
    }

    // 将用户添加到组中
    @PostMapping("/group/relation/{userId}/{groupId}")
    public String userGroupRelation(@PathVariable("userId") String userId,
                                    @PathVariable("groupId") String groupId) {
        String error = checkUserGroupExist(userId, groupId);
        if (error != null) {
            return error;
        }

        final User exist = identityService.createUserQuery().memberOfGroup(groupId).userId(userId).singleResult();
        if (exist != null) {
            return "该用户与组已关联";
        }

        identityService.createMembership(userId, groupId);
        return "请求成功";
    }

    // 从组中删除用户
    @PostMapping("/group/delete/{userId}/{groupId}")
    public String userGroupDelete(@PathVariable("userId") String userId,
                                  @PathVariable("groupId") String groupId) {
        String error = checkUserGroupExist(userId, groupId);
        if (error != null) {
            return error;
        }
        identityService.deleteMembership(userId, groupId);
        return "请求成功";
    }

    private String checkUserGroupExist(String userId, String groupId) {
        final User user = identityService.createUserQuery().userId(userId).singleResult();
        final Group group = identityService.createGroupQuery().groupId(groupId).singleResult();

        if (userId != null && user == null) {
            return "该用户不存在：" + userId;
        }

        if (groupId != null && group == null) {
            return "该组不存在：" + groupId;
        }

        return null;
    }
}
