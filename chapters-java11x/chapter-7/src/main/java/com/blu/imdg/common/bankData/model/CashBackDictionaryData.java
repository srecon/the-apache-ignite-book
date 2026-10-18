package com.blu.imdg.common.bankData.model;

import java.math.BigDecimal;

/**
 * Immutable cash back dictionary data record.
 */
public record CashBackDictionaryData(
        BigDecimal cashBackPercent,
        String savingsDescription
) {}
