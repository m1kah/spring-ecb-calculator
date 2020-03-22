package com.mika.calculator

import com.neovisionaries.i18n.CurrencyCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.math.BigDecimal

@Controller
class IndexViewController {
    @Autowired
    lateinit var ecbService: EcbService

    @GetMapping("/")
    fun index(model: Model, result: String?, amount: String?, currency: String?): String {
        model.addAttribute("currencies", CurrencyCode.values()
                .map { it.name })
        model.addAttribute("result", result)
        model.addAttribute("inputs", Inputs(amount = amount ?: "", currency = currency ?: ""))
        return "index"
    }

    @PostMapping("/calculate")
    fun calculate(redirectAttributes: RedirectAttributes, @ModelAttribute inputs: Inputs): String {
        redirectAttributes.addAttribute("amount", inputs.amount)
        redirectAttributes.addAttribute("currency", inputs.currency)
        val result = ecbService.calculateDaily(
                amount = BigDecimal(inputs.amount),
                sourceCurrency = inputs.currency ?: "SEK",
                targetCurrency = "EUR")
        redirectAttributes.addAttribute("result", result?.toPlainString())
        return "redirect:/"
    }
}

data class Inputs (
        var amount: String?,
        var currency: String?
)
