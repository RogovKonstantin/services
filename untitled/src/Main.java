
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TransportationOrderRepository repository = new TransportationOrderRepository();
        OrderLifecycleService lifecycleService = new OrderLifecycleService(repository);
        FraudDetectionService fraudDetectionService = new FraudDetectionService();

        // Создание маршрута
        DeliveryRoute route = new DeliveryRoute("Warsaw", "Madrid");

        // Создание заявки через агрегат
        TransportationOrder order = new TransportationOrder(route);
        repository.save(order);

        // Проверка маршрута
        System.out.println("Маршрут: " + order.getRoute().getOrigin() + " -> " + order.getRoute().getDestination());

        // Проверка текущего статуса
        System.out.println("Начальный статус: " + order.getStatus().getStatus());

        // Добавление сообщений с вложениями
        Attachment a1 = new Attachment("map.pdf", new byte[]{1, 2});
        Attachment a2 = new Attachment("bill.jpg", new byte[]{3, 4});
        Attachment a3 = new Attachment("checklist.txt", new byte[]{5, 6});

        Message m1 = new Message("Документы по маршруту", Role.CUSTOMER, Role.PROVIDER, Collections.singletonList(a1));
        Message m2 = new Message("Подтверждаю получение", Role.PROVIDER, Role.CUSTOMER, Collections.singletonList(a2));
        Message m3 = new Message("Дополнительные требования", Role.CUSTOMER, Role.PROVIDER, Collections.singletonList(a3));
        Message m4 = new Message("Все выполнено", Role.PROVIDER, Role.CUSTOMER, Collections.emptyList());
        Message m5 = new Message("Еще вопрос", Role.CUSTOMER, Role.PROVIDER, Collections.emptyList());
        Message m6 = new Message("Ответ на вопрос", Role.PROVIDER, Role.CUSTOMER, Collections.emptyList());

        order.addMessage(m1);
        order.addMessage(m2);
        order.addMessage(m3);
        order.addMessage(m4);
        order.addMessage(m5);
        order.addMessage(m6);

        // Подтверждение сообщений противоположной стороной
        m1.confirm(Role.PROVIDER);
        m2.confirm(Role.CUSTOMER);

        // Попытка некорректного подтверждения
        try {
            m3.confirm(Role.CUSTOMER); // отправитель пытается подтвердить сам
        } catch (Exception e) {
            System.out.println("Ожидаемая ошибка при подтверждении: " + e.getMessage());
        }

        // Проверка активности
        System.out.println("Последняя активность заявки: " + order.getLastActivity());

        // Проверка на мошенничество (6 сообщений — подозрительно)
        fraudDetectionService.detectFraud(order);

        // Смена статуса через доменный сервис
        lifecycleService.changeOrderStatus(order.getId(), OrderStatus.reopened());

        // Явное закрытие заявки
        lifecycleService.closeOrder(order.getId());

        // Попытка добавить сообщение в закрытую заявку
        try {
            Message m7 = new Message("Попытка после закрытия", Role.CUSTOMER, Role.PROVIDER, Collections.emptyList());
            order.addMessage(m7);
        } catch (Exception e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
        }

        // Переоткрытие заявки
        lifecycleService.reopenOrder(order.getId());

        // Повторное добавление сообщения после переоткрытия
        Message m8 = new Message("Новый документ после переоткрытия", Role.CUSTOMER, Role.PROVIDER, Collections.emptyList());
        order.addMessage(m8);
        repository.save(order);

        // Проверка повторного статуса
        System.out.println("Статус после переоткрытия: " + order.getStatus().getStatus());

        // Автоматическое закрытие (эмуляция простоя)
        System.out.println("Ждём 10 секунд для имитации бездействия...");
        Thread.sleep(10000);

        lifecycleService.autoCloseOrderIfInactive(order.getId());

        TransportationOrder finalOrder = repository.findById(order.getId());
        System.out.println("Статус после авто-закрытия: " + finalOrder.getStatus().getStatus());

        // Вывод всех сообщений
        System.out.println("Все сообщения в заявке:");
        List<Message> allMessages = finalOrder.getMessages();
        for (Message msg : allMessages) {
            System.out.println(" - #" + msg.getId() + ": " + msg.getText() +
                    " | Подтверждено: " + msg.isConfirmed() +
                    " | От: " + msg.getSender() + " -> Кому: " + msg.getReceiver() +
                    " | Вложений: " + msg.getAttachments().size());
        }
    }
}
