package me.wbean

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import me.wbean.ui.theme.WSignTheme

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WSignTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingGreeting()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingGreeting(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "my public key")
        Row(modifier = modifier) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "生成我的公私钥")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "复制我的公钥")
            }
        }
        TextField(value = TextFieldValue("请输入公钥名称"), onValueChange = {})
        TextField(value = TextFieldValue("请输入公钥"), onValueChange = {})
        Button(onClick = { /*TODO*/ }) {
            Text(text = "添加公钥")
        }
        Text(text = "已添加公钥列表")
        Row(modifier = modifier) {
            Text(text = "公钥1")
            Button(onClick = { /*TODO*/ }) {
                Text(text = "删除")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingGreetingPreview() {
    WSignTheme {
        SettingGreeting()
    }
}