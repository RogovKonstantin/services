import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private static int idCounter = 1;
    private final int id;
    private final String text;
    private final Role sender;
    private final Role receiver;
    private final List<Attachment> attachments;
    private boolean isConfirmed;
    private final LocalDateTime timestamp;

    public Message(String text, Role sender, Role receiver, List<Attachment> attachments) {
        if (attachments == null) {
            throw new IllegalArgumentException("Список вложений не может быть null. Если вложений нет, передайте пустой список.");
        }
        this.id = idCounter++;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.attachments = new ArrayList<>(attachments);
        this.isConfirmed = false;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Role getSender() {
        return sender;
    }

    public Role getReceiver() {
        return receiver;
    }

    public List<Attachment> getAttachments() {
        return new ArrayList<>(attachments);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void confirm(Role confirmingRole) {
        if (isConfirmed) {
            throw new IllegalStateException("Сообщение уже подтверждено.");
        }
        if (confirmingRole != receiver) {
            throw new IllegalArgumentException("Подтверждение доступно только стороне, противоположной отправителю (" + receiver + ").");
        }
        isConfirmed = true;
        System.out.println("Сообщение " + id + " подтверждено стороной: " + confirmingRole);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", attachments=" + attachments +
                ", isConfirmed=" + isConfirmed +
                ", timestamp=" + timestamp +
                '}';
    }
}
