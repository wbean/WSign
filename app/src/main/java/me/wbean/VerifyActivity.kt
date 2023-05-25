package me.wbean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import me.wbean.service.FileService
import me.wbean.service.RSAService
import me.wbean.ui.theme.WSignTheme

class VerifyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var fileService = FileService(this.filesDir)
        setContent {
            WSignTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VerifyGreeting(onClickButton = {
                        val (message, sign) = it.split("|")
                        val result = RSAService.verify(message, sign, fileService.selfPublicKey)
                        println("result: $result")
                        result
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyGreeting(onClickButton: (text: String) -> Boolean, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    var displayResult by remember { mutableStateOf("结果：") }

    Column(modifier = modifier) {
        TextField(
            label = { Text(text = "请输入需要验证的信息") },
            value = text,
            onValueChange = {text = it},
            modifier = Modifier.fillMaxSize(),
            trailingIcon = {
                Button(onClick = {
                    var result = onClickButton(text.text)
                    displayResult = if (result) "验证成功" else "验证失败"
                }) {
                    Text(text = "验证签名")
                }
            }
        )
        Text(text = displayResult)
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyGreetingPreview() {
    WSignTheme {
        VerifyGreeting(onClickButton = {true})
    }
}