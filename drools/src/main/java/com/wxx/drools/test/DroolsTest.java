package com.wxx.drools.test;

import com.wxx.drools.entity.Demo;
import com.wxx.drools.entity.Student;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.utils.KieHelper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-07-10 17:02
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class DroolsTest {

    KieSession kieSession;

    @Before
    public void preserver(){


        //获取服务
        KieServices kieServices = KieServices.Factory.get();
        //获取kie的默认路径下的drl文件和xml文件
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        //从kie容器中拿到session对象
        kieSession = kieClasspathContainer.newKieSession();
    }
    //规则的流程demo
    @Test
    public void droolsTest1(){


        Demo demo = new Demo();
        demo.setId("1");
        demo.setCardCount(10);
        demo.setName("小王");
        //将我们的逻辑对象添加到规则内存中
        FactHandle insert = kieSession.insert(demo);
        //触发所有规则
        int i = kieSession.fireAllRules();
        System.out.println("规则数"+i);
        System.out.println(demo.getDecision());
        //关闭会话
        kieSession.dispose();

    }

    //指定规则的运行使用过滤器实现
    @Test
    public void droolsTest2(){

        Demo demo = new Demo();
        demo.setId("1");
        demo.setCardCount(6);
        demo.setName("王秀旭");
        //将我们的逻辑对象添加到规则内存中
        FactHandle insert = kieSession.insert(demo);
        //触发所有规则
        int i = kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("card_count_policy_0"));
        //关闭会话
        kieSession.dispose();

    }
    //测试update方法
    @Test
    public void droolsUpdateTest(){
        Student student = new Student();
        student.setName("成龙");
        student.setAge(11);

        kieSession.setGlobal("student",new Student());
        kieSession.insert(student);

        //调用规则文件定义的查询
        QueryResults query = kieSession.getQueryResults("student_query");
        String[] identifiers = query.getIdentifiers();
        for (QueryResultsRow queryResultsRow : query) {
            System.out.println(queryResultsRow.get("$stu"));
        }
        System.out.println(identifiers);
        int size = query.size();
        System.out.println(size);

        kieSession.fireAllRules();

        kieSession.fireUntilHalt();

        kieSession.halt();

        kieSession.dispose();
    }
    @Test
    public void test4(){
        Student student = new Student();
        student.setName("wxx");
        student.setAge(0);

        kieSession.insert(student);

        int i = kieSession.fireAllRules();

        kieSession.dispose();


    }

    @Test
    public void test5() {
        File file = new File("/");
        //通过输入流将xls文件读入到项目
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        //使用SpreadsheetCompiler这个对象将xls文件转为drl文件到字符串
        String compile = compiler.compile(in, InputType.XLS);

        //Drools还提供基于drl格式到字符串创建kiesession
        KieHelper kieHelper = new KieHelper();
        KieHelper drl = kieHelper.addContent(compile, ResourceType.BRL);
        KieSession kieSession = drl.build().newKieSession();

    }













}
