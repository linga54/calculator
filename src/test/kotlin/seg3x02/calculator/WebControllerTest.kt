package seg3x02.calculator

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
class WebControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private fun calculate(first: String, second: String, operation: String) =
        mockMvc.perform(
            MockMvcRequestBuilders.get("/calculate")
                .param("first", first)
                .param("second", second)
                .param("operation", operation)
        )

    @Test
    fun request_to_home() {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("home"))
    }

    @Test
    fun addition() {
        calculate("6", "3", "ADD")
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.model().attribute("result", "9"))
            .andExpect(MockMvcResultMatchers.view().name("home"))
    }

    @Test
    fun subtraction() {
        calculate("6", "3", "SUB")
            .andExpect(MockMvcResultMatchers.model().attribute("result", "3"))
    }

    @Test
    fun multiplication() {
        calculate("6", "3", "MUL")
            .andExpect(MockMvcResultMatchers.model().attribute("result", "18"))
    }

    @Test
    fun division() {
        calculate("1", "4", "DIV")
            .andExpect(MockMvcResultMatchers.model().attribute("result", "0.25"))
    }

    @Test
    fun decimal_addition() {
        calculate("0.1", "0.2", "ADD")
            .andExpect(MockMvcResultMatchers.model().attribute("result", "0.3"))
    }

    @Test
    fun division_by_zero() {
        calculate("5", "0", "DIV")
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.model().attribute("error", "DivisionByZeroError"))
            .andExpect(MockMvcResultMatchers.model().attribute("result", ""))
            .andExpect(MockMvcResultMatchers.view().name("home"))
    }

    @Test
    fun invalid_first_number() {
        calculate("abc", "3", "ADD")
            .andExpect(MockMvcResultMatchers.model().attribute("error", "FirstNumberFormatError"))
    }

    @Test
    fun invalid_second_number() {
        calculate("3", "abc", "ADD")
            .andExpect(MockMvcResultMatchers.model().attribute("error", "SecondNumberFormatError"))
    }

    @Test
    fun blank_number() {
        calculate("", "3", "ADD")
            .andExpect(MockMvcResultMatchers.model().attribute("error", "FirstNumberFormatError"))
    }

    @Test
    fun invalid_operation() {
        calculate("6", "3", "POW")
            .andExpect(MockMvcResultMatchers.model().attribute("error", "OperationFormatError"))
    }
}

//I had made an error with my file, my home page was not displaying the result of the calculation, so I had to fix that. I also added a test for decimal addition, which was not in the original tests.
//And my page was showing without any style but with no apparent reason, so I had to fix that as well. 