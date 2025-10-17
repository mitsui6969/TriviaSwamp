package com.example.triviaswamp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.triviaswamp.Views.HomeScreen
import com.example.triviaswamp.Views.SettingScreen
import com.example.triviaswamp.Views.UserScreen
import com.example.triviaswamp.Views.LoginScreen
import com.example.triviaswamp.ui.theme.TriviaSwampTheme

// 各タブ画面の情報を定義するクラス
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "ホーム", Icons.Default.Home)
    object User : Screen("user", "ユーザー", Icons.Default.List)
    object Settings : Screen("settings", "設定", Icons.Default.Settings)
    object Login : Screen("login", "ログイン", Icons.Default.List)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviaSwampTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.User,
        Screen.Settings,
        Screen.Login
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // スタックのトップに戻るまでの間、ポップアップする
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // 同じ画面を何度もスタックに積まないようにする
                                launchSingleTop = true
                                // 再選択時に状態を復元する
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // NavHostで画面の表示領域を定義
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route, // 最初に表示する画面
            modifier = Modifier.padding(innerPadding)
        ) {
            // 各ルートに対応する画面(Composable)を定義
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.User.route) { UserScreen() }
            composable(Screen.Settings.route) { SettingScreen() }
            composable(Screen.Login.route) { LoginScreen() }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TriviaSwampTheme {
        MainScreen()
    }
}