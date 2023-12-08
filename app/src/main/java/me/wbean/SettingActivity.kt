package me.wbean

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.ComponentActivity.*
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.wbean.component.NewKeyDialog
import me.wbean.service.FileService
import me.wbean.service.RSAService
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
                    val fileService = FileService(this.filesDir)
                    SettingGreeting(
                        fileService.selfName,
                        fileService.selfPublicKey,
                        fileService.publicKeyList,
                        copyMyPublicKey = {
                            val clipboard: ClipboardManager =
                                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val result = "${fileService.selfName}|${fileService.selfPublicKey}"
                            val clip = ClipData.newPlainText("myKey", result)
                            clipboard.setPrimaryClip(clip)
                            result
                        },
                        genMyNewKey = {
                            val generateKey = RSAService.generateKey()
                            val pubKeyStr = RSAService.encodePublicKey(generateKey.public)
                            val priKeyStr = RSAService.encodePrivateKey(generateKey.private)
                            fileService.writeSelfKeyPair(it, pubKeyStr, priKeyStr)
                            fileService.selfPublicKey
                        },
                        addNewPublicKay = {
                            val (name, key) = it.split("|")
                            fileService.writePublicKey(name, key)
                            fileService.publicKeyList
                        },
                        deletePublicKey = {
                            fileService.deletePublicKey(it)
                            fileService.publicKeyList
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewKeyDialogV2(showDialog: Boolean,
                   setShowDialog: (Boolean) -> Unit,
                   confirmNewName: (String) -> Unit,
): String{

    var nameTextField by remember {
        mutableStateOf(TextFieldValue(""))
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { setShowDialog(false) },
            properties = DialogProperties(),
        ) {
            Column {
                Row {
                    TextField(
                        label = { Text(text = "输入新名称") },
                        value = nameTextField, onValueChange = { nameTextField = it }
                    )
                }
                Row {
                    Button(onClick = {
                        if (nameTextField.text == "") {
                            println("empty name")
                            return@Button
                        }
                        confirmNewName(nameTextField.text)
                        setShowDialog(false)
                    }) {
                        Text(text = "生成")
                    }
                    Button(onClick = { setShowDialog(false) }) {
                        Text(text = "取消")
                    }
                }
            }
        }
    }
    return nameTextField.text
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingGreeting(name:String,
                    myPublicKey: String,
                    publicKeyList: List<String>,
                    copyMyPublicKey: () -> String,
                    genMyNewKey: (String) -> String,
                    addNewPublicKay: (String) -> List<String>,
                    deletePublicKey: (String) -> List<String>,
                    modifier: Modifier = Modifier) {
    var myName by remember {
        mutableStateOf(name)
    }
    var myPublicKeyText by remember {
        mutableStateOf(myPublicKey)
    }
    var newPublicKeyTextField by remember {
        mutableStateOf(TextFieldValue())
    }
    var addNewPublicKeyResult by remember {
        mutableStateOf("")
    }
    var publicKeyList by remember {
        mutableStateOf(publicKeyList)
    }

    var showDialog by remember {
        mutableStateOf(true)
    }

    var setShowDialog: (Boolean) -> Unit = {
        showDialog = it
    }

    var confirmNewName: (String) -> Unit = {
        myName = it
        val publicKey = genMyNewKey(it)
        myPublicKeyText = publicKey
    }

    NewKeyDialogV2(showDialog = showDialog,
        setShowDialog = setShowDialog,
        confirmNewName = confirmNewName,
    )

    Column(modifier = modifier) {
        Text(text="名称:$myName")
        Text(text = "公钥:$myPublicKeyText")
        Row(modifier = modifier) {
            Button(onClick = {copyMyPublicKey()}) {
                Text(text = "复制我的公钥")
            }
            Button(onClick = {
                setShowDialog(true)
            }) {
                Text(text = "重新生成我的公私钥")
            }
        }
        TextField(value = newPublicKeyTextField,
            onValueChange = {newPublicKeyTextField = it},
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            label = { Text(text = "新公钥粘贴到这里")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            trailingIcon = {
                Button(onClick = {
                    val result = addNewPublicKay(newPublicKeyTextField.text)
                    addNewPublicKeyResult = "添加新公钥成功!"
                    publicKeyList = result
                }) {
                    Text(text = "添加他人公钥")
                }
            }
        )

        Text(text = "已添加公钥列表")

        for (publicKey in publicKeyList) {
            Row(modifier = modifier) {
                Text(text = publicKey)
                Button(onClick = { deletePublicKey(publicKey) }) {
                    Text(text = "删除")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingGreetingPreview() {
    WSignTheme {
        SettingGreeting("wbean", "1234567890", ArrayList(),
            copyMyPublicKey = { "1234567890" }, genMyNewKey = {""}, addNewPublicKay = {ArrayList()}, deletePublicKey = {ArrayList()} )
    }
}