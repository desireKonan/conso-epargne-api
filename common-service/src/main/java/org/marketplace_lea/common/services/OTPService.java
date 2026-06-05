package org.marketplace_lea.common.services;

import com.google.common.cache.LoadingCache;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.springframework.stereotype.Service;

@Service
public class OTPService {
    private final LoadingCache<String, Integer> otpCache;
    private final LoadingCache<String, String> deleteAccountCache;

    private static final int MAX_VALUE = 10_000;

    public OTPService(LoadingCache<String, Integer> otpCache, LoadingCache<String, String> deleteAccountCache) {
        this.otpCache = otpCache;
        this.deleteAccountCache = deleteAccountCache;
    }

    public int generateOTP(String key) {
        if (getOtp(key) != null) {
            clearOTP(key);
        }

        int otp = Integer.parseInt(GeneratorUtils.generate5digitsCode());
        otpCache.put(key, otp);
        return otp;
    }

    public void saveAccountId(String key, String accountId) {
        if (getAccountId(key) != null) {
            clearAccountCache(key);
        }

        deleteAccountCache.put(key, accountId);
    }

    public Integer getOtp(String key) {
        return otpCache.getIfPresent(key);
    }

    public String getAccountId(String key) {
        return deleteAccountCache.getIfPresent(key);
    }

    public void clearOTP(String key) {
        otpCache.invalidate(key);
    }

    public void clearAccountCache(String key) {
        deleteAccountCache.invalidate(key);
    }

    public boolean isValidOtp(String key, int value) {
        if (value < MAX_VALUE) {
            return false;
        }

        Integer currentOtp = getOtp(key);
        return currentOtp != null && currentOtp == value;
    }
}