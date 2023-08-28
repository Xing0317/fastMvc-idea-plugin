package com.xlh.plugin.utils;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;

public class Notification {
    /**
     * 右下角气泡方式提醒
     *
     * @param message
     * @param type
     */
    public static void balloonNotify(Project project, String message, MessageType type) {
        if (message == null) {
            return;
        }
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Custom Notification Group")
                .createNotification(message, type)
                .notify(project);
    }
}
