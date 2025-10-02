package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class CalculatorViewModel : ViewModel() {

    private val _equationText = MutableLiveData("")
    val equationText : LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText : LiveData<String> = _resultText

    private fun formatResult(result: Double): String {
        var finalResult = if (result.isNaN() || result.isInfinite()) "Error" else result.toString()
        if (finalResult.endsWith(".0")) {
            finalResult = finalResult.substring(0, finalResult.length - 2)
        }
        return finalResult
    }

    fun calculateResult(equation : String) : String {
        var processedEquation = equation
            .replace(Regex("([0-9.]+)\\s*\\^\\s*([0-9.]+)"), "Math.pow($1, $2)")
            .replace("sin(", "Math.sin(")
            .replace("cos(", "Math.cos(")
            .replace("tan(", "Math.tan(")
            .replace("asin(", "Math.asin(")
            .replace("acos(", "Math.acos(")
            .replace("atan(", "Math.atan(")
            .replace("sqrt(", "Math.sqrt(")
            .replace(Regex("([0-9.]+)!"), "fact($1)")


        val context : Context = Context.enter()
        context.optimizationLevel = -1
        val scriptable : Scriptable = context.initStandardObjects()

        val factFunction = """
            function fact(n) {
                if (n < 0 || n % 1 !== 0) return 'Error'; 
                if (n > 20) return 'Error'; 
                var res = 1;
                for (var i = 2; i <= n; i++) res = res * i;
                return res;
            }
        """.trimIndent()
        context.evaluateString(scriptable, factFunction, "Javascript", 1, null)

        var finalResult = try {
            context.evaluateString(scriptable, processedEquation, "Javascript", 1, null).toString()
        } catch (e: Exception) {
            "Error"
        }

        val resultDouble = finalResult.toDoubleOrNull()
        return if (resultDouble != null) formatResult(resultDouble) else finalResult
    }

    fun onButtonClick(btn: String) {
        _equationText.value?.let { currentEquation ->
            var newValue = currentEquation
            var isCalculated = false

            when (btn) {
                "AC" -> {
                    newValue = ""
                    _resultText.value = "0"
                }
                "C" -> {
                    newValue = currentEquation.dropLast(1)
                }
                "=" -> {
                    isCalculated = true
                }
                "1/x" -> {
                    val lastResult = _resultText.value?.toDoubleOrNull()
                    if (lastResult != null && lastResult != 0.0) {
                        newValue = "1/(${lastResult})"
                        _resultText.value = formatResult(1.0 / lastResult)
                    } else {
                        _resultText.value = "Error"
                        newValue = currentEquation
                    }
                    _equationText.value = newValue
                    return
                }
                "x^y" -> newValue += "^"
                "sqrt" -> newValue += "sqrt("
                "sin" -> newValue += "sin("
                "cos" -> newValue += "cos("
                "tan" -> newValue += "tan("
                "asin" -> newValue += "asin("
                "acos" -> newValue += "acos("
                "atan" -> newValue += "atan("
                "x!" -> newValue += "!"

                else -> {
                    newValue += btn
                }
            }

            _equationText.value = newValue

            if (newValue.isNotEmpty() && !isCalculated) {
                val result = calculateResult(newValue)
                if (result != "Error") {
                    _resultText.value = result
                }
            } else if (isCalculated) {
                val finalResult = calculateResult(newValue)
                _resultText.value = finalResult
                _equationText.value = finalResult
            }
        }
    }
}