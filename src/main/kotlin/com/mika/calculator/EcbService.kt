package com.mika.calculator

import EcbHistory
import feign.RequestLine
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.RoundingMode

class EcbService(
        val ecb: Ecb
) {
    private val rates: MutableMap<String, BigDecimal> = mutableMapOf()

    fun calculateDaily(amount: BigDecimal, sourceCurrency: String, targetCurrency: String): BigDecimal? {
        log.debug("Calculating daily exchange $amount $sourceCurrency $targetCurrency")
        if (sourceCurrency == targetCurrency) {
            throw IllegalArgumentException("Source and target currency cannot be same")
        }
        requireRates()
        if (sourceCurrency == "EUR") {
            return rates[targetCurrency]?.also { rate ->
                rate.multiply(amount).setScale(2, RoundingMode.HALF_UP)
            }
        } else {
            return rates[sourceCurrency]?.also { rate ->
                if (rate.signum() == 0) return null
                return amount.divide(rate, 2, RoundingMode.HALF_UP)
            }
        }
    }

    @Synchronized
    private fun requireRates() {
        if (rates.isNotEmpty()) {
            return
        }
        log.info("Loading rates")
        val daily = ecb.getDaily()
        log.debug("Daily exchange rates: $daily")
        val dailyRates = daily.Cube?.Cube?.firstOrNull()
        dailyRates?.Cube?.forEach { rate ->
            val currency = rate.currency ?: return
            val fxRate = rate.rate ?: return
            rates[currency] = BigDecimal(fxRate)
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(EcbService::class.java)
    }
}

interface Ecb {
    @RequestLine("GET /stats/eurofxref/eurofxref-hist.xml")
    fun getHistory(): EcbHistory

    @RequestLine("GET /stats/eurofxref/eurofxref-daily.xml")
    fun getDaily(): EcbHistory
}
