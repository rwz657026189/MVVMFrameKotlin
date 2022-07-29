package com.landon.debug

import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    println(executor("ipconfig"))
}

fun executor(command: String): String? {
    var inBuffer: BufferedReader? = null
    try {
        val process = Runtime.getRuntime().exec(command)
        val inReader = InputStreamReader(process.inputStream)
        inBuffer = BufferedReader(inReader)
        val sb = StringBuffer()
        var line: String? = null
        while (line.apply {
                line = inBuffer.readLine()
            } != null) {
            sb.append(line)
        }
        return sb.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        inBuffer?.close()
    }
}