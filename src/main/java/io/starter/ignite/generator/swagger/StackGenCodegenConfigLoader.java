package io.starter.ignite.generator.swagger;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConfigLoader;

public class StackGenCodegenConfigLoader extends CodegenConfigLoader{
    /**
     * 
     * load StackGen Generator
     * 
     * @param name name of config, or full qualified class name in classpath
     * @return config class
     */
    public static CodegenConfig forName(String name) {
    	CodegenConfig ret = null;
    	 // else try to load directly
        try {
            ret = (CodegenConfig) Class.forName("io.starter.ignite.generator.swagger.languages.StackGenSpringCodegen").newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Can't load config class with name ".concat(name) , e);
        }
        return ret;
    }
}
