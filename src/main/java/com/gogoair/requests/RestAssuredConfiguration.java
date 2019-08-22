package com.gogoair.requests;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestAssuredConfiguration {
	
	public UserDetails userDetails;
	
	@BeforeSuite(alwaysRun = true)
	@Parameters({"email", "first_name", "last_name", "gender", "authorization"})
	public void configure(String email, String firstName, String lastName, String gender, String authorization){
	//public void configure(){
			RestAssured.baseURI = "https://gorest.co.in";
			RestAssured.basePath="/public-api";
			//this.token = user01_token;
			
			
			userDetails = new UserDetails(email, firstName, lastName, gender, authorization);
			
	}
	
	public RequestSpecification getRequestSpecification() {
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Authorization", userDetails.getAuthorization());
		return httpRequest;
	}
	



}
