package com.example.calculator

import android.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.livedata.observeAsState


val buttonList = listOf(
    "sin","cos", "tan","asin","acos","atan",
    "1/x","7","8","9","(",")",
    "x^y","4","5","6","*","/",
    "sqrt","1","2","3","+","-",
    "C","AC","0",".","!","=",
)

@Composable
fun Calculator(modifier: Modifier = Modifier, viewModel: CalculatorViewModel) {

    val equationText = viewModel.equationText.observeAsState()
    val resultText = viewModel.resultText.observeAsState()

    Box(modifier = modifier) {
        Column (
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ){
            Text(
                text = equationText.value?:"",
                style = TextStyle(
                    fontSize = 35.sp,
                    textAlign = TextAlign.End
                ),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = resultText.value?:"",
                style = TextStyle(
                    fontSize = 65.sp,
                    textAlign = TextAlign.End
                ),
                maxLines = 2,

            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(6     ),
            ) {
                items(buttonList){
                    CalculatorButtom(btn = it, onClick = {
                        viewModel.onButtonClick(it)
                    })
                }
            }
        }
    }
}

@Composable
fun CalculatorButtom(btn : String,onClick : () -> Unit) {
    Box(modifier = Modifier.padding(1.dp)){
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(100.dp),
            shape = RectangleShape,
            contentColor = Color.White,
            containerColor = getColor(btn)
        ) {
            Text(text = btn, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        }
    }
}

fun getColor(btn: String) : Color{
    if(btn == "AC" || btn == "C")
        return Color(0xFF980000)
    if(btn == "(" || btn == ")")
        return Color.DarkGray
    if(btn == "+" || btn == "-" || btn == "*" || btn == "/" || btn == "=" || btn == "sqrt" || btn ==
        "x^y" || btn == "x/y" || btn == "1/x" )
        return Color(0xFF009688)
    if(btn == "sin" || btn == "cos" || btn == "tan" || btn == "asin" || btn == "acos" || btn == "atan")
        return Color(0xFF4C6E64)
    return Color(0xFF004D40)


    // package com.example.calculator
    //
    //import android.util.Log
    //import androidx.lifecycle.LiveData
    //import androidx.lifecycle.MutableLiveData
    //import androidx.lifecycle.ViewModel
    //import org.mozilla.javascript.Context
    //import org.mozilla.javascript.Scriptable
    //
    //class CalculatorViewModel : ViewModel() {
    //
    //    private val _equationText = MutableLiveData("")
    //    val equationText : LiveData<String> = _equationText
    //
    //
    //    private val _resultText = MutableLiveData("0")
    //    val resultText : LiveData<String> = _resultText
    //
    //
    //    private fun formatResult(result: Double): String {
    //        var finalResult = if (result.isNaN() || result.isInfinite()) "Error" else result.toString()
    //        if (finalResult.endsWith(".0")) {
    //            finalResult = finalResult.substring(0, finalResult.length - 2)
    //        }
    //        return finalResult
    //    }
    //
    //
    //    fun calculateResult(equation : String) : String {
    //        var processedEquation = equation
    //
    //            .replace(Regex("([0-9.]+)\\s*\\^\\s*([0-9.]+)"), "Math.pow($1, $2)")
    //            .replace("ln(", "Math.log(")
    //            .replace("log(", "Math.log10(")
    //            .replace("sin(", "Math.sin(")
    //            .replace("cos(", "Math.cos(")
    //            .replace("tan(", "Math.tan(")
    //            .replace("asin(", "Math.asin(")
    //            .replace("acos(", "Math.acos(")
    //            .replace("atan(", "Math.atan(")
    //            .replace("sqrt(", "Math.sqrt(")
    //            .replace(Regex("([0-9.]+)!"), "fact($1)")
    //
    //
    //        val context : Context = Context.enter()
    //        context.optimizationLevel = -1
    //        val scriptable : Scriptable = context.initStandardObjects()
    //
    //        val factFunction = """
    //            function fact(n) {
    //                if (n < 0 || n % 1 !== 0) return 'Error'; // Harus bilangan bulat non-negatif
    //                if (n > 20) return 'Error'; // Batas untuk menghindari overflow Long
    //                var res = 1;
    //                for (var i = 2; i <= n; i++) res = res * i;
    //                return res;
    //            }
    //        """.trimIndent()
    //        context.evaluateString(scriptable, factFunction, "Javascript", 1, null)
    //
    //        // Evaluasi persamaan utama
    //        var finalResult = try {
    //            context.evaluateString(scriptable, processedEquation, "Javascript", 1, null).toString()
    //        } catch (e: Exception) {
    //            "Error"
    //        }
    //
    //        val resultDouble = finalResult.toDoubleOrNull()
    //        return if (resultDouble != null) formatResult(resultDouble) else finalResult
    //    }
    //
    //
    //    fun onButtonClick(btn: String) {
    //        _equationText.value?.let { currentEquation ->
    //            var newValue = currentEquation
    //            var isCalculated = false
    //
    //            when (btn) {
    //                "AC" -> {
    //                    newValue = ""
    //                    _resultText.value = "0"
    //                }
    //                "C" -> {
    //                    newValue = currentEquation.dropLast(1)
    //                }
    //                "=" -> {
    //                    isCalculated = true
    //                }
    //                "1/x" -> {
    //                    val lastResult = _resultText.value?.toDoubleOrNull()
    //                    if (lastResult != null && lastResult != 0.0) {
    //                        newValue = "1/(${lastResult})"
    //                        _resultText.value = formatResult(1.0 / lastResult)
    //                    } else {
    //                        _resultText.value = "Error"
    //                    }
    //                    _equationText.value = newValue
    //                    return
    //                }
    //                "x^y" -> newValue += "^"
    //                "sqrt" -> newValue += "sqrt("
    //                "sin" -> newValue += "sin("
    //                "cos" -> newValue += "cos("
    //                "tan" -> newValue += "tan("
    //                "asin" -> newValue += "asin("
    //                "acos" -> newValue += "acos("
    //                "atan" -> newValue += "atan("
    //                "log" -> newValue += "log("
    //                "ln" -> newValue += "ln("
    //                "x!" -> newValue += "!"
    //
    //                else -> {
    //                    newValue += btn
    //                }
    //            }
    //
    //            _equationText.value = newValue
    //
    //            if (newValue.isNotEmpty() && !isCalculated) {
    //                try {
    //                    _resultText.value = calculateResult(newValue)
    //                } catch (_: Exception) {
    //                }
    //            } else if (isCalculated) {
    //                try {
    //                    val finalResult = calculateResult(newValue)
    //                    _resultText.value = finalResult
    //                    _equationText.value = finalResult
    //                } catch (e: Exception) {
    //                    _resultText.value = "Error"
    //                }
    //            }
    //        }
    //    }
    //}
}