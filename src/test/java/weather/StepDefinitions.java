package weather;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StepDefinitions {

    private Response response;
    private String apiKey = WeatherConfig.API_KEY;
    private final String baseUri = WeatherConfig.BASE_URI;

    @Given("ключ API для погоды")
    public void apiKey() {
        assertNotNull(apiKey, "Ключ API не задан");
    }

    @Given("несуществующий ключ API для погоды")
    public void incorrectApiKey() {
        apiKey = "testkey";
        assertNotNull(apiKey, "Ключ API не задан");
    }

    @When("^я запрашиваю текущую погоду для (.+)$")
    public void requestTheWeather(String city) {
        response = RestAssured.given()
                .queryParam("key", apiKey)
                .queryParam("q", city)
                .when()
                .get(baseUri);
    }

    @When("^я запрашиваю без токена текущую погоду для (.+)$")
    public void requestTheWeatherWithNoToken(String city) {
        response = RestAssured.given()
                .queryParam("q", city)
                .when()
                .get(baseUri);
    }

    @When("^я запрашиваю текущую погоду без города")
    public void requestTheNoCityWeather() {
        response = RestAssured.given()
                .queryParam("key", apiKey)
                .when()
                .get(baseUri);
    }

    @Then("ответ содержит ожидаемые данные о погоде для (.+)$")
    public void checkTheResponse(String city) {
        // Проверка названия города
        String actualCityName = response.jsonPath().get("location.name");
        assertEquals(city, actualCityName, String.format("Название города не совпало, город в ответе: %s", actualCityName));

        // Проверка температуры
        assertNotNull(response.jsonPath().get("current.temp_c"), "Температуры нет в ответе");

        // Проверка влажности
        assertNotNull(response.jsonPath().get("current.humidity"), "Влажности нет в ответе");

        // Проверка скорости ветра
        assertNotNull(response.jsonPath().get("current.wind_kph"), "Скорости ветра нет в ответе");

        // Проверка давления
        assertNotNull(response.jsonPath().get("current.pressure_mb"), "Давления нет в ответе");
    }

    @Then("ответ содержит Error Code (.+)$")
    public void checkResponseErrorCode(int errorCode) {
        int actualCode = response.jsonPath().get("error.code");
        assertEquals(actualCode, errorCode, String.format("Error code не %s, а %s", errorCode, actualCode));
    }

    @Then("ответ содержит Status Code (.+)$")
    public void checkResponseStatusCode(int statusCode) {
        assertEquals(statusCode, response.getStatusCode(), String.format("Неверный татус ответа - %s (ожидаемый - %d)", response.getStatusCode(), 400));
    }
}
