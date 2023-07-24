package com.xlh.plugin.service;

import com.thoughtworks.qdox.model.expression.Or;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;

/**
 * @author xingluheng
 * @date 2023/07/20 16:50
 **/
public class GeneratorConfig {
    private static final String ENCODING = "UTF-8";
    private static final FreemarkerConfiguration FREEMARKER = new FreemarkerConfiguration("/template/");

    protected Template getTemplate(String ftl) throws IOException {
        return FREEMARKER.getTemplate(ftl, ENCODING);
    }

    static class FreemarkerConfiguration extends Configuration {

        public FreemarkerConfiguration(String basePackagePath) {
            super(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            setDefaultEncoding(ENCODING);
            setClassForTemplateLoading(getClass(), basePackagePath);
        }

    }
}
