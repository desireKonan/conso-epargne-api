package org.marketplace_lea.common.common.service.webhook;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public final class NoWalletSignatureValidator {
    private static final String ALGORITHM = "HmacSHA256";

    public static boolean validateSignature(String noWalletSignature, String body, String webhookSecret, String webhookUniqueKey) {
        // Split signature parts
        String[] parts = noWalletSignature.split(",");
        String key = Arrays.stream(parts)
                .filter(part -> part.startsWith("key="))
                .map(part -> part.split("=")[1])
                .findFirst()
                .orElse("null");

        log.info("Clé recuperée : {}",  key);

        String[] signatureParts = Arrays.stream(parts)
                .filter(part -> part.startsWith("signature="))
                .toArray(String[]::new);

        // Get all signatures
        String[] signatures = Arrays.stream(signatureParts)
                .map(s -> s.split("=")[1])
                .toArray(String[]::new);

        log.info("Signatures extraites : {}", Arrays.toString(signatures));  // Afficher toutes les signatures extraites

        try {
            // Encrypt the key using HMAC with WebhookUniqueKey
            String keyEncrypted = hmacSha256(webhookUniqueKey, key);
            log.info("Clé cryptée : {}", keyEncrypted);

            // Create the payload
            String payload = keyEncrypted + body;
            log.info("Payload avec clé et corps : {}", payload);

            // Calculate the signature using the webhook secret
            String calculatedSignature = hmacSha256(webhookSecret, payload);
            log.info("signature calculée : {}", calculatedSignature);  // Afficher le résultat HMAC SHA256

            // Check if the calculated signature is present in the list of signatures
            return Arrays.asList(signatures).contains(calculatedSignature);
        } catch (Exception e) {
            log.error("Erreur dans la signature des comptes: {}", e.getMessage(), e);
            return false;
        }
    }

    // Helper method to compute HMAC SHA256
    private static String hmacSha256(String secret, String message) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(keySpec);
        byte[] result = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(result);
    }

    // Helper method to convert bytes to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
