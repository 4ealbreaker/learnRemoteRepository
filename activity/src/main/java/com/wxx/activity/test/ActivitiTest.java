package com.wxx.activity.test;


import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-22 14:03
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ActivitiTest {


    @Autowired
    ProcessEngine processEngine;


    //每个流程的组件
    //服务的部署
    //1 repositoryService activiti的服务仓库，所谓服务仓库就是定义的bpmn和图片
    //执行的表：act_ge_bytearry 存放bpmn图片
    //          act_re_deployment 流程部署的表
    //          act_re_procdef 流程定义数据表
    //          act_re_model 流程设计模型部署表

    @Test
    public void repositoryService(){
        processEngine.getRepositoryService().createDeployment()
                .addClasspathResource("processes/progress.bpmn")
                .name("progress")
                .deploy();
    }

    //2 启动流程实例 将部署的流程进行发布
    //RuntimeService 执行管理，包括启动、推进、删除流程实例等操作
    //执行的表：act_hi_identylink
    //          act_hi_actinst 历史节点表
    //            act_hi_taskinst 历史任务实例表
    //              act_ru_identylink 运行身份连接表
    //                  act_ru_task 运行任务表
    //                      act_ru_execution 运行时流程执行实例表
    @Test
    public void runtimeService(){

        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("_1");

        System.out.println("流程定义id"+processInstance.getProcessDefinitionId()+"流程名字"+processInstance.getProcessDefinitionName());

    }

    //3 查看个人任务
    // TaskService
    //所有任务相关的东西都组织在TaskService中，例如
    //查询分派给用户或组的任务
    //创建standalone（独立运行）任务。这是一种没有关联到流程实例的任务。
    //决定任务的执行用户（assignee），或者将用户通过某种方式与任务关联。
    //认领（claim）与完成（complete）任务。认领是指某人决定成为任务的执行用户，也即他将会完成这个任务。完成任务是指“做这个任务要求的工作”，通常是填写某种表单。
    @Test
    public void taskService(){
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery().taskAssignee("小王").list();
        if(null != list && list.size() > 0){
            for (Task task : list) {
                System.out.println("代办人的任务列表："+task.getName());
                System.out.println("代办人："+task.getAssignee());
            }
        }

    }

    //4 任务的推进 完成个人任务
    //进入到下一个任务点
    @Test
    public void completeTask(){
        processEngine.getTaskService().complete("5005");
        System.out.println("小王发布请假任务");
    }

    //5 下一节点的任务开始 刚哥处理请假任务
    //7502	1	5002	5001	_1:2:2504	主管审批			_5		刚哥		50	2020-07-02 10:45:58.956			1
    @Test
    public void taskService2(){
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery().taskAssignee("刚哥").list();
        if(null != list && list.size() > 0){
            for (Task task : list) {
                System.out.println("代办人的任务列表："+task.getName());
                System.out.println("代办人："+task.getAssignee());
            }
        }

    }

    // 6 继续运行任务到结束
    //查看所有的act_ru_*都清空了
    //这些表可以查看历史的运行信息
    //act_hi_identylink
    //          act_hi_actinst 历史节点表
    //            act_hi_taskinst 历史任务实例表
    //              act_ru_identylink 运行身份连接表

    @Test
    public void completeAllTask(){
        processEngine.getTaskService().complete("7502");
        System.out.println("所有任务结束");
    }

    @Test
    public void processVariable(){
        processEngine.getTaskService().setVariable(/*taskId*/"7502"/*变量内容*/,"天数",7);
    }
    @Test
    public void completeProcessVariable(){
        Map<String, Object> map = new HashMap<>();
        map.put("message","yes");
        processEngine.getTaskService().complete("1320",map);


        Map<String, Object> map1 = new HashMap<>();
        map.put("userId", "小王");
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById("部署的流程id", map1);
    }









}
