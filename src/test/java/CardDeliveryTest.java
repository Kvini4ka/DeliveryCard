import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    private WebDriver driver;

    private String dateForTest(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @Test
    void shouldEnterHappyDate() {
        String planningDate = dateForTest(5);

        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Катина Катерина");
        $("[data-test-id=phone] input").setValue("+71300000000");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $(".notification__content")
                .shouldHave(Condition.text(("Встреча успешно забронирована на " + planningDate)), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void shouldEnterHappyDateBySearchCityOfList() {
        String planningDate = dateForTest(4);

        $("[data-test-id=city] input").setValue("но");
        $$(".menu-item__control").first().click();
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Катина Катерина");
        $("[data-test-id=phone] input").setValue("+71300000000");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $(".notification__content")
                .shouldHave(Condition.text(("Встреча успешно забронирована на " + planningDate)), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void shouldEnterIncorrectDate() {
        String planningDate = dateForTest(1);

        $("[data-test-id=city] input").setValue("но");
        $$(".menu-item__control").first().click();
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Катина Катерина");
        $("[data-test-id=phone] input").setValue("+71300000000");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldEnterIncorrectCity() {
        String planningDate = dateForTest(5);

        $("[data-test-id=city] input").setValue("Бобруйск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Катина Катерина");
        $("[data-test-id=phone] input").setValue("+791300000000");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldEnterIncorrectName() {
        String planningDate = dateForTest(5);

        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Katina Kate");
        $("[data-test-id=phone] input").setValue("+791300000000");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldEnterIncorrectPhone() {
        String planningDate = dateForTest(5);

        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Катина Катерина");
        $("[data-test-id=phone] input").setValue("+7913000000");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }


    @Test
    void shouldEnterIncorrectCheckbox() {
        String planningDate = dateForTest(5);

        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Катина Катерина");
        $("[data-test-id=phone] input").setValue("+79130000000");
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=agreement] .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldEnterEmptyCity() {
        String planningDate = dateForTest(5);

        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Катина Катерина");
        $("[data-test-id=phone] input").setValue("+791300000000");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }


    @Test
    void shouldEnterEmptyDate() {


        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=name] input").setValue("Катина Катерина");
        $("[data-test-id=phone] input").setValue("+79130000000");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }


    @Test
    void shouldEnterEmptyName() {
        String planningDate = dateForTest(5);

        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+791300000000");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldEnterEmptyPhone() {
        String planningDate = dateForTest(5);

        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Катерина Катина");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));

    }




}