import java.util.Objects;

public final class OrderStatus {
    private final String status;

    public OrderStatus(String status) {
        this.status = status;
    }

    public static OrderStatus active() {
        return new OrderStatus("ACTIVE");
    }

    public static OrderStatus closed() {
        return new OrderStatus("CLOSED");
    }

    public static OrderStatus reopened() {
        return new OrderStatus("REOPENED");
    }

    public boolean isActive() {
        return "ACTIVE".equals(this.status) || "REOPENED".equals(this.status);
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderStatus)) return false;
        OrderStatus that = (OrderStatus) o;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        return "OrderStatus{" + "status='" + status + '\'' + '}';
    }
}
