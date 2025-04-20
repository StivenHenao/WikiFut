package com.wikifut.app.presentation.Ligas

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.wikifut.app.utils.Constans

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun StandingsWidget(
    leagueId: Int,
    season: Int,
    modifier: Modifier = Modifier
) {
    val htmlContent = """
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        </head>
        <body style="margin:0;padding:0;">
            <div id="wg-api-football-standings"
                data-host="v3.football.api-sports.io"
                data-key="${Constans.X_RAPIDAPI_KEY}"
                data-league="$leagueId"
                data-season="$season"
                data-show-errors="false"
                data-show-logos="true"
                class="wg_loader">
            </div>
            <script type="module" src="https://widgets.api-sports.io/2.0.3/widgets.js"></script>
        </body>
        </html>
    """.trimIndent()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                webViewClient = WebViewClient()
                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        },
        modifier = modifier
    )
}
