package com.c4soft;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.c4soft.DemoController.EnumSerializedByName;

@WebMvcTest(controllers = DemoController.class)
class DemoControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @Test
    void whenGetDemo_thenStatusIsSerializedByName() throws Exception {
        mockMvc.perform(get("/demo").param("status", EnumSerializedByName.ON.name())).andExpect(status().isOk()).andExpect(jsonPath("status", is(EnumSerializedByName.ON.name())));
        mockMvc.perform(get("/demo").param("status", EnumSerializedByName.ON.toString())).andExpect(status().is4xxClientError());
    }

}
