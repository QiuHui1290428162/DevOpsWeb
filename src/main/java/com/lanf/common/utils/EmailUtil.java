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

    public static String[] trimRecipientEmails(String emails) {
        if (StringUtil.isNullOrEmptyString(emails)) {
            return null;
        }

        // 去除邮箱地址的前后空格和分号
        emails = emails.trim();
        if (emails.charAt(emails.length() - 1) == ';'){
            emails = emails.substring(0, emails.length() - 1);
        }

        // 分割字符串为各个邮箱地址
        String[] emailArray = emails.split(";");
        for ( int i=0; i<emailArray.length; i++ ) {
            emailArray[i] = emailArray[i].trim();
            //校验邮件
            if (!isValidEmailAddress(emailArray[i])) {
                return null;
            }
        }
        return emailArray;
    }

}
