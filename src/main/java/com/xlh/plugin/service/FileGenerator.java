package com.xlh.plugin.service;

import com.intellij.openapi.project.Project;
import com.xlh.plugin.data.vo.CodeGenContextVO;

/**
 * @author xingluheng
 * @date 2023/07/19 20:20
 **/
public interface FileGenerator {
    /**
     * 生成通用代码
     * @param project
     * @param codeGenContextVO
     */
    void generation(Project project, CodeGenContextVO codeGenContextVO);
}
