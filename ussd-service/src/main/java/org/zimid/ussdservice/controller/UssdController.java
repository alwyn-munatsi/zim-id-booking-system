package org.zimid.ussdservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zimid.ussdservice.service.UssdMenuService;

@RestController
@RequestMapping("/api/v1/ussd")
@RequiredArgsConstructor
@Slf4j
public class UssdController {

    private final UssdMenuService ussdMenuService;

    /**
     * Africa's Talking USSD Webhook
     *
     * Request Parameters:
     * - sessionId: Unique session identifier
     * - serviceCode: USSD code dialed (e.g., *384*7001#)
     * - phoneNumber: User's phone number
     * - text: User's input (empty on first request)
     */
    @PostMapping(value = "/callback",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> handleUssdCallback(
            @RequestParam String sessionId,
            @RequestParam String serviceCode,
            @RequestParam String phoneNumber,
            @RequestParam(defaultValue = "") String text) {

        log.info("USSD Request - Session: {}, Phone: {}, Code: {}, Text: '{}'",
                sessionId, phoneNumber, serviceCode, text);

        try {
            String response = ussdMenuService.processUssdRequest(sessionId, phoneNumber, text);
            log.info("USSD Response: {}", response.substring(0, Math.min(50, response.length())));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing USSD request", e);
            return ResponseEntity.ok("END Service temporarily unavailable. Please try again later.");
        }
    }

    /**
     * Test endpoint for local testing
     */
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("USSD Service is running!");
    }
}