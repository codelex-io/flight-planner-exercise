package io.codelex.flightplanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class RestTemplateConfiguration {
    @Value('${flight-planner-app.admin.username}')
    String adminUsername

    @Value('${flight-planner-app.admin.password}')
    String adminPassword

    @Autowired
    PasswordEncoder passwordEncoder

    @Bean
    TestRestTemplate testingApiTemplate(RestTemplateFactory restTemplateFactory) {
        return restTemplateFactory.create('/testing-api/')
    }

    @Bean
    TestRestTemplate publicApiTemplate(RestTemplateFactory restTemplateFactory) {
        return restTemplateFactory.create('/api/')
    }

    @Bean
    TestRestTemplate adminApiTemplate(RestTemplateFactory restTemplateFactory) {
        return restTemplateFactory.create('/admin-api/')
                .withBasicAuth(adminUsername, adminPassword)
    }
}
