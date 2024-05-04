package com.example.camundademo.api.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Schema(name = "TaskDto", title = "Camunda Task DTO")
public class TaskDto {
    @Schema(title = "Task Id")
    private String id;
    @Schema(title = "Task Name")
    private String name;
    @Schema(title = "Task Assignee")
    private String assignee;
    @Schema(title = "Task Create Time")
    private Date createTime;
}
