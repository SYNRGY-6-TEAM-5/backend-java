package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.PaymentResponseDTO;
import com.finalproject.Tiket.Pesawat.dto.payment.request.CreateVaPaymentRequest;
import com.finalproject.Tiket.Pesawat.dto.payment.request.RequestWebhookXendit;
import com.finalproject.Tiket.Pesawat.dto.payment.response.SupportPaymentResponse;
import com.finalproject.Tiket.Pesawat.service.PaymentService;
import com.xendit.enums.BankCode;
import com.xendit.model.FixedPaymentCode;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.finalproject.Tiket.Pesawat.utils.Constants.XENDIT_API_BASE_URL;

@Log4j2
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${aeroswift.xendit.username}")
    private String username;

    @GetMapping("/success-payment")
    public ModelAndView showSuccessPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("success");
        return modelAndView;
    }

    @GetMapping("/failed-payment")
    public ModelAndView showFailedPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("failed");
        return modelAndView;
    }



    // todo buat retailnya juga ( 2 ENDPOINT)
    @Hidden
    @PostMapping("/xendit-payment/create-va")
    public ResponseEntity<PaymentResponseDTO> paymentXendit(@Valid @RequestBody CreateVaPaymentRequest request) {
        PaymentResponseDTO response = paymentService.createPaymentXendit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/xendit-payment/webhook-create")
    @Hidden
    public ResponseEntity webhookXenditCreate(@RequestHeader("x-callback-token") String xCallbackToken,
                                              @RequestBody RequestWebhookXendit requestWebhook) {
        log.info("executing webhhook");
        String response = paymentService.createVirtualAccountXenditWebhook(xCallbackToken, requestWebhook);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/xendit-payment/webhook-paid")
    public ResponseEntity webhookXenditPaid(@RequestHeader("x-callback-token") String xCallbackToken,
                                            @RequestBody RequestWebhookXendit requestWebhook) { // todo paid requestnya berebeda dengan create
        log.info("executing webhhook");
        String responseWebhook = paymentService.paidXenditVirtualAccountWebhook(xCallbackToken, requestWebhook);
        return ResponseEntity.ok(responseWebhook);
    }

    // todo ditaruh diservice
    // todo websecurity config ( public access )
    @GetMapping("/xendit/support-payment")
    public ResponseEntity<SupportPaymentResponse> getPaymentSupport() {
        List<String> bankCodes = Arrays.stream(BankCode.values())
                .map(BankCode::getText)
                .collect(Collectors.toList());

        List<String> retailOutletNames = Arrays.stream(FixedPaymentCode.RetailOutletName.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        SupportPaymentResponse response = SupportPaymentResponse.builder()
                .banks(bankCodes)
                .retails(retailOutletNames)
                .build();
        return ResponseEntity.ok(response);
    }

    // todo pindahkan ke service
    @PostMapping(value = "/simulate_payment", produces = "application/json")
    public ResponseEntity<Object> simulatePayment(
            @RequestParam("amount") String amount,
            @RequestParam("external_id") String externalId
    ) {
        String requestBody = "{\"amount\":" + amount + "}";
        String apiUrl = XENDIT_API_BASE_URL + "external_id=" + externalId + "/simulate_payment";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, requestEntity, String.class);

            return ResponseEntity.ok(responseEntity.getBody());
        } catch (HttpClientErrorException.BadRequest e) {
            String errorMessage = extractErrorMessage(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    private String extractErrorMessage(HttpClientErrorException.BadRequest e) {
        try {
            return e.getResponseBodyAsString();
        } catch (Exception ex) {
            return "Bad Request: " + e.getMessage();
        }
    }

}
