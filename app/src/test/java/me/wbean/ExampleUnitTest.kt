package me.wbean

import me.wbean.service.FileService
import me.wbean.service.RSAService
import org.junit.Test

import org.junit.Assert.*
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var key = RSAService.generateKey()
        println(key.public)
        println(key.private)

        var sign = RSAService.sign("hello", RSAService.encodePrivateKey(key.private))
        println(sign)

        var verify = RSAService.verify("hello", sign, RSAService.encodePublicKey(key.public))
        println(verify)
    }

    @Test
    fun fileServiceTest() {
        var fileService = FileService(File("/tmp") )
        var key = RSAService.generateKey()
        var encodePublicKey = RSAService.encodePublicKey(key.public)
        var encodePrivateKey = RSAService.encodePrivateKey(key.private)
        println(encodePublicKey)
        println(encodePrivateKey)
        fileService.writeSelfKeyPair("w", encodePublicKey, encodePrivateKey)
        println(fileService.selfPublicKey)
        println(fileService.selfPrivateKey)

        var sign = RSAService.sign("hello", fileService.selfPrivateKey)
        println(sign)

        var verify = RSAService.verify("hello", sign, fileService.selfPublicKey)
        println(verify)
    }
}