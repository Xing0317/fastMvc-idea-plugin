package com.xlh.plugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @author xingluheng
 * @date 2023/07/21 21:23
 **/
public class SQLResultDialog extends DialogWrapper {
    private JBTextArea sqlArea;

    public SQLResultDialog(Project project){
        super(project,true);
        setTitle("SQL Result");
        setOKButtonText("Copy");
        sqlArea = new JBTextArea();
        JBScrollPane scrollPane = new JBScrollPane(sqlArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return sqlArea;
    }

    @Override
    protected void doOKAction() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(sqlArea.getText());
        clipboard.setContents(selection,null);
        this.close(0);
    }

    public JBTextArea getSqlArea() {
        return sqlArea;
    }

    public static void showDialog(Project project, String sql) {
        SQLResultDialog dialog = new SQLResultDialog(project);
        dialog.getSqlArea().setText(sql);
        dialog.show();
    }
}
