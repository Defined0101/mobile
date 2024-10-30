package com.defined.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.defined.mobile.ui.theme.MobileTheme
import com.defined.mobile.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileTheme(darkTheme = isSystemInDarkTheme()) {
                MainScreen()
            }
        }
    }
}
