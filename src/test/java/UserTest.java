import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.*;
import static steps.UserStep.*;

public class UserTest extends BaseApiTest {
    private UserModel user;
    private String userAccessToken;

    @Before
    public void setUser() {
        user = new UserModel(EMAIL, PASSWORD, NAME);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверяет успешное создание уникального пользователя с валидными данными")
    public void testCreateUserSuccess() {
        Response resCreateUser = createUser(user);
        userAccessToken = getUserAccessToken(resCreateUser);
        resCreateUser.then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(EMAIL))
                .body("user.name", equalTo(NAME))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверяет ошибку при попытке создать пользователя, который уже существует")
    public void testCreateDoubleUserFailure() {
        Response resCreateUser = createUser(user);
        userAccessToken = getUserAccessToken(resCreateUser);
        createUser(user).then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    @Description("Проверяет ошибку при попытке создать пользователя без заполнения одного из обязательных полей (email)")
    public void testCreateUserWithoutEmailFailure() {
        user.setEmail(null);
        createUser(user).then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверяет ошибку при попытке создать пользователя без заполнения пароля")
    public void testCreateUserWithoutPasswordFailure() {
        user.setPassword(null);
        createUser(user).then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверяет ошибку при попытке создать пользователя без заполнения имени")
    public void testCreateUserWithoutNameFailure() {
        user.setName(null);
        createUser(user).then()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        // Код для удаления созданного пользователя
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
