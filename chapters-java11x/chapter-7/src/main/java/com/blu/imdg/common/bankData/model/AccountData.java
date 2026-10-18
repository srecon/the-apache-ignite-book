package com.blu.imdg.common.bankData.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Account data with mutable fields for runtime updates.
 */
public class AccountData implements AccountCacheData {
    private String account;
    private String personId;
    private String accountType;
    private LocalDate openedAt;

    private BigDecimal dailyLimit;
    private BigDecimal todayOperationSum;
    private LocalDate today;

    public AccountData(String account, String personId, String accountType, LocalDate openedAt, BigDecimal dailyLimit) {
        this.account = account;
        this.personId = personId;
        this.accountType = accountType;
        this.openedAt = openedAt;
        this.dailyLimit = dailyLimit;
        this.todayOperationSum = BigDecimal.ZERO;
        this.today = LocalDate.now();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public LocalDate getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDate openedAt) {
        this.openedAt = openedAt;
    }

    public BigDecimal getTodayOperationSum() {
        return todayOperationSum;
    }

    public void setTodayOperationSum(BigDecimal todayOperationSum) {
        this.todayOperationSum = todayOperationSum;
    }

    public LocalDate getToday() {
        return today;
    }

    public void setToday(LocalDate today) {
        this.today = today;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    @Override
    public String toString() {
        return "AccountData{" +
                "account='" + account + '\'' +
                ", personId='" + personId + '\'' +
                ", accountType='" + accountType + '\'' +
                ", openedAt=" + openedAt +
                ", dailyLimit=" + dailyLimit +
                ", todayOperationSum=" + todayOperationSum +
                ", today=" + today +
                '}';
    }
}
