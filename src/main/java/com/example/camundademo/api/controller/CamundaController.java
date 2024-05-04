package com.example.camundademo.api.controller;

import com.example.camundademo.api.dto.TaskDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Tag(name = "camunda", description = "Camunda")
@RequestMapping("/camunda")
public class CamundaController {
    @Autowired
    private IdentityService identityService;

    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected HistoryService historyService;



    @Operation(summary = "开启流程", description = "开启流程",
            parameters = {@Parameter(name = "processKey", description = "流程的processKey")})
    @ApiResponse(responseCode = "2xx",description = "Void")
    @SneakyThrows
    @PostMapping("/start/{processKey}")
    public  void start1(@PathVariable(value = "processKey") String processKey){
        identityService.setAuthenticatedUserId("cc");

        final VariableMap variables = Variables.createVariables();
        variables.putValue("initiator", "cc");

        runtimeService.startProcessInstanceByKey(processKey, variables);
    }

    //待办任务列表
    @Operation(summary = "待办任务列表", description = "待办任务列表",
        parameters = {@Parameter(name = "userId", description = "用户ID")})
    @ApiResponse(responseCode = "2xx", description = "任务列表")
    @GetMapping("/task/list/{userId}")
    public List<TaskDto> taskList(@PathVariable("userId") String userId) {
        final List<Task> list = taskService.createTaskQuery()
                .taskAssignee(userId).list();

        List<TaskDto> dtoList = new ArrayList<>();
        for (Task task : list) {
            TaskDto dto = new TaskDto();
            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setAssignee(task.getAssignee());
            dto.setCreateTime(task.getCreateTime());
            dtoList.add(dto);
        }
        return dtoList;
    }

    //处理任务，包括员工填写请假表单、组长审批，经理审批，总经理审批
    @Operation(summary = "处理任务", description = "处理任务，包括员工填写请假表单、组长审批，经理审批，总经理审批")
    @ApiResponse(responseCode = "2xx", description = "请求成功")
    @PostMapping("/task/complete")
    public  String complete(@RequestBody HashMap<String, Object> params){
        final  String id = String.valueOf(params.get("id"));
        final  Task task = taskService.createTaskQuery().taskId(id).singleResult();
        if(task == null){
            return "该任务不存在";
        }

        VariableMap variables = Variables.createVariables();

        for(String key : params.keySet()){
            Object value = params.get(key);
            if(value instanceof  Integer){
                variables.putValue(key, Long.parseLong(params.get(key).toString()));
            }else{
                variables.putValue(key, params.get(key));
            }
        }

        taskService.complete(id, variables);

        return  "请求成功";
    }

    @Operation(summary = "任务历史", description = "按userId查看处理历史",
            parameters = {@Parameter(name = "userId", description = "流程的processKey")})
    @ApiResponse(responseCode = "2xx",description = "任务历史")
    @SneakyThrows
    @GetMapping("/history/list/{userId}")
    public  List<HistoricTaskInstance> taskHistoryList(@PathVariable("userId") String userId){
        return historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished().list();
    }
}
