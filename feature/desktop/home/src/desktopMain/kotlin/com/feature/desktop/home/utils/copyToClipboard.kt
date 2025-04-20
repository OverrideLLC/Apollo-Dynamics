package com.feature.desktop.home.utils

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

fun copyToClipboard(text: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val stringSelection = StringSelection(text)
    clipboard.setContents(stringSelection, null)
}