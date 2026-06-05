package org.marketplace_lea.common.client.paystack.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionListResponse {
    private PageInfo meta;

    @JsonProperty("transactions")
    private List<TransactionVerifyResponse> transactions;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageInfo {
        private Integer total;
        private Integer skipped;
        private Integer perPage;
        private Integer page;
        private Integer pageCount;
    }
}