package cab.app.paymentservice.service;

import cab.app.paymentservice.dto.request.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.response.PayResponse;
import cab.app.paymentservice.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    void createPayment(CreatePaymentRequest paymentRequest);

    void deletePayment(Long paymentId);

    PayResponse payForRide(PayRequest payRequest);

    List<PaymentResponse> getAllPayments();

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse getPaymentByRide(Long rideId);
}
