package com.miaoyongzheng.utils

fun String?.isImageFile(): Boolean {
    if (this.isNullOrBlank()) return false
    val extensions = setOf("jpg", "jpeg", "png", "webp", "svg", "bmp")
    return substringAfterLast('.').lowercase() in extensions
}