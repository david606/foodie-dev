package com.imooc.mybatis.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class GeneratorDisplay {

    public void generate() throws Exception {

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;

        File configFile = getConfigFile();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

        // 打印异常
        printWarnings(warnings);
    }

    private void printWarnings(List<String> warnings) {
        if (CollectionUtils.isEmpty(warnings))
            return;
        warnings.forEach(System.out::println);
    }

    private File getConfigFile() throws Exception {
        // 获取 generatorConfig.xml 文件的URL
        URL resourceUrl = getClass().getClassLoader().getResource("generatorConfig.xml");
        if (resourceUrl == null) {
            throw new Exception("The generatorConfig.xml file was not found in the resources directory.");
        }

        // 将URL转换为File对象
        File configFile;
        try {
            configFile = new File(resourceUrl.toURI());
        } catch (URISyntaxException e) {
            throw new Exception("There was a problem converting the resource URL to a File object.", e);
        }
        return configFile;
    }

    public static void main(String[] args) throws Exception {
        GeneratorDisplay generatorSqlmap = new GeneratorDisplay();
        generatorSqlmap.generate();
    }
}
