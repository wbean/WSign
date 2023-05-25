package me.wbean.component

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.widget.EditText
import me.wbean.service.FileService
import me.wbean.service.RSAService

class NewKeyDialog {
    fun show(fileService:FileService, context: Context){
        var editText = EditText(context)
        var builder = AlertDialog.Builder(context)
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