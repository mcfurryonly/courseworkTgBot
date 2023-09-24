package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.BotListenerRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private BotListenerRepository botListenerRepository;


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String messageText = update.message().text();
            long chatId = update.message().chat().id();
            if (messageText.equals("/start")) {
                SendResponse response = telegramBot.execute(new SendMessage(chatId, "Hello!"));
            }
            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

            Matcher matcher = pattern.matcher(messageText);
            System.out.println("matcher = " + matcher);
           CharSequence dateTime = null;
            if (matcher.find()) {
                dateTime = matcher.group(1);

                System.out.println("messageText = " + messageText);
                System.out.println("chatId = " + chatId);
                System.out.println("dateTime = " + dateTime);
            }
            if (dateTime != null) {
                LocalDateTime localDateTime = LocalDateTime.parse(
                        dateTime,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                );
                NotificationTask notificationTask = new NotificationTask(
                        1l,
                        chatId,
                        messageText,
                        localDateTime);
                botListenerRepository.save(notificationTask);
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    @Scheduled(cron = "0 0/1 * * * *")
    public void scheduledMessage() {
        logger.info("scheduleMessage() starts");

        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        logger.info("currentDateTime is installed = {}", currentDateTime);

        List<NotificationTask> notificationTasks = botListenerRepository.findByScheduleDateTime(currentDateTime);

        notificationTasks.stream()
                .forEach(notificationTask -> {
                    SendResponse responseMessage = telegramBot.execute(new SendMessage(
                            notificationTask.getChatId(),
                            notificationTask.getTextMessage()));
                    if (responseMessage.isOk()) {
                        logger.info("responseMessage = OK");
                    } else {
                        logger.info("responseMessage = BAD");
                    }
                });

    }

}
