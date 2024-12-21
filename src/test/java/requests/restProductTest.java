package requests;

import data.Specifications;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pojos.productPojo;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.reset;

public class restProductTest {

    private static final CookieFilter cookiefilter = new CookieFilter();

    @BeforeAll
    static void prepare(){
        Specifications.installSpec(
                Specifications.requestSpecification("http://localhost:8080"),
                Specifications.responseSpecification(200)
        );
    }

    @Test
    void addNonExoticVegetable(){
        String data = "{\"name\": \"Огурец\", \"type\": \"VEGETABLE\", \"exotic\": false}";

        given()
                .filter(cookiefilter)
                .basePath("/api/food")
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Test
    void addExoticVegetable(){
        String data = "{\"name\": \"Бамия\", \"type\": \"VEGETABLE\", \"exotic\": true}";

        given()
                .filter(cookiefilter)
                .basePath("/api/food")
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Test
    void addNonExoticFruit(){
        String data = "{\"name\": \"Лимон\", \"type\": \"FRUIT\", \"exotic\": false}";

        given()
                .filter(cookiefilter)
                .basePath("/api/food")
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Test
    void addExoticFruit(){
        String data = "{\"name\": \"Инжир\", \"type\": \"FRUIT\", \"exotic\": true}";

        given()
                .filter(cookiefilter)
                .basePath("/api/food")
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();
    }


    @AfterAll
    static void testEnd(){
        List<productPojo> products = given()
                .filter(cookiefilter)
                .basePath("/api/food")
                .when()
                .contentType(ContentType.JSON)
                .get("")
                .then()
                .log().all()
                .extract()
                .jsonPath().getList("", productPojo.class);

        Assertions.assertTrue(products.stream().anyMatch(
                product -> "Лимон".equals(product.getName()) && "FRUIT".equals(product.getType())));
        Assertions.assertTrue(products.stream().anyMatch(
                product -> "Инжир".equals(product.getName()) && "FRUIT".equals(product.getType())));
        Assertions.assertTrue(products.stream().anyMatch(
                product -> "Огурец".equals(product.getName()) && "VEGETABLE".equals(product.getType())));
        Assertions.assertTrue(products.stream().anyMatch(
                product -> "Бамия".equals(product.getName())
                        && "VEGETABLE".equals(product.getType())));

        given()
                .filter(cookiefilter)
                .basePath("/api/data/reset")
                .when()
                .post();

        reset();

        given()
                .filter(cookiefilter)
                .basePath("/api/food")
                .when()
                .contentType(ContentType.JSON)
                .get("")
                .then()
                .log().all()
                .extract()
                .jsonPath().getList("", productPojo.class);
    }
}
