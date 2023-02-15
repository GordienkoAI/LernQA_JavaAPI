import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWork {

// Ex10: Тест на короткую фразу
    @ParameterizedTest
    @ValueSource(strings = {"1234567890123456", "123456789012"})
    public void checkStringLength(String value){
        assertTrue(value.length() > 15, "Value length lees 15 simbols");
    }
}
