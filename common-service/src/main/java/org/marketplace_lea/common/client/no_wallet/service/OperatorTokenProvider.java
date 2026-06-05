// OperatorTokenProvider.java
package org.marketplace_lea.common.client.no_wallet.service;

import java.util.List;

public interface OperatorTokenProvider {
    String getTokenForOperator(String operator);
    boolean supportsOperator(String operator);
    String extractOperator(List<String> operatorsCode);
}