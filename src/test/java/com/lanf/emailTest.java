package com.lanf;


import com.lanf.tasks.model.TaskScheduledEmail;
import com.lanf.tasks.service.EmailService;
import com.lanf.tasks.service.TaskScheduledEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class emailTest {

    @Autowired
    private EmailService emailServiceImpl;

    @Autowired
    private TaskScheduledEmailService taskScheduledEmailServiceImpl;

    /**
     * sendHTMLMail方法测试
     *
     * @param
     */
    @Test
    public void test1() {
        emailServiceImpl.sendMail("qiuhuiyong@gxytgroup.com",""
                ,"测试发送邮件","测试发送邮件",null);
    }

    @Test
    public void test2() {
        TaskScheduledEmail task =  taskScheduledEmailServiceImpl.getById("13");
        emailServiceImpl.sendMail(task);
    }
}