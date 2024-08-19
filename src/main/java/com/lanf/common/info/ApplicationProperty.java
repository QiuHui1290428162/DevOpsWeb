package com.lanf.common.info;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//资源配置文件
@Component()
//查询配置文件的路径
@PropertySource("classpath:application.properties")
public class ApplicationProperty {


    @Value("${spring.mail.username}")
    private String formEmail;

    @Value("${excel.email.export.address}")
    private String emailExportAddressToExcel;


    public String getFormEmail() {
        return formEmail;
    }

    public void setFormEmail(String formEmail) {
        this.formEmail = formEmail;
    }

    public String getEmailExportAddressToExcel() {
        return emailExportAddressToExcel;
    }

    public void setEmailExportAddressToExcel(String emailExportAddressToExcel) {
        this.emailExportAddressToExcel = emailExportAddressToExcel;
    }


}
