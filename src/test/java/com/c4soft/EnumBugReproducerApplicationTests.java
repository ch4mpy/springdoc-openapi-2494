package com.c4soft;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.c4soft.DemoController.EnumSerializedByName;
import com.c4soft.DemoController.EnumSerializedByToString;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@SpringBootTest
@AutoConfigureMockMvc
class EnumBugReproducerApplicationTests {
	@Autowired
	MockMvc mockMvc;

	JsonPath namePath = JsonPath.compile("$.name");
	JsonPath strPath = JsonPath.compile("$.str");

	JsonPath namePossibleValuesPath = JsonPath.compile("$.components.schemas.Dto.properties.name.enum.*");
	JsonPath strPossibleValuesPath = JsonPath.compile("$.components.schemas.Dto.properties.str.enum.*");

	@Test
	void whenUsingNameUnlessToStringIsDecoratedWithJsonValue_thenOk() throws Exception {
		final var actual = mockMvc
				.perform(get("/demo").param("name", EnumSerializedByName.A.name()).param("str", EnumSerializedByToString.A.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("name", is(EnumSerializedByName.A.name())))
				.andExpect(jsonPath("str", is(EnumSerializedByToString.A.toString())))
				.andReturn()
				.getResponse()
				.getContentAsString();

		final var actualEnumSerializedByName = namePath.read(actual);
		final var actualEnumSerializedByToString = strPath.read(actual);

		final var spec = mockMvc.perform(get("/v3/api-docs")).andReturn().getResponse().getContentAsString();
		final var namePossibleValues = (JSONArray) namePossibleValuesPath.read(spec);
		final var strPossibleValues = (JSONArray) strPossibleValuesPath.read(spec);

		assertTrue(strPossibleValues.contains(actualEnumSerializedByToString));
		assertTrue(namePossibleValues.contains(actualEnumSerializedByName));
	}

}
