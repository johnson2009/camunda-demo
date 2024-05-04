package com.example.camundademo.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("AddLeaderListener")
public class AddLeaderListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        long leaveDay = (long) execution.getVariable("leaveDays");
        if(leaveDay <0){
            throw new RuntimeException("请假天数异常");
        }

        System.out.println("进入增加领导集合类，员工请假天数：" + leaveDay);
        List<String> leaders = new ArrayList<>();
        if(leaveDay <= 1) {
            leaders.add("ZuZhang");
        }else if(leaveDay > 1 && leaveDay <=3 ) {
            leaders.add("JingLi");
        }else if(leaveDay > 3){
            leaders.add("JingLi");
            leaders.add("ZongJingLi");
        }

        execution.setVariable("leaders", leaders);
    }
}
