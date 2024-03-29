package io.starter.ignite.generator.swagger;

import io.swagger.codegen.v3.CodegenConfig;
import io.swagger.codegen.v3.CodegenConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackGenCodegenConfigLoader extends CodegenConfigLoader{

    protected final static Logger logger = LoggerFactory
            .getLogger(StackGenCodegenConfigLoader.class);

    /**
     * 
     * load StackGen Generator
     * 
     * @param name name of config, or full qualified class name in classpath
     * @return config class
     */
    public static CodegenConfig forName(String name) {
        logger.info("Loading StackGenSpringCodegen: " + name);
    	CodegenConfig ret = null;

    	try{
    	   ret = CodegenConfigLoader.forName(name);
        }catch(Exception e){;}

    	 // else try to load directly
        if(ret == null) {
            try {
                ret = (CodegenConfig) Class.forName("io.starter.ignite.generator.swagger.languages.StackGenSpringCodegen").getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Can't load config class with name: " + name, e);
            }
        }
        return ret;     
    }
}
