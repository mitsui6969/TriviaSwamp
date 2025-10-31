package com.example.triviaswamp.Views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triviaswamp.R
import com.example.triviaswamp.ui.theme.TriviaSwampTheme

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriviaSwampTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SettingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 80.dp, start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fzwwv_magaaof9d),
            contentDescription = "プロフィール画像",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape)

        )
        Text(
            text = "名前",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                .padding(vertical = 8.dp)
        ) {
            SettingItem(icon = Icons.Default.Person, text = "プロフィール")
            Divider(color = Color.LightGray, thickness = 1.dp)
            SettingItem(icon = Icons.Default.Bookmark, text = "お気に入り")
            Divider(color = Color.LightGray, thickness = 1.dp)
            SettingItem(icon = Icons.Default.Key, text = "パスワード変更")
            Divider(color = Color.LightGray, thickness = 1.dp)
            SettingItem(icon = Icons.Default.Block, text = "ブロック")
            Divider(color = Color.LightGray, thickness = 1.dp)
            SettingItem(icon = Icons.Default.Logout, text = "ログアウト")
        }
    }
}

@Composable
fun SettingItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: 項目を押したときの処理 */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    TriviaSwampTheme {
        SettingScreen()
    }
}

