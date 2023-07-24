package com.xlh.plugin.data.po;

import java.util.Set;

/**
 * @author xingluheng
 * @date 2023/07/20 21:59
 **/
public class Service extends Base{

    private Set<String> packages;
    private String resource;

    public Service(String comment, String name,Set<String> packages,String resource){
        super(comment,name);
        this.packages = packages;
        this.resource = resource;
    }
    @Override
    public Set<String> getImports() {
        return this.packages;
    }

    public String getResourceClassName(){
        return this.resource;
    }

    public String getResourceFieldName(){
        return Character.toLowerCase(resource.charAt(0)) + resource.substring(1);
    }


}
