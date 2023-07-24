package com.xlh.plugin.data.po;

import java.util.Set;

/**
 * @author xingluheng
 * @date 2023/07/20 22:17
 **/
public class Controller extends Base{

    private Set<String> packages;
    private String resource;

    public Controller(String comment,String name,Set<String> packages,String resource){
        super(comment,name);
        this.packages = packages;
        this.resource = resource;
    }

    @Override
    public Set<String> getImports() {
        return packages;
    }

    public String getResourceClassName(){
        return this.resource;
    }

    public String getResourceFieldName(){
        return Character.toLowerCase(resource.charAt(0)) + resource.substring(1);
    }
}
