package com.example.cryptotrade.presentation.base

import android.content.ClipData
import android.content.Context
import com.example.cryptotrade.domain.clipboard.ClipboardManager
import javax.inject.Inject

class AndroidClipboardManager @Inject constructor(
    context: Context,
) : ClipboardManager {

    private val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager

    override fun copy(text: String) {
        val clip = ClipData.newPlainText("clipboard", text)
        clipboard.setPrimaryClip(clip)
    }
}