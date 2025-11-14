package com.example.triviaswamp.Views

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triviaswamp.data.Post
import com.example.triviaswamp.ui.PostCard
import com.example.triviaswamp.ui.theme.TriviaSwampTheme
import java.time.LocalDateTime

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            TriviaSwampTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // ホーム画面のUIを呼び出し
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * ホーム画面のUIを定義するコンポーザブル関数
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState()
    var searchResults by rememberSaveable { mutableStateOf(listOf<String>()) }
    
    // サンプルのポストデータ
    val samplePosts = rememberSaveable {
        listOf(
            Post(
                id = "1",
                author = "田中太郎",
                authorHandle = "tanaka_taro",
                content = "今日はいい天気ですね！散歩に出かけてみようと思います。皆さんも良い一日を！",
                timestamp = LocalDateTime.now().minusHours(2),
                likes = 42,
                retweets = 8,
            ),
            Post(
                id = "2",
                author = "佐藤花子",
                authorHandle = "sato_hanako",
                content = "新しいレストランを発見しました！料理がとても美味しくて、また行きたいです。おすすめです！",
                timestamp = LocalDateTime.now().minusHours(5),
                likes = 28,
                retweets = 5,
            ),
            Post(
                id = "3",
                author = "山田次郎",
                authorHandle = "yamada_jiro",
                content = "プログラミングの勉強を始めました。最初は難しく感じますが、少しずつ理解できてきて楽しいです。",
                timestamp = LocalDateTime.now().minusDays(1),
                likes = 15,
                retweets = 2,
            ),
            Post(
                id = "4",
                author = "鈴木美咲",
                authorHandle = "suzuki_misaki",
                content = "映画館で新しい映画を観ました。ストーリーが面白くて、最後まで引き込まれました。",
                timestamp = LocalDateTime.now().minusDays(2),
                likes = 33,
                retweets = 6,
            )
        )
    }
    
    val onSearch: (String) -> Unit = { query ->
        // 検索ロジックをここに実装
        searchResults = listOf("検索結果1: $query", "検索結果2: $query", "検索結果3: $query")
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // ヘッダー
        Text(
            text = "ホーム",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // 検索バー
        SearchBar(
            query = textFieldState.text.toString(),
            onQueryChange = { textFieldState.edit { replace(0, length, it) } },
            onSearch = {
                onSearch(textFieldState.text.toString())
                expanded = false
            },
            active = expanded,
            onActiveChange = { expanded = it },
            placeholder = { Text("検索...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // 検索結果を表示
            Column(Modifier.verticalScroll(rememberScrollState())) {
                searchResults.forEach { result ->
                    ListItem(
                        headlineContent = { Text(result) },
                        modifier = Modifier
                            .clickable {
                                textFieldState.edit { replace(0, length, result) }
                                expanded = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
        
        // ポスト一覧
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(samplePosts) { post ->
                PostCard(
                    post = post,
                    onLikeClick = { /* いいね処理 */ },
                    onRetweetClick = { /* リツイート処理 */ },
                )
            }
        }
    }
}

/**
 * HomeScreenのプレビュー用コンポーザブル関数
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TriviaSwampTheme {
        HomeScreen()
    }
}