package com.xlh.plugin.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import com.xlh.plugin.data.DataSetting;
import com.xlh.plugin.data.po.Table;
import com.xlh.plugin.data.vo.CodeGenContextVO;
import com.xlh.plugin.data.vo.FastMVCConfigVO;
import com.xlh.plugin.service.FileGenerator;
import com.xlh.plugin.utils.DBHelper;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * @author xingluheng
 * @date 2023/07/19 19:44
 **/
public class DBConfigUI implements Configurable {
    private TextFieldWithBrowseButton daoPath;
    private TextFieldWithBrowseButton pojoPath;
    private TextFieldWithBrowseButton xmlPath;
    private TextFieldWithBrowseButton servicePath;
    private TextFieldWithBrowseButton controllerPath;
    private JTextField serverAddress;
    private JTextField username;
    private JTextField tablePrefix;
    private JTextField database;
    private JButton showTables;
    private JButton connect;
    private JPanel main;
    private JPasswordField password;
    private JScrollPane tablePanel;
    private JBTable table;
    private JTextField author;

    private Project project;
    private FastMVCConfigVO configVO;
    private FileGenerator fileGenerator;

    public DBConfigUI(Project project, FileGenerator fileGenerator) {
        this.project = project;
        configVO = DataSetting.getInstance(project).getFastMVCConfigVO();
        configVO.setTableNames(new HashSet<>());
        this.fileGenerator = fileGenerator;
        initPersistenceData();
        addOpenFileListener();
        connect.addActionListener(e -> {
            connectListener(project);
        });
        showTables.addActionListener(e -> {
            showDataBaseListener(project);
        });
        this.table.getEmptyText().setText("Please Click '查询表名' And Select Table");
        // 给表添加事件
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (1 == e.getClickCount()) {
                    int rowIdx = table.rowAtPoint(e.getPoint());
                    Boolean flag = (Boolean) table.getValueAt(rowIdx, 0);
                    Set<String> tableNames = configVO.getTableNames();
                    if (null != flag && flag) {
                        tableNames.add(table.getValueAt(rowIdx, 1).toString());
                    } else {
                        tableNames.remove(table.getValueAt(rowIdx, 1).toString());
                    }
                }
            }
        });
    }

    private void showDataBaseListener(Project project) {
        // 查询库里表名
        try {
            DBHelper dbHelper = getDbHelper();
            Map<String, String> allTableNameMap = dbHelper.getAllTableName(getDataBase());
            String[] columnNames = {"选择", "表名", "表注释"};
            Object[][] data = new Object[allTableNameMap.size()][3];
            int rowIndex = 0;
            for (Map.Entry<String, String> entry : allTableNameMap.entrySet()) {
                // 获取表名和表注释
                String tableName = entry.getKey();
                String tableComment = entry.getValue();
                // 将数据放入二维数组对应的位置
                data[rowIndex][1] = tableName;
                data[rowIndex][2] = tableComment;
                rowIndex++;
            }
            // 绘制表格
            table.setModel(new DefaultTableModel(data, columnNames));
            TableColumn tc = table.getColumnModel().getColumn(0);
            tc.setCellEditor(new DefaultCellEditor(new JCheckBox()));
            tc.setCellEditor(table.getDefaultEditor(Boolean.class));
            tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
            tc.setMaxWidth(100);
            main.validate();
            main.repaint();
        } catch (Exception exception) {
            Messages.showWarningDialog(project, "数据库连接错误,请检查配置.", "Warning");
        }
    }

    private void connectListener(Project project) {
        //测试连接
        String[] split = getServerAddress().split(":");
        try {
            DBHelper dbHelper = getDbHelper();
            String mysqlVersion = dbHelper.testDatabase();
            Messages.showInfoMessage(project, "Connection successful! \r\nMySQL version : " + mysqlVersion, "OK");
        } catch (Exception exception) {
            Messages.showWarningDialog(project, "数据库连接错误,请检查配置.", "Warning");
        }
    }

    @NotNull
    private DBHelper getDbHelper() {
        String[] split = getServerAddress().split(":");
        return new DBHelper(split[0], Integer.parseInt(split[1]),getUserName(),getPassword(),getDataBase());
    }

    private void initPersistenceData() {
        this.daoPath.setText(configVO.getDaoPath());
        this.xmlPath.setText(configVO.getXmlPath());
        this.pojoPath.setText(configVO.getPojoPath());
        this.servicePath.setText(configVO.getServicePath());
        this.controllerPath.setText(configVO.getControllerPath());
        this.serverAddress.setText(configVO.getServerAddress());
        this.username.setText(configVO.getUsername());
        this.password.setText(configVO.getPassword());
        this.tablePrefix.setText(configVO.getTablePrefix());
        this.database.setText(configVO.getDatabase());
        this.author.setText(configVO.getAuthor());
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Generate Config";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return main;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        // 获取配置
        configVO.setDaoPath(daoPath.getText());
        configVO.setXmlPath(xmlPath.getText());
        configVO.setPojoPath(pojoPath.getText());
        configVO.setServicePath(servicePath.getText());
        configVO.setControllerPath(controllerPath.getText());
        configVO.setServerAddress(serverAddress.getText());
        configVO.setUsername(username.getText());
        configVO.setPassword(password.getText());
        configVO.setTablePrefix(tablePrefix.getText());
        configVO.setDatabase(database.getText());
        configVO.setAuthor(author.getText());
        // 生成代码

        CodeGenContextVO codeGenContext = new CodeGenContextVO();
        codeGenContext.setPojoDir(pojoPath.getText());
        codeGenContext.setDaoDir(daoPath.getText());
        codeGenContext.setXmlDir(xmlPath.getText());
        codeGenContext.setServiceDir(servicePath.getText());
        codeGenContext.setControllerDir(controllerPath.getText());
        List<Table> tables = new ArrayList<>();
        Set<String> tableNames = configVO.getTableNames();
        for (String tableName : tableNames) {
            tables.add(getDbHelper().getTable(tableName,tablePrefix.getText()));
        }
        codeGenContext.setTables(tables);
        codeGenContext.setAuthor(author.getText());
        // 生成代码
        fileGenerator.generation(project, codeGenContext);
    }

    public String getServerAddress(){
        return serverAddress.getText();
    }
    public String getUserName(){
        return username.getText();
    }
    public String getPassword(){
        return password.getText();
    }
    private String getDataBase() {
        return database.getText();
    }


    private void addOpenFileListener(){
        daoPath.addBrowseFolderListener(getTextBrowseFolderListener());
        pojoPath.addBrowseFolderListener(getTextBrowseFolderListener());
        xmlPath.addBrowseFolderListener(getTextBrowseFolderListener());
        servicePath.addBrowseFolderListener(getTextBrowseFolderListener());
        controllerPath.addBrowseFolderListener(getTextBrowseFolderListener());
    }

    @NotNull
    private TextBrowseFolderListener getTextBrowseFolderListener() {
        FileChooserDescriptor cliChooserDescriptor = new FileChooserDescriptor
                (false, true, false, false, false, false);
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(Objects.requireNonNull(project.getBasePath()));
        cliChooserDescriptor.setRoots(virtualFile);
        TextBrowseFolderListener folderListener = new TextBrowseFolderListener(cliChooserDescriptor);
        return folderListener;
    }


}
