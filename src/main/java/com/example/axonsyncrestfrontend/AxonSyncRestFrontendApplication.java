package com.example.axonsyncrestfrontend;

import com.thoughtworks.xstream.XStream;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class AxonSyncRestFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AxonSyncRestFrontendApplication.class, args);
    }

    /**
     * Setting tracking processors to make projection logic asynchronous.
     */
    @Autowired
    public void configure(EventProcessingConfiguration configuration) {
        configuration.usingTrackingProcessors();
    }

    /**
     * Setting up Swagger to create a GUI on our API
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.axonsyncrestfrontend"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Correctly configuration XStream security to avoid ugly warnings.
     */
    @Autowired
    public void configure(Serializer serializer) {
        if(serializer instanceof XStreamSerializer) {
            XStream xStream = ((XStreamSerializer)serializer).getXStream();
            XStream.setupDefaultSecurity(xStream);
            xStream.allowTypesByWildcard(new String[] { "com.example.axonsyncrestfrontend.**", "org.axonframework.**" });
        }
    }

}
