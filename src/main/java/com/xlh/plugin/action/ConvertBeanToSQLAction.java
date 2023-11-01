package com.xlh.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiAnnotationImpl;
import com.xlh.plugin.ui.SQLResultDialog;
import com.xlh.plugin.utils.Notification;
import com.xlh.plugin.utils.SqlUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * 转换实体类为SQL语句
 * @author xingluheng
 * @date 2023/07/21 16:06
 **/
public class ConvertBeanToSQLAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
        PsiManager psiManager = PsiManager.getInstance(project);
        PsiFile psiFile = psiManager.findFile(file);
        PsiJavaFile javaFile = (PsiJavaFile) psiFile;
        PsiClass[] classes = javaFile.getClasses();
        if (classes.length!=1) {
            Notification.balloonNotify(project,"当前文件不支持生成SQL", MessageType.ERROR);
        }
        
        String sql = SqlUtils.convertToMySQL(classes[0], getTableName(classes[0]));
        Notification.balloonNotify(project,"SQL生成成功", MessageType.INFO);
        SQLResultDialog.showDialog(project,sql);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(e.getProject());
        FileEditor selectedEditor = fileEditorManager.getSelectedEditor();
        // 检查是否有选择的编辑器
        if (selectedEditor == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }
        VirtualFile virtualFile = selectedEditor.getFile();
        // 检查是否有虚拟文件
        if (virtualFile == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }
        FileType fileType = virtualFile.getFileType();
        e.getPresentation().setEnabledAndVisible("JAVA".equals(fileType.getName()));
    }

    /**
     * 获取注释的表名称
     * @param psiClass
     * @return
     */
    public String getTableName(PsiClass psiClass){
        String tableName = psiClass.getName().toLowerCase();
        for (PsiAnnotation annotation : psiClass.getAnnotations()) {
            if ("com.baomidou.mybatisplus.annotation.TableName".equals(annotation.getQualifiedName())) {
                PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
                tableName = value == null ? tableName : value.getText().replace("\"","`");
            }
        }
        return tableName;
    }

}
