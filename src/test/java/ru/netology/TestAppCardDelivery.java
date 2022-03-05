package ru.netology;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestAppCardDelivery {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        Configuration.holdBrowserOpen = true;
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
    }

    @Test
    public void shouldUseFaker() {
        Faker faker = new Faker(new Locale("ru"));
        String myDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").val(faker.address().cityName());
        $("[data-test-id='date'] input").sendKeys(myDate);
        $("[data-test-id='name'] input").val(faker.name().fullName());
        $("[data-test-id='phone'] input").val(faker.number().digits(11));
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='success-notification']").shouldHave(text("Встреча успешно запланирована на " + myDate));
    }

    @Test
    public void shouldRescheduleDate() {
        Faker faker = new Faker(new Locale("ru"));
        String myDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").val(faker.address().cityName());
        $("[data-test-id='date'] input").sendKeys(myDate);
        $("[data-test-id='name'] input").val(faker.name().fullName());
        $("[data-test-id='phone'] input").val(faker.number().digits(11));
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='success-notification']").shouldHave(text("Встреча успешно запланирована на " + myDate));
        $(".button").click();
        $("[data-test-id='replan-notification']").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(byText("Перепланировать")).click();
        $("[data-test-id='success-notification']").shouldHave(text("Встреча успешно запланирована на " + myDate));
    }
}