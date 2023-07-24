package com.xlh.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.xlh.plugin.service.FileGenerator;
import com.xlh.plugin.service.impl.FileGeneratorImpl;
import com.xlh.plugin.ui.DBConfigUI;
import org.jetbrains.annotations.NotNull;

/**
 * 生成按钮入口
 * @author xingluheng
 * @date 2023/07/19 19:33
 **/
public class GenerateAction extends AnAction {

    private final FileGenerator generator = new FileGeneratorImpl();
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ShowSettingsUtil.getInstance().editConfigurable(e.getProject(), new DBConfigUI(e.getProject(),generator));
    }
}
