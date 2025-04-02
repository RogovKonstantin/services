import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class TransportationOrderRepository {
    private final Map<Integer, TransportationOrder> storage = new HashMap<>();

    public void save(TransportationOrder order) {
        storage.put(order.getId(), order);
        System.out.println("Заявка сохранена: " + order);
    }

    public TransportationOrder findById(int id) {
        return storage.get(id);
    }

    public Collection<TransportationOrder> findAll() {
        return storage.values();
    }
}
