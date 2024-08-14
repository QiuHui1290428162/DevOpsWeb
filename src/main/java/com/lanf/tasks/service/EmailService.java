package com.lanf.tasks.service;

import com.lanf.common.exception.CacheExpiredException;
import com.lanf.tasks.model.TaskScheduledEmail;

public interface EmailService {

    public void sendMail(String RecipientEmail, String ccEmail, String subject, String Body, String filePath) throws CacheExpiredException;
    public void sendMail(TaskScheduledEmail task);
}
