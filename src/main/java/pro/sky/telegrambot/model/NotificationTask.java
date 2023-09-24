package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationTaskId;
    private Long chatId;
    private String textMessage;
    private LocalDateTime scheduleDateTime;

    public NotificationTask() {
    }

    public NotificationTask(Long notificationTaskId, Long chatId, String textMessage, LocalDateTime scheduleDateTime) {
        this.notificationTaskId = notificationTaskId;
        this.chatId = chatId;
        this.textMessage = textMessage;
        this.scheduleDateTime = scheduleDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(notificationTaskId, that.notificationTaskId) && Objects.equals(chatId, that.chatId) && Objects.equals(textMessage, that.textMessage) && Objects.equals(scheduleDateTime, that.scheduleDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationTaskId, chatId, textMessage, scheduleDateTime);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "notificationTaskId=" + notificationTaskId +
                ", chatId=" + chatId +
                ", textMessage='" + textMessage + '\'' +
                ", scheduleDateTime=" + scheduleDateTime +
                '}';
    }

    public void setNotificationTaskId(Long notificationTaskId) {
        this.notificationTaskId = notificationTaskId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setScheduleDateTime(LocalDateTime scheduleDateTime) {
        this.scheduleDateTime = scheduleDateTime;
    }

    public Long getNotificationTaskId() {
        return notificationTaskId;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public LocalDateTime getScheduleDateTime() {
        return scheduleDateTime;
    }
}
