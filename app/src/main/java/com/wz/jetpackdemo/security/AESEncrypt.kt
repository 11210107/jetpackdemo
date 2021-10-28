package com.wz.jetpackdemo.security

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.wz.jetpackdemo.extension.hex2Bytes
import com.wz.jetpackdemo.extension.hexString
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESEncrypt {
    @RequiresApi(Build.VERSION_CODES.O)
    fun aes() {
        val message = "Hello World!"
        val key = "123456"
        val data = message.toByteArray(Charsets.UTF_8)
        val salt = SecureRandom.getInstanceStrong().generateSeed(16)
        val encrypted = encrypt(key,salt, data)
        Log.e("AES", "encrytped:${Arrays.toString(encrypted)}")
        Log.e("AES", "encrytped:${encrypted?.hexString()}")
        val decrypted = decrypt(key, salt,encrypted!!)
        Log.e("AES", "decrypted:${Arrays.toString(decrypted)}")
        Log.e("AES", "decrypted:${String(decrypted!!, Charsets.UTF_8)}")
    }

    fun decrypt(key: String,salt:ByteArray, encrypted: ByteArray): ByteArray? {
        val pbeKeySpec = PBEKeySpec(key.toCharArray())
        val sKeyFactory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC")
        val skey = sKeyFactory.generateSecret(pbeKeySpec)
        val pbeps = PBEParameterSpec(salt, 1000)
        val cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC")
        cipher.init(Cipher.DECRYPT_MODE, skey, pbeps)
        return cipher.doFinal(encrypted)
        /*val iv = ByteArray(16)
        val data = ByteArray(encrypted.size - 16)
        System.arraycopy(encrypted, 0, iv, 0, 16)
        System.arraycopy(encrypted, 16, data, 0, data.size)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key, "AES")
        val ivParameterSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)
        return cipher.doFinal(data)*/

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(key: String, salt: ByteArray, data: ByteArray): ByteArray? {
        val keySpec = PBEKeySpec(key.toCharArray())
        val sKeyFractory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC")
        val sKey = sKeyFractory.generateSecret(keySpec)
        val pbeps = PBEParameterSpec(salt, 1000)
        val cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC")
        cipher.init(Cipher.ENCRYPT_MODE, sKey, pbeps)
        return cipher.doFinal(data)
    }
    fun join(bs1: ByteArray, bs2: ByteArray): ByteArray? {
        val r = ByteArray(bs1.size + bs2.size)
        System.arraycopy(bs1, 0, r, 0, bs1.size)
        System.arraycopy(bs2, 0, r, bs1.size, bs2.size)
        return r
    }
    fun hmac() {
        val hkey =
            byteArrayOf(96, 94, -123, -66, -92, -100, -64, -36, 48, 98, 125, 117, 30, 78, -67, 103)
        val secretKey = SecretKeySpec(hkey, "HmacMD5")
        val mac = Mac.getInstance("HmacMD5")
        mac.init(secretKey)
        mac.update("HelloWorld".toByteArray(Charsets.UTF_8))
        val result = mac.doFinal()
        Log.e("HmacMD5", result.hexString())//989EC376558888530CFFB1AE6D7ECE7F
    }

    fun hmacMD5() {
        val keyGen = KeyGenerator.getInstance("HmacMD5")
        val key = keyGen.generateKey()
        val sKey = key.encoded
        val skeyhex = BigInteger(1, sKey).toString(16)
        Log.e("HmacMD5","skeyhex:$skeyhex")
        Log.e("HmacMD5","skey:${Arrays.toString(sKey)}")
        Log.e("HmacMD5","skey:${Arrays.toString(skeyhex.hex2Bytes())}")
        val mac = Mac.getInstance("HmacMD5")
        mac.init(key)
        mac.update("HelloWorld".toByteArray(Charsets.UTF_8))
        val result = mac.doFinal()
        Log.e("HmacMD5", result.hexString())


    }

    fun md5() {
        val md5 = MessageDigest.getInstance("SHA-256")
        md5.update("Hello".toByteArray(Charsets.UTF_8))
        md5.update("World".toByteArray(Charsets.UTF_8))
        val digest = md5.digest()
        Log.e("MD5","digest:${digest.hexString()}")


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun base64() {
        val input = byteArrayOf(0xe4.toByte(), 0xb8.toByte(), 0xad.toByte())
        val b64encoded = Base64.getEncoder().encodeToString(input)
        Log.e("Base64", "encode:$b64encoded")
        val decode = Base64.getDecoder()
            .decode("COCP9OeAgIADMhBlZTAwZjdhOTI3MDc2ZWNiOAOIAaA4kAGGmceJBpgBAaABAKgBALABAMoBD2RzWnVBOEhJUUNxY1l5QQ==")
        Log.e("Base64", "decode:${decode.hexString()}")
    }
}