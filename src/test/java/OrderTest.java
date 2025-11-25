import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.OrderModel;
import models.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.*;
import static steps.OrderStep.*;
import static steps.UserStep.*;

public class OrderTest extends BaseApiTest {

    private UserModel user;
    private String userAccessToken;
    private OrderModel order;

    @Before
    public void setUser() {
        this.user = new UserModel(EMAIL, PASSWORD, NAME);
        Response resCreateUser = createUser(user);
        this.userAccessToken = getUserAccessToken(resCreateUser);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверяет успешное создание заказа с авторизацией и валидными ингредиентами")
    public void testCreateOrderWithTokenSuccess() {
        ArrayList<String> ingredients = getOrdersIngredients(INGREDIENT_BUN, INGREDIENT_MEAT);
        order = new OrderModel(ingredients);

        createOrder(order, userAccessToken).then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("order.owner.name", equalTo(NAME))
                .body("order.owner.email", equalTo(EMAIL));
    }

    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    @Description("Проверяет успешное создание заказа без авторизации, но с валидными ингредиентами")
    public void testCreateOrderWithoutTokenSuccess() {
        ArrayList<String> ingredients = getOrdersIngredients(INGREDIENT_BUN, INGREDIENT_MEAT);
        order = new OrderModel(ingredients);

        createOrder(order, "").then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией без ингредиентов")
    @Description("Проверяет ошибку при попытке создать заказ с авторизацией, но без ингредиентов")
    public void testCreateOrderWithoutIngredientsFailure() {
        ArrayList<String> ingredients = getOrdersIngredients();
        order = new OrderModel(ingredients);

        createOrder(order, userAccessToken).then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и неверным хэшем ингредиентов")
    @Description("Проверяет ошибку при попытке создать заказ с авторизацией и неверными хэшами ингредиентов")
    public void testCreateOrderWithWrongIngredientsFailure() {
        ArrayList<String> ingredients = getOrdersIngredients(FIRST_WRONG_INGREDIENT, SECOND_WRONG_INGREDIENT);
        OrderModel order = new OrderModel(ingredients);

        createOrder(order, userAccessToken).then()
                .statusCode(HTTP_INTERNAL_ERROR);
    }

    @After
    public void tearDown() {
        // Код для удаления созданного пользователя
        if (userAccessToken != null) {
            deleteUser(userAccessToken);
        }
    }
}
