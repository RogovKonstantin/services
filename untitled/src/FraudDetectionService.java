public class FraudDetectionService {

    public boolean detectFraud(TransportationOrder order) {
        if (order.getMessages().size() > 5) {
            System.out.println("FraudDetection: Заявка " + order.getId() + " имеет подозрительную активность (слишком много сообщений).");
            return true;
        }
        System.out.println("FraudDetection: Заявка " + order.getId() + " прошла проверку.");
        return false;
    }
}
