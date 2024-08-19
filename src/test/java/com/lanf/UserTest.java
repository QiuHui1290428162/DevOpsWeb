//package com.lanf;
//
//
//import com.lanf.common.utils.UserUtil;
//import com.lanf.system.model.SysUser;
//import com.lanf.tasks.model.TaskScheduledEmail;
//import com.lanf.tasks.service.EmailService;
//import com.lanf.tasks.service.TaskScheduledEmailService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class UserTest {
//
//    @Autowired
//    private EmailService emailServiceImpl;
//
//    @Autowired
//    private TaskScheduledEmailService taskScheduledEmailServiceImpl;
//
//    /**
//     * sendHTMLMail方法测试
//     *
//     * @param
//     */
//    @Test
//    public void test1() {
//        SysUser sysUser = UserUtil.getUserInfo();
//        System.out.println(sysUser);
//    }
//
//}