package com.xlh.plugin.service.impl;

import com.google.common.base.CaseFormat;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.xlh.plugin.data.po.*;
import com.xlh.plugin.data.vo.CodeGenContextVO;
import com.xlh.plugin.service.AbstractFileGenerator;
import com.xlh.plugin.utils.JavaType;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xingluheng
 * @date 2023/07/19 20:21
 **/
public class FileGeneratorImpl extends AbstractFileGenerator {

    @Override
    protected void generateFile(Project project, CodeGenContextVO codeGenContextVO) {
        List<Table> tables = codeGenContextVO.getTables();
        for (Table table : tables) {
            List<Column> columns = table.getColumns();
            List<Field> fields = new ArrayList<>();
            for (Column column : columns) {
                Field field = new Field(column.getComment(), JavaType.convertType(column.getType()), column.getName());
                field.setId(column.isId());
                fields.add(field);
            }
            // 生成PO (pojo注释)表注释 全路径,表名,字段
            Model model = new Model(table.getComment(),
                    codeGenContextVO.getPojoDir()+ File.separator +
                            CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
                            table.getName()), table.getName(), fields);
            writeFile(project, codeGenContextVO.getPojoDir(), model.getSimpleName() + ".java", "mvc/model.ftl", model);


            // 生成DAO
            Dao dao = new Dao(table.getComment(),
                    codeGenContextVO.getDaoDir() + File.separator +
                            CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName()) + "Dao", model);
            writeFile(project, codeGenContextVO.getDaoDir(), dao.getSimpleName() + ".java", "mvc/dao.ftl", dao);

            // 生成Mapper
            writeFile(project, codeGenContextVO.getXmlDir(),
                    dao.getModel().getSimpleName() + "Dao.xml", "mvc/mapper.ftl",new Dao(dao.getPackage()+"."+dao.getSimpleName()));

            // 生成Service
            String baseName = codeGenContextVO.getServiceDir() + File.separator +
                    CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName());
            Set<String> packages = new HashSet<>();
            packages.add(model.getPackage()+"."+model.getSimpleName());
            Service service = new Service(getFileComment(table.getComment(), "服务"), baseName + "Service", packages,"");
            writeFile(project, codeGenContextVO.getServiceDir(),
                    dao.getModel().getSimpleName() + "Service.java", "mvc/service.ftl",
                    service);

            // 生成ServiceImpl
            packages.add(dao.getPackage()+"."+dao.getSimpleName());
            packages.add(service.getPackage()+"."+service.getSimpleName());
            baseName = codeGenContextVO.getServiceDir() + File.separator +"impl" + File.separator +
                    CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName());
            Service serviceImpl = new Service(getFileComment(table.getComment(), "服务实现类"), baseName + "ServiceImpl", packages,dao.getSimpleName());
            writeFile(project, codeGenContextVO.getServiceDir()+ File.separator +"impl/",
                    dao.getModel().getSimpleName() + "ServiceImpl.java", "mvc/serviceImpl.ftl", serviceImpl);

            // 生成Controller
            packages.clear();
            packages.add(service.getPackage()+"."+service.getSimpleName());
            baseName = codeGenContextVO.getControllerDir() + File.separator +
                    CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table.getName());
            writeFile(project, codeGenContextVO.getControllerDir(), dao.getModel().getSimpleName() + "Controller.java",
                    "mvc/controller.ftl", new Controller(getFileComment(table.getComment(),"接口"),baseName+"Controller",packages,service.getSimpleName()));

        }
    }
}
