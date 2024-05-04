package com.example.camundademo.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("NotifyHRService")
public class NotifyHRService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        final String initiator = String.valueOf(execution.getVariable("initiator"));
        final long leaveDays = (long) execution.getVariable("leaveDays");
        final  boolean approve = (boolean) execution.getVariable("approve");
        final  String comment = String.valueOf(execution.getVariable("comment"));

        System.out.println("员工发起请假申请，申请人：" + initiator + "，请假天数：" + leaveDays);
        System.out.println("申请是否通过：" + (approve ? "是" : "否"));
        System.out.println("上级审批意见：" + comment);
    }
}
