public class OrderLifecycleService {
    private final TransportationOrderRepository repository;

    public OrderLifecycleService(TransportationOrderRepository repository) {
        this.repository = repository;
    }

    public TransportationOrder createOrder(String origin, String destination) {
        TransportationOrder order = new TransportationOrder(new DeliveryRoute(origin, destination));
        repository.save(order);
        System.out.println("Создана заявка с ID: " + order.getId());
        return order;
    }


    public void changeOrderStatus(int orderId, OrderStatus newStatus) {
        TransportationOrder order = repository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Заявка с ID " + orderId + " не найдена.");
        }
        order.changeStatus(newStatus);
        repository.save(order);
    }

    public void closeOrder(int orderId) {
        changeOrderStatus(orderId, OrderStatus.closed());
    }

    public void reopenOrder(int orderId) {
        changeOrderStatus(orderId, OrderStatus.reopened());
    }

    public void autoCloseOrderIfInactive(int orderId) {
        TransportationOrder order = repository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Заявка с ID " + orderId + " не найдена.");
        }
        order.autoCloseIfInactive();
        repository.save(order);
    }
}
