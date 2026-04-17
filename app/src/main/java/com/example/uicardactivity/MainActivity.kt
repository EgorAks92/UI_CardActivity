package com.example.uicardactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.uicardactivity.payment.PaymentScreen
import com.example.uicardactivity.ui.theme.UICardActivityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UICardActivityTheme(darkTheme = true) {
                PaymentScreen()
            }
        }
    }
}
