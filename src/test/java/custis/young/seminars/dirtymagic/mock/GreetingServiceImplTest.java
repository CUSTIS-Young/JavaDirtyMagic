package custis.young.seminars.dirtymagic.mock;

import org.testng.annotations.Test;

import java.util.Locale;

import static custis.young.seminars.dirtymagic.mock.MyMockFramework.*;
import static org.testng.Assert.assertEquals;

public class GreetingServiceImplTest {

    @Test public void sayHello_0() {

        // when messageRepository.getMessage(Locale.getDefault()) return "Здравствуй, %s!"

        final MessageRepository messageRepository = new MessageRepository() {
            @Override public String getMessage(Locale locale) {
                if (Locale.getDefault().equals(locale)) {
                    return "Здравствуй, %s!";
                } else {
                    return null;
                }
            }
        };

        final GreetingService greetingService = new GreetingServiceImpl(messageRepository);
        assertEquals(greetingService.sayHello("Мир"), "Здравствуй, Мир!");
    }

    @Test public void sayHello_1() {

        final MessageRepository messageRepository = mock(MessageRepository.class);

        // when messageRepository.getMessage(Locale.getDefault()) return "Здравствуй, %s!"
        messageRepository.getMessage(Locale.getDefault());
        thenReturn("Здравствуй, %s!");

        final GreetingService greetingService = new GreetingServiceImpl(messageRepository);
        assertEquals(greetingService.sayHello("Мир"), "Здравствуй, Мир!");
    }

    @Test public void sayHello_2() {

        final MessageRepository messageRepository = mock(MessageRepository.class);
        // when messageRepository.getMessage(Locale.getDefault()) then return "Здравствуй, %s!"
        when(messageRepository.getMessage(Locale.getDefault())).thenReturn("Здравствуй, %s!");

        final GreetingService greetingService = new GreetingServiceImpl(messageRepository);
        assertEquals(greetingService.sayHello("Мир"), "Здравствуй, Мир!");
    }

    @Test public void sayHello_3() {

        // создаем mock
        final MessageRepository messageRepository = mock(MessageRepository.class);

        // вызываем метод и запоминаем: mock, метод и аргументы
        String message = messageRepository.getMessage(Locale.getDefault());

        // захватываем типизацию
        MockBehaviorDefinition<String> mockBehaviorDefinition = when(message);

        // програмируем mock
        mockBehaviorDefinition.thenReturn("Здравствуй, %s!");

        final GreetingService greetingService = new GreetingServiceImpl(messageRepository);
        assertEquals(greetingService.sayHello("Мир"), "Здравствуй, Мир!");
    }

    @Test public void sayHello_4() {

        // создаем mock
        final MessageRepository messageRepository = mock(MessageRepository.class);

        // вызываем метод и запоминаем: mock, метод и аргументы
        messageRepository.getMessage(Locale.getDefault());

        // захватываем типизацию
        MockBehaviorDefinition<String> mockBehaviorDefinition = when(null);

        // програмируем mock
        mockBehaviorDefinition.thenReturn("Здравствуй, %s!");

        final GreetingService greetingService = new GreetingServiceImpl(messageRepository);
        assertEquals(greetingService.sayHello("Мир"), "Здравствуй, Мир!");
    }
}
