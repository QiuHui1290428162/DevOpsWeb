package com.lanf.tasks.service;

import com.lanf.common.exception.GlobalExpiredException;
import com.lanf.tasks.model.TaskScheduledEmail;

public interface EmailService {

    public void sendMail(String RecipientEmail, String ccEmail, String subject, String Body, String filePath) throws GlobalExpiredException;
    public void sendMail(TaskScheduledEmail task);
}
