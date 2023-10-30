package com.xlh.plugin.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.xlh.plugin.data.po.Model;
import com.xlh.plugin.data.vo.CodeGenContextVO;
import com.xlh.plugin.utils.Notification;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author xingluheng
 * @date 2023/07/20 16:53
 **/
public abstract class AbstractFileGenerator extends GeneratorConfig implements FileGenerator{
    private static final Logger LOG = Logger.getInstance(AbstractFileGenerator.class);


    @Override
    public void generation(Project project, CodeGenContextVO codeGenContextVO) {
        generateFile(project,codeGenContextVO);
    }

    /**
     * 生成文件
     * @param project
     * @param codeGenContextVO
     */
    protected abstract void generateFile(Project project,CodeGenContextVO codeGenContextVO);


    public void writeFile(Project project, String packageName, String name, String ftl, Object model) {
        if (StringUtil.isEmpty(packageName)){
            return;
        }

        ApplicationManager.getApplication().runWriteAction(() -> {
            VirtualFile virtualFile = null;
            virtualFile = createPackageDir(packageName);
            if (virtualFile.findChild(name) == null) {
                // 在此处执行您的写操作代码
                try {
                    VirtualFile childData = virtualFile.createChildData(project, name);
                    StringWriter stringWriter = new StringWriter();
                    Template template = super.getTemplate(ftl);
                    template.process(model, stringWriter);
                    childData.setBinaryContent(stringWriter.toString().getBytes(StandardCharsets.UTF_8));
                } catch (IOException | TemplateException e) {
                    LOG.error(e);
                    Notification.balloonNotify(project,e.getMessage(), MessageType.ERROR);
                }
            }
            });


    }

    private static VirtualFile createPackageDir(String packageName) {
        String path = FileUtil.toSystemIndependentName(StringUtil.replace(packageName, ".", "/"));
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }

    protected String getFileComment(String tableName,String typeName){
        if (tableName.endsWith("表")){
            tableName = tableName.substring(0, tableName.length() - 1) + typeName;
        }else {
            tableName = tableName + typeName;
        }
        return tableName;
    }

}
