package uz.gita.mynotesapplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat

@RequiresApi(Build.VERSION_CODES.N)
fun html2text(html: String?): String {
    return android.text.Html.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}