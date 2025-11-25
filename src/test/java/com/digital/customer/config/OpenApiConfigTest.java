package com.digital.customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpenApiConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
        assertTrue(context.containsBean("openApiConfig"));
    }

    @Test
    void customerOnboardingAPIBeanIsCreatedCorrectly() {
        OpenAPI openAPI = context.getBean(OpenAPI.class);
        assertNotNull(openAPI);

        Info info = openAPI.getInfo();
        assertNotNull(info);

        assertEquals("Customer Onboarding API", info.getTitle());
        assertEquals("1.0.0", info.getVersion());
        assertEquals("API for customer onboarding and account creation", info.getDescription());
    }
}
