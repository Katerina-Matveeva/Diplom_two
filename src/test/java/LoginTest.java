import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static steps.UserStep.*;

public class LoginTest extends BaseApiTest {

    private String userAccessToken;  // Поле для токена

    @Before
    public  void createUserForTest() {
        // Создание пользователя перед каждым тестом
        UserModel user = new UserModel(EMAIL, PASSWORD, NAME);
        Response resCreate = createUser(user);
        this.userAccessToken = getUserAccessToken(resCreate);  // Сохраняем токен
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверяет успешный вход с валидными данными")
    public void testLoginExistingUserSuccess() {
        // Пользователь создан в @Before, используем его для логина
        UserModel user = new UserModel(EMAIL, PASSWORD, NAME);
        Response resLogin = loginUser(user, "");  // Без токена в header для логина
        resLogin.then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("accessToken", startsWith("Bearer "));
    }

    @Test
    @DisplayName("Логин с неверным логином")
    @Description("Проверяет ошибку при входе с неверным логином и правильным паролем")
    public void testLoginWrongLoginFailure() {
        UserModel wrongLoginUser = new UserModel(WRONG_EMAIL, PASSWORD, NAME);
        loginUser(wrongLoginUser, "").then()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверяет ошибку при входе с правильным логином и неверным паролем")
    public void testLoginWrongPasswordFailure() {
        UserModel wrongPasswordUser = new UserModel(EMAIL, WRONG_PASSWORD, NAME);
        loginUser(wrongPasswordUser, "").then()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        // Удаление пользователя, если он был создан (токен есть)
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}