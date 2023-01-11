package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
//import org.springframework.test.jdbc.JdbcTestUtils;
//import org.springframework.test.jdbc.JdbcTestUtils;

import com.promineotech.jeep.Constants;
import com.promineotech.jeep.controller.support.FetchJeepTestSupport;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.service.JeepSalesService;




class FetchJeepTest extends FetchJeepTestSupport {
	
	@Nested
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)  //Week 13
	@ActiveProfiles("test")
	@Sql(scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql", 
			"classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
			config = @SqlConfig(encoding = "utf-8"))
	class TestsThatDoNotPolluteTheAnnotationContext extends FetchJeepTestSupport{
		/**
		 * 
		 */

		@Test
		void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {  //Week 13
			//Given: a valid model, trim and URI
			JeepModel model = JeepModel.WRANGLER;
			String trim = "Sport";
			String uri = 
					String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
			
			//When: a connection is made to the URI   
			ResponseEntity<List<Jeep>> response = 
					getRestTemplate().exchange(uri, HttpMethod.GET, null, 
							new ParameterizedTypeReference<>() {});
			
			//Then: a success (OK - 200) status code is returned
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);   //Week 13
			
			//And:the actual list returned is the same as the expected list
			List<Jeep> actual = response.getBody();
			List<Jeep> expected = buildExpected();
			
		
			assertThat(actual).isEqualTo(expected);
		
		}
	/**
	 * 
	 */

	@Test
	void testThatAnErrorMessageIsReturnedWhenAnUnknownTrimIsSupplied() { 
		//Given: a valid model, trim and URI
		JeepModel model = JeepModel.WRANGLER;
		String trim = "Unkown Value";
		String uri = 
				String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
		
		//When: a connection is made to the URI   
		ResponseEntity<Map<String, Object>> response = 
				getRestTemplate().exchange(uri, 
					HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
		
		//Then: a not found (404) status is returned
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);   //Week 13
		
		//And:An error message is returned
		Map<String, Object> error = response.getBody();
		
		assertErrorMessageValid(error, HttpStatus.NOT_FOUND);

	}

	@ParameterizedTest
	@MethodSource("com.promineotech.jeep.controller.FetchJeepTest#parametersForInvalidInput")
	void testThatAnErrorMessageIsReturnedWhenAnIvalidValueIsSupplied(
			String model, String trim, String reason) { 
		//Given: a valid model, trim and URI
		String uri = 
				String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
		
		//When: a connection is made to the URI   
		ResponseEntity<Map<String, Object>> response = 
				getRestTemplate().exchange(uri, 
					HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
		
		//Then: a not found (404) status is returned
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);   //Week 13
		
		//And:An error message is returned
		Map<String, Object> error = response.getBody();
		
		assertErrorMessageValid(error, HttpStatus.BAD_REQUEST);

	}	
	}
	
	static Stream<Arguments> parametersForInvalidInput(){
		//@formatter:off
		return Stream.of(
				arguments("WRANGLER", "@#$%^&&%", "Trim contains non-alpha-numeric charaters"),
				arguments("WRANGLER", "C".repeat(Constants.TRIM_MAX_LENGTH + 1), "Trim length too long"),
				arguments("INVALID", "Sport","Model is not enum value")
		//formatter:on		
		);
	}

	@Nested
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)  //Week 13
	@ActiveProfiles("test")
	@Sql(scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql", 
			"classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
			config = @SqlConfig(encoding = "utf-8"))
	class TestsThatPolluteTheAnnotationContext extends FetchJeepTestSupport{
	@MockBean
	private JeepSalesService jeepSalesService;
	/**
	 * 
	 */

	@Test
	void testThatAnUnplannedErrorResultsInA500Status() { 
		//Given: a valid model, trim and URI
		JeepModel model = JeepModel.WRANGLER;
		String trim = "Invalid";
		String uri = 
				String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
		
		doThrow(new RuntimeException("Ouch!")).when(jeepSalesService)
		.fetchJeeps(model, trim);
		
		//When: a connection is made to the URI   
		ResponseEntity<Map<String, Object>> response = 
				getRestTemplate().exchange(uri, 
					HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
		
		//Then: an internal server error (500) is returned
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);   //Week 13
		
		//And:An error message is returned
		Map<String, Object> error = response.getBody();
		
		assertErrorMessageValid(error, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	}
	
}




	

