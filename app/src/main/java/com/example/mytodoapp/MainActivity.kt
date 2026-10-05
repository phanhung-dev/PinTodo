package com.example.mytodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mytodoapp.notification.TodoNotificationHelper
import com.example.mytodoapp.ui.TodoApp
import com.example.mytodoapp.ui.theme.MyTodoAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        TodoNotificationHelper.createChannel(this)

        enableEdgeToEdge()

        setContent {
            MyTodoAppTheme {
                TodoApp()
            }
        }
    }
}