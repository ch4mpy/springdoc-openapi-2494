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
import com.c4soft.DemoController.EnumSerializedByToString;

@WebMvcTest(controllers = DemoController.class)
class DemoControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @Test
    void whenUsingNameUnlessToStringIsDecoratedWithJsonValue_thenOk() throws Exception {
        mockMvc.perform(get("/demo").param("name", EnumSerializedByName.A.name()).param("str", EnumSerializedByToString.A.toString()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("name", is(EnumSerializedByName.A.name())))
          .andExpect(jsonPath("str", is(EnumSerializedByToString.A.toString())));
    }

    @Test
    void whenUsingToStringOutputOnEnumWithToStringWithoutJsonValue_thenKo() throws Exception {
        mockMvc.perform(get("/demo").param("name", EnumSerializedByName.A.toString()).param("str", EnumSerializedByToString.A.name()))
          .andExpect(status().is4xxClientError());
    }

    @Test
    void whenUsingNameOnEnumWithToStringWithJsonValue_thenKo() throws Exception {
        mockMvc.perform(get("/demo").param("name", EnumSerializedByName.A.name()).param("str", EnumSerializedByToString.A.name()))
          .andExpect(status().is4xxClientError());
    }

}
