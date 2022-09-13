package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME),
                Keys.BACK_SPACE);
        $x("//*[@placeholder='Дата встречи']").setValue(firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//span[@class='checkbox__box']").click();
        $x("//*[contains(text(), \"Запланировать\")]").click();
        $(".notification_status_ok .notification__content").shouldHave(text("Встреча успешно " +
                "запланирована на " + firstMeetingDate), Duration.ofSeconds(15)).shouldBe(visible);
        $x("//*[@placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME),
                Keys.BACK_SPACE);
        $x("//*[@placeholder='Дата встречи']").setValue(secondMeetingDate);
        $x("//*[contains(text(), \"Запланировать\")]").click();
        $(".notification_status_error .notification__content").shouldHave(text("У вас уже запланирована " +
                "встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15)).shouldBe(visible);
        $x("//*[contains(text(), \"Перепланировать\")]").click();
        $(".notification_status_ok .notification__content").shouldHave(text("Встреча успешно " +
                "запланирована на " + secondMeetingDate), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
