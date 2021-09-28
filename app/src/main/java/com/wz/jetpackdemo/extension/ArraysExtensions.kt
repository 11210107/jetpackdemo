package com.wz.jetpackdemo.extension



fun ByteArray.hexString(): String {
    val DIGITS = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    )
    val sb = StringBuffer(this.size * 2)
    for (i in this.indices) {
        sb.append(DIGITS[this[i].toInt() shr 4 and 0x0F])
        sb.append(DIGITS[this[i].toInt() and 0x0F])
    }
    return sb.toString()
}

fun String.hex2Bytes(): ByteArray {
    val out = ByteArray(this.length / 2)
    out.forEachIndexed { i, byte ->
        val index = i*2
        val parseInt = Integer.parseInt(substring(index , index + 2), 16)
        out[i] = parseInt.toByte()
    }
    return out
}