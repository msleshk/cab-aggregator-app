package cab.app.paymentservice.service;

import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.response.PayResponse;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.dto.response.ResponseList;

public interface PaymentService {
    void createPayment(CreatePaymentRequest paymentRequest);

    void deletePayment(Long paymentId);

    PayResponse payForRide(PayRequest payRequest);

    ResponseList<PaymentResponse> getAllPayments(int offset, int limit);

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse getPaymentByRide(Long rideId);
}
