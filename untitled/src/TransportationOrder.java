import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransportationOrder {
    private static int idCounter = 1;
    private final int id;
    private OrderStatus status;
    private DeliveryRoute route;
    private final List<Message> messages;
    private LocalDateTime lastActivity;

    public TransportationOrder(DeliveryRoute route) {
        this.id = idCounter++;
        this.route = route;
        this.status = OrderStatus.active();
        this.messages = new ArrayList<>();
        this.lastActivity = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public DeliveryRoute getRoute() {
        return route;
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void addMessage(Message message) {
        if (!status.isActive()) {
            throw new IllegalStateException("Нельзя добавить сообщение в закрытую заявку.");
        }
        messages.add(message);
        lastActivity = LocalDateTime.now();
        System.out.println("В заявке " + id + " добавлено сообщение: " + message);
    }

    public void changeStatus(OrderStatus newStatus) {
        this.status = newStatus;
        System.out.println("Статус заявки " + id + " изменён на: " + newStatus.getStatus());
    }

    public void autoCloseIfInactive() {
        LocalDateTime now = LocalDateTime.now();
        Duration inactivity = Duration.between(lastActivity, now);
        if (inactivity.toMinutes() >= 10 && status.isActive()) {
            changeStatus(OrderStatus.closed());
            System.out.println("Заявка " + id + " закрыта автоматически из-за отсутствия активности.");
        }
    }

    @Override
    public String toString() {
        return "TransportationOrder{" +
                "id=" + id +
                ", status=" + status +
                ", route=" + route +
                ", messages=" + messages +
                ", lastActivity=" + lastActivity +
                '}';
    }
}
