package io.dataease.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EnvParameters {

    @Value("${rootPath}")
    private String rootPath;

}
