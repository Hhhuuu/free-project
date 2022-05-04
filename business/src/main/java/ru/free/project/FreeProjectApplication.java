package ru.free.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {
        "ru.free.project",
        "ru.free.project.filters"
})
@ConfigurationPropertiesScan("ru.free.project")
public class FreeProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeProjectApplication.class, args);
    }
}
