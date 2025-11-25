package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.UserModel;
import static io.restassured.RestAssured.given;

public class UserStep {

    public static final String CREATE_USER_PATH = "/api/auth/register";
    public static final String DELETE_USER_PATH = "/api/auth/user";
    public static final String LOGIN_PATH = "/api/auth/login";

    @Step("Создать пользователя")
    public static Response createUser(UserModel userModel) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(userModel)
                .when()
                .post(CREATE_USER_PATH)
                .then()
                .log().all()
                .extract().response();
    }
    @Step("Получить токен доступа")
    public static String getUserAccessToken(Response response) {
        return response.path("accessToken");
    }

    @Step("Логин пользователя")
    public static Response loginUser(UserModel userModel, String userAccessToken) {
        return given()
                .log().all()
                .header("Authorization", userAccessToken)
                .contentType(ContentType.JSON)
                .body(userModel)
                .when()
                .post(LOGIN_PATH)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Удалить пользователя")
    public static void deleteUser(String userAccessToken) {
        given()
                .log().all()
                .header("Authorization", userAccessToken)
                .delete(DELETE_USER_PATH)
                .then()
                .log().all();
    }

}