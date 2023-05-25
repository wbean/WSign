package me.wbean

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import me.wbean.service.FileService
import me.wbean.service.RSAService
import me.wbean.ui.theme.WSignTheme


class SignActivity : ComponentActivity() {
    var message = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WSignTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var fileService = FileService(this.filesDir)
                    SignGreeting(
                        onClickButton = {
                            message = it
                            val sign = RSAService.sign(message, fileService.selfPrivateKey)
                            println("sign: $sign")
                            val clipboard: ClipboardManager =
                                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            var result = "$message|$sign"
                            val clip = ClipData.newPlainText("signed", result)
                            clipboard.setPrimaryClip(clip)
                            result
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignGreeting(onClickButton: (message: String) -> String,
                 modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf(TextFieldValue()) }
    var displayResult by remember {
        mutableStateOf("")
    }
    Column(modifier = modifier) {
        Row {
            TextField(
                label = { Text(text = "请输入需要签名的信息") },
                value = text, onValueChange = { text = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
                trailingIcon = {
                    Button(onClick = {
                        var result = onClickButton(text.text)
                        displayResult = "签名结果:\n$result\n\n 已复制到剪贴板,请直接在聊天软件粘贴并发送"
                    }) {
                        Text(text = "签名")
                    }
                }
            )
        }
        Row {
            Text(text = displayResult)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignGreetingPreview() {
    WSignTheme {
        SignGreeting(onClickButton = {""})
    }
}