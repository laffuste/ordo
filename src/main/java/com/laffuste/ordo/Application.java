package com.laffuste.ordo;

import com.laffuste.ordo.properties.loaders.ClasspathPropertiesLoader;
import com.laffuste.ordo.properties.loaders.DefaultPropertiesLoader;
import com.laffuste.ordo.properties.PropertyLoaderBuilder;
import com.laffuste.ordo.properties.parsers.JavaPropertyParser;
import com.laffuste.ordo.properties.parsers.PropertiesFileParser;
import com.laffuste.ordo.properties.parsers.YamlPropertyParser;
import com.laffuste.ordo.validation.application.properties.OrdoValidationProperties;
import com.laffuste.ordo.validation.adapter.SimpleController;
import com.laffuste.ordo.validation.application.properties.ValidationPropertiesMapper;
import com.laffuste.ordo.validation.application.OrderValidationService;
import com.laffuste.ordo.validation.application.in.OrderValidationUseCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
    /**
     * Name f the jvm arg that will give the classpath path of the properties file.
     */
    public static final String CONFIG_JVM_ARG = "app.config";
    /**
     * Port for the web server.
     */
    public static final int WEB_PORT = 1234;

    public static void main(String[] args) throws Exception {
        // init properties
        OrdoValidationProperties ordoValidationProperties = resolveProperties();
        log.info("Using properties: {}", ordoValidationProperties);

        // init services
        OrderValidationUseCase orderValidationUseCase = new OrderValidationService(ordoValidationProperties);
        log.info("Validator service loaded");

        // init interfaces (web)
        SimpleController c = new SimpleController(orderValidationUseCase);
        c.start(WEB_PORT);
    }

    private static OrdoValidationProperties resolveProperties() {
        PropertiesFileParser javaPropertyLoader = new JavaPropertyParser();
        PropertiesFileParser yamlPropertyLoader = new YamlPropertyParser();
        return PropertyLoaderBuilder.<OrdoValidationProperties>builder()
                .add(new DefaultPropertiesLoader(javaPropertyLoader))
                .add(new ClasspathPropertiesLoader(CONFIG_JVM_ARG, yamlPropertyLoader, javaPropertyLoader))
                .mapper(ValidationPropertiesMapper::toOrdoProperties)
                .load();
    }

}
