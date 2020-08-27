package com.wxx.activity.conf;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-07-02 14:07
 **/

public class TaskAssignHandler implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        //通过监听器对流程运行监听，使用DelegateTask对代办人添加
        //优点在于可以通过大量业务逻辑来对代办人进行处理

        //代码逻辑片段
        //。。。。。
        //。。。。
        delegateTask.setAssignee("小王");
    }
}
