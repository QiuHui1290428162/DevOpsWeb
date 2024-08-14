package com.lanf.common.utils;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @email 邮箱号
 */
public class EmailUtil {


    //校检邮件格式合法性
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;

        try {
            //该类继承了邮件格式校检方法，直接调用。若报错，则邮件格式错误
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        }catch ( AddressException e ) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
