package com.gogoair.post.request.validations;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.gogoair.requests.EndPoint;
import com.gogoair.requests.RestAssuredConfiguration;
import com.gogoair.requests.Result;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;

import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Map;

public class APIValidations extends RestAssuredConfiguration {

	private RequestSpecification requestSpecification;

	@BeforeMethod
	public void setRequestSpecification() {

		requestSpecification = getRequestSpecification();

	}

	//TC01 - Validate POST request to add new User
	@Test(priority = 1)
	public void validatePostRequest() {

		JSONObject requestParams = new JSONObject();
		requestParams.put("email", userDetails.getEmail());
		requestParams.put("first_name", userDetails.getFirst_name());
		requestParams.put("last_name", userDetails.getLast_name());
		requestParams.put("gender", userDetails.getGender());

		requestSpecification.body(requestParams.toJSONString());

		Response response = given().spec(requestSpecification).post(EndPoint.ENDPOINT_USERS);
		System.out.println(response.getBody().asString());
		System.out.println(response.getStatusLine());
		response.then().statusCode(302).log().all();

		JsonPath jsonPath = response.jsonPath();

		Map<String, String> map = jsonPath.getMap("result");
		userDetails.setId(map.get("id"));

	}

	//TC02 - Validate POST request with Invalid field value(Gender is otherthan Male or Female)
	@Test(priority = 2)
	public void validatePostRequestWithInvalidGender() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("email", userDetails.getEmail());
		requestParams.put("first_name", userDetails.getFirst_name());
		requestParams.put("last_name", userDetails.getLast_name());
		requestParams.put("gender", (userDetails.getGender() + userDetails.getGender()));

		requestSpecification.body(requestParams.toJSONString());

		Response response = given().spec(requestSpecification).post(EndPoint.ENDPOINT_USERS);
		System.out.println(response.getBody().asString());
		System.out.println(response.getStatusLine());
		response.then().statusCode(200).log().all();

		JsonPath jsonPath = response.jsonPath();

		Map<String, String> meta = jsonPath.getMap("_meta");
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(meta.get("message").contains("Data validation failed"),
				"Validating Invalid Gender FAILED");

		List<Result> result = jsonPath.getList("result", Result.class);
		boolean foundMesg = false;
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).getMessage().contains("Gender is invalid."))
				; 
			foundMesg = true;
		}

		softAssert.assertTrue(foundMesg, "Validating Invalid Gender FAILED");

		softAssert.assertAll();
	}

	//TC03 - Validate POST request with Missing field(Gender field is missed in request body)
	@Test(priority = 3)
	public void validatePostRequestWithMissingField() {

		JSONObject requestParams = new JSONObject();
		requestParams.put("email", userDetails.getEmail());
		requestParams.put("first_name", userDetails.getFirst_name());
		requestParams.put("last_name", userDetails.getLast_name());
		// requestParams.put("gender", userDetails.getGender());

		requestSpecification.body(requestParams.toJSONString());

		Response response = given().spec(requestSpecification).post(EndPoint.ENDPOINT_USERS);
		System.out.println(response.getBody().asString());
		System.out.println(response.getStatusLine());
		response.then().statusCode(200).log().all();

		JsonPath jsonPath = response.jsonPath();
		Map<String, String> meta = jsonPath.getMap("_meta");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(meta.get("message").contains("Data validation failed"),
				"Validating Missing field FAILED");
		
		List<Result> result = jsonPath.getList("result", Result.class);
		boolean foundMesg = false;
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).getMessage().contains("Gender cannot be blank"))
				; 
			foundMesg = true;
		}

		softAssert.assertTrue(foundMesg, "Validating Invalid Gender FAILED");
		


		softAssert.assertAll();
	}

	//TC04 - Validate POST request with Invalid Authorization
	@Test(priority = 4)
	public void validatePostRequestWithInvalidAuthorization() {

		requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.header("Authorization", "L41uyhzxoig9roHjTyRHFvBT48K2GjCMjbVC");

		JSONObject requestParams = new JSONObject();
		requestParams.put("email", userDetails.getEmail());
		requestParams.put("first_name", userDetails.getFirst_name());
		requestParams.put("last_name", userDetails.getLast_name());
		requestParams.put("gender", userDetails.getGender());

		requestSpecification.body(requestParams.toJSONString());

		Response response = given().spec(requestSpecification).post(EndPoint.ENDPOINT_USERS);
		System.out.println(response.getBody().asString());
		System.out.println(response.getStatusLine());
		response.then().statusCode(200).log().all();

		JsonPath jsonPath = response.jsonPath();
		Map<String, String> result = jsonPath.getMap("result");
		Map<String, String> meta = jsonPath.getMap("_meta");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(meta.get("message").contains("Authentication failed"), "Authentication failed");
		softAssert.assertTrue(result.get("message").contains("Your request was made with invalid credentials"),
				"Validating Invalid Gender FAILED");

		softAssert.assertAll();

	}

	
	//TC05 - Validate POST request with Missing Authorization
	@Test(priority = 5)
	public void validatePostRequestWithMissingAuthorization() {

		requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		//requestSpecification.header("Authorization", "L41uyhzxoig9roHjTyRHFvBT48K2GjCMjbVC");

		JSONObject requestParams = new JSONObject();
		requestParams.put("email", userDetails.getEmail());
		requestParams.put("first_name", userDetails.getFirst_name());
		requestParams.put("last_name", userDetails.getLast_name());
		requestParams.put("gender", userDetails.getGender());

		requestSpecification.body(requestParams.toJSONString());

		Response response = given().spec(requestSpecification).post(EndPoint.ENDPOINT_USERS);
		System.out.println(response.getBody().asString());
		System.out.println(response.getStatusLine());
		response.then().statusCode(200).log().all();

		JsonPath jsonPath = response.jsonPath();
		Map<String, String> result = jsonPath.getMap("result");
		Map<String, String> meta = jsonPath.getMap("_meta");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(meta.get("message").contains("Authentication failed"), "Authentication failed");
		softAssert.assertTrue(result.get("message").contains("Your request was made with invalid credentials"),
				"Validating Invalid Gender FAILED");

		softAssert.assertAll();

	}
	
	//TC06 - Validate GET request with userId
	@Test(dependsOnMethods = { "validatePostRequest" })
	public void validateGetRequest() {

		Response response = given().spec(requestSpecification).get(EndPoint.ENDPOINT_USERS + "/" + userDetails.getId());
		System.out.println(response.getBody().asString());
		System.out.println(response.getStatusLine());
		response.then().statusCode(200).log().all();

		JsonPath jsonPath = response.jsonPath();
		Map<String, String> result = jsonPath.getMap("result");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(result.get("first_name").equals(userDetails.getFirst_name()),
				"FIRST_NAME is NOT Matched");
		softAssert.assertTrue(result.get("last_name").equals(userDetails.getLast_name()), "LAST_NAME is NOT Matched");
		softAssert.assertTrue(result.get("gender").equals(userDetails.getGender()), "GENDER is NOT Matched");
		softAssert.assertTrue(result.get("email").equals(userDetails.getEmail()), "EMAIL is NOT Matched");

		softAssert.assertAll();

	}

}
