package custis.young.seminars.dirtymagic.mock;

import java.util.Locale;

public class GreetingServiceImpl implements GreetingService {

    private final MessageRepository messageRepository;

    public GreetingServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public String sayHello(String name) {
        final Locale locale = Locale.getDefault();
        final String message = messageRepository.getMessage(locale);
        return String.format(message, name);
    }
}
