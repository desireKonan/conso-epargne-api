package org.marketplace_lea.common.client.wave.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.ALGORITHM_WEBHOOK;

@Slf4j
public final class WaveWebhookSignatureVerifier {
    private static final byte[] HEX_ARRAY = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);

    private WaveWebhookSignatureVerifier() {}

    public static boolean verifySignature(String waveSignature, String body, String webhookSecret) {
        final String[] waveSignatureParts = waveSignature.split(",");
        String timestamp = "";
        List<String> signatures = new ArrayList<>();

        for (String elem : waveSignatureParts) {
            String[] keyval = elem.split("=");
            String key = keyval[0];
            String val = keyval[1];
            if (key.equals("t")) {
                timestamp = val;
            } else {
                signatures.add(val);
            }
        }

        byte[] hash = initHashMacSecretKey(webhookSecret, (timestamp + body));
        String calculatedSignature = bytesToHex(hash);
        return signatures.contains(calculatedSignature);
    }


    private static byte[] initHashMacSecretKey(String webhookSecret, String data) {
        try {
            Mac HmacSHA256 = Mac.getInstance(ALGORITHM_WEBHOOK);
            SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), ALGORITHM_WEBHOOK);
            HmacSHA256.init(secretKeySpec);
            return HmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Error while initializing secret key: {}", ex.getMessage(), ex);
            return new byte[0];
        }
    }

    private static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}
