package seg3x02.calculator

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal
import java.math.RoundingMode

@Controller
class WebController {

    @ModelAttribute
    fun addAttributes(model: Model) {
        model.addAttribute("error", "")
        model.addAttribute("first", "")
        model.addAttribute("second", "")
        model.addAttribute("result", "")
    }

    @RequestMapping("/")
    fun home(): String {
        return "home"
    }

    @GetMapping("/calculate")
    fun doCalculate(
        @RequestParam(value = "first", required = false) first: String?,
        @RequestParam(value = "second", required = false) second: String?,
        @RequestParam(value = "operation", required = false) operation: String?,
        model: Model
    ): String {
        val firstText = first ?: ""
        val secondText = second ?: ""
        model.addAttribute("first", firstText)
        model.addAttribute("second", secondText)

        val firstVal = firstText.trim().toDoubleOrNull()
        if (firstVal == null || !firstVal.isFinite()) {
            return fail(model, "FirstNumberFormatError")
        }
        val secondVal = secondText.trim().toDoubleOrNull()
        if (secondVal == null || !secondVal.isFinite()) {
            return fail(model, "SecondNumberFormatError")
        }

        val result = when (operation) {
            "ADD" -> firstVal + secondVal
            "SUB" -> firstVal - secondVal
            "MUL" -> firstVal * secondVal
            "DIV" -> {
                if (secondVal == 0.0) return fail(model, "DivisionByZeroError")
                firstVal / secondVal
            }
            else -> return fail(model, "OperationFormatError")
        }
        if (!result.isFinite()) {
            return fail(model, "ResultTooLargeError")
        }

        model.addAttribute("result", formatResult(result))
        return "home"
    }

    private fun fail(model: Model, errorCode: String): String {
        model.addAttribute("error", errorCode)
        return "home"
    }

    private fun formatResult(value: Double): String {
        return BigDecimal(value)
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
    }
}