package me.wbean

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.contextaware.ContextAware
import androidx.activity.contextaware.ContextAwareHelper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import me.wbean.service.FileService
import me.wbean.service.RSAService
import me.wbean.ui.theme.WSignTheme

class MainActivity : ComponentActivity() {

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
                    Greeting(onClickButton = {
                        var intentSign = Intent(this@MainActivity, SignActivity::class.java)
                        var intentVerify = Intent(this@MainActivity, VerifyActivity::class.java)
                        var intentSetting = Intent(this@MainActivity, SettingActivity::class.java)
                        when (it) {
                            "sign" -> {
                                startActivity(intentSign)
                            }
                            "verify" -> {
                                startActivity(intentVerify)
                            }
                            "setting" -> {
                                startActivity(intentSetting)
                            }
                        }
                    })
                }
            }
        }
        if (!fileService.isSelfKeyExist) {
            var editText = EditText(this)
            var builder = Builder(this)
            builder.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return@OnKeyListener true
                }
                false
            })
            builder.setTitle("请输入你的名字")
            builder.setView(editText)
            builder.setCancelable(false);
            builder.setPositiveButton("确定") { dialog, which ->
                var name = editText.text.toString()
                var generateKey = RSAService.generateKey()
                fileService.writeSelfKeyPair(name, RSAService.encodePublicKey(generateKey.public), RSAService.encodePrivateKey(generateKey.private))
            }
            builder.setNegativeButton("取消") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
    }
}

@Composable
fun Greeting(onClickButton: (type: String) -> Unit, modifier: Modifier = Modifier, ) {
    Column(modifier = modifier) {
        Button(onClick = {
            onClickButton("sign")
        }) {
            Text(text = "签名")
        }
        Button(onClick = {
            onClickButton("verify")
        }) {
            Text(text = "验签")
        }
        Button(onClick = {
            onClickButton("setting")
        }) {
            Text(text = "设置")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WSignTheme {
        Greeting(onClickButton = {
        })
    }
}