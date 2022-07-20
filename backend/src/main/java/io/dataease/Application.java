package io.dataease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication(exclude = {
        LdapAutoConfiguration.class
})
@ServletComponentScan
@EnableScheduling
@PropertySource(value = "file:/${rootPath}/dataease.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
