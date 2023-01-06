package com.promineotech.jeep.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.promineotech.jeep.entity.Jeep;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;

@RequestMapping("/jeeps")// Week 13
@OpenAPIDefinition(info = @Info(title = "Jeep Sales Service"), servers = {
	@Server(url = "http://localhost:8080", description = "Local server.")})
public interface JeepSalesController {

	//@formatter:off
	@Operation(//Week 13
		summary = "Returns a list of Jeeps",
		description = "Returns a list of Jeeps given an optional model and/or trim",
		responses = {
			
			@ApiResponse(responseCode = "200", //Week 13
					description = "A list of Jeeps is returned", 
					content = @Content(
					mediaType = "application/json", 
					schema = @Schema(
					implementation = Jeep.class))),
			
			@ApiResponse(responseCode = "400",  //Week 13
					description = "The request parameters are invalid", 
					content = @Content(
					mediaType = "application/json")),
			
			@ApiResponse(responseCode = "404",  //Week 13
					description = "No Jeeps were found wih the input criteria", 
					content = @Content(
					mediaType = "application/json")),
			
			@ApiResponse(responseCode = "500",  //Week 13
					description = "An unplanned error occured.", 
					content = @Content(
					mediaType = "application/json"))
		},
		parameters = { //Week 13
			@Parameter(
					name = "model", 
					allowEmptyValue = false, 
					required = false, 
					description = "The model name (i.e., 'WRANGLER')"),
			@Parameter(  //Week 13
					name = "trim", 
					allowEmptyValue = false, 
					required = false, 
					description = "The trim level (i.e., 'Sport')")
		}
	)
	
	@GetMapping  //Week 13
	@ResponseStatus(code = HttpStatus.OK)
	List<Jeep> fetchJeeps(
			@RequestParam(required = false) 
				String model, 
			@RequestParam(required = false) 
				String trim);
	//@formatter:on
}
