package com.example.triviaswamp.Views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.triviaswamp.ui.theme.TriviaSwampTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriviaSwampTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 画面のUIを呼び出し
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * 画面のUIを定義するコンポーザブル関数
 */
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login Screen",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * LoginScreenのプレビュー用コンポーザブル関数
 */
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    TriviaSwampTheme {
        LoginScreen()
    }
}