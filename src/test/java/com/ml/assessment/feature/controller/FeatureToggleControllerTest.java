package com.ml.assessment.feature.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml.assessment.feature.dto.DTOFeatureAvailability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.containsString;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FeatureToggleControllerTest {

    private String ACCESS_TRUE = "\"canAccess\" : true";
    private String ACCESS_FALSE = "\"canAccess\" : false";
    private String EMAIL = "myemail@email.com";
    private String FEATURE1 = "MyNewFeature";
    private String FEATURE2 = "MyAnotherFeature";
    private String TYPE_JSON = "application/json";
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void checkFeatureOnEmptyDatabase() throws Exception {
        this.mockMvc.perform(
                get("/feature")
                        .requestAttr("email", EMAIL)
                        .requestAttr("featureName", FEATURE1)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString(ACCESS_FALSE)));

    }

    @Test
    @Transactional
    void toggleFeatureAndCheck() throws Exception {

        DTOFeatureAvailability availability
                = new DTOFeatureAvailability(EMAIL, FEATURE1, true);

        this.mockMvc.perform(
                post("/feature")
                        .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(availability))
                        .contentType(TYPE_JSON)
                )
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/feature")
                                .requestAttr("email", EMAIL)
                                .requestAttr("featureName", FEATURE1)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString(ACCESS_TRUE)));

    }

    @Test
    @Transactional
    void toggleFeatureAndCheckAnother() throws Exception {

        DTOFeatureAvailability availability
                = new DTOFeatureAvailability(EMAIL, FEATURE1, true);

        this.mockMvc.perform(
                        post("/feature")
                                .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(availability))
                                .contentType(TYPE_JSON)
                )
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/feature")
                                .requestAttr("email", EMAIL)
                                .requestAttr("featureName", FEATURE2)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString(ACCESS_FALSE)));

        availability = new DTOFeatureAvailability(EMAIL, FEATURE2, true);

        this.mockMvc.perform(
                        post("/feature")
                                .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(availability))
                                .contentType(TYPE_JSON)
                )
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/feature")
                                .requestAttr("email", EMAIL)
                                .requestAttr("featureName", FEATURE2)
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString(ACCESS_TRUE)));

    }

    @Test
    @Transactional
    void toggleSameFeaturesAndFound() throws Exception {

        DTOFeatureAvailability availability
                = new DTOFeatureAvailability(EMAIL, FEATURE1, true);

        this.mockMvc.perform(
                        post("/feature")
                                .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(availability))
                                .contentType(TYPE_JSON)
                )
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        post("/feature")
                                .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(availability))
                                .contentType(TYPE_JSON)
                )
                .andExpect(status().isFound());
    }
}