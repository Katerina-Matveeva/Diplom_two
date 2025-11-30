package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.OrderModel;

import static io.restassured.RestAssured.given;

public class OrderStep {

    public static final String CREATE_ORDER_PATH = "/api/orders";

    @Step("Создать заказ")
    public static Response createOrder(OrderModel orderModel, String userAccessToken) {
        return given()
                .log().all()
                .header("Authorization", userAccessToken)
                .contentType(ContentType.JSON)
                .body(orderModel)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .log().all()
                .extract().response();
    }
}