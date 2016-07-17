package com.toska

import java.security.MessageDigest
import java.security.SecureRandom


class ProofOfWork {

    private val AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    private val rnd = SecureRandom()

    private val CHARS = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    internal fun Byte.toHexString(): String {
        val i = this.toInt()
        val char2 = CHARS[i and 0x0f]
        val char1 = CHARS[i shr 4 and 0x0f]
        return "$char1$char2"
    }

    internal fun ByteArray.toHexString(): String {
        val builder = StringBuilder()
        for (b in this) {
            builder.append(b.toHexString())
        }
        return builder.toString()
    }

    fun randomString(len: Int): String {
        var sb = StringBuilder(len);
        for (item in 0..len) {
            sb.append(AB[rnd.nextInt(AB.length)])
        }
        return sb.toString()
    }

    fun getSHA256Hash(str: String): String {
        var md = MessageDigest.getInstance("SHA-256");
        md.update(str.toByteArray(charset("UTF-8")))
        var digest = md.digest()
        return digest.toHexString()
    }
}

fun main(args: Array<String>) {

    val ANSWER_LENGTH = 26
    val START_WITH = "00000"
    val EXAMPLE_CHALLENGE = "Jas7LBFNKLWgMArTuUFUWP4HQrM"

    val proofOfWork = ProofOfWork()
    var found: Boolean = false
    var startTime = System.currentTimeMillis()

    while (!found) {
        val answer = proofOfWork.randomString(ANSWER_LENGTH)
        var attempt = EXAMPLE_CHALLENGE + answer
        var hash = proofOfWork.getSHA256Hash(attempt)
        found = hash.startsWith(START_WITH)
        if (found) {
            var endTime = System.currentTimeMillis()
            println("Answer (hash that starts with $START_WITH): $hash")
            println("Right answer found in " + (endTime - startTime) + " ms")
        }
    }

}
