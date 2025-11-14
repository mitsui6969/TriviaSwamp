package com.example.triviaswamp.Views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.triviaswamp.R
import com.example.triviaswamp.ui.theme.TriviaSwampTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriviaSwampTheme {
                UserScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen() {

    // 投稿
    val posts = remember { mutableStateListOf<String>() }
    var showPostBox by remember { mutableStateOf(false) }
    var newPostText by remember { mutableStateOf("") }

    // 編集ダイアログの状態
    var showEditDialog by remember { mutableStateOf(false) }

    // 編集可能ユーザーデータ
    var username by remember { mutableStateOf("ユーザー名") }
    var profile by remember { mutableStateOf("これはプロフィールです。ここに自己紹介文が入ります。") }
    var headerImage by remember { mutableStateOf(R.drawable.footer) }
    var iconImage by remember { mutableStateOf(R.drawable.footer) }

    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            if (!showPostBox) {
                FloatingActionButton(onClick = {
                    showPostBox = true
                    coroutineScope.launch {
                        delay(100)
                        focusRequester.requestFocus()
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "新規投稿")
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            // ヘッダー
            Image(
                painter = painterResource(id = headerImage),
                contentDescription = "ヘッダー画像",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )

            // プロフィール
            ProfileSection( // ProfileRowからProfileSectionに変更
                name = username,
                profile = profile,
                onEditClick = { showEditDialog = true },
                iconRes = iconImage
            )

            Divider()

            // 投稿欄
            if (showPostBox) {
                PostInputSection(
                    text = newPostText,
                    onTextChange = { newPostText = it },
                    onCancel = {
                        newPostText = ""
                        showPostBox = false
                    },
                    onPost = {
                        if (newPostText.isNotBlank()) {
                            posts.add(0, newPostText)
                            newPostText = ""
                            showPostBox = false
                        }
                    },
                    focusRequester = focusRequester
                )
            }

            // weight(1f) を適用して残りのスペースをすべて使う
            PostListNoGap(posts = posts, modifier = Modifier.weight(1f))
        }
    }

    // 編集ダイアログ
    if (showEditDialog) {
        EditUserDialog(
            username = username,
            profile = profile,
            headerRes = headerImage,
            iconRes = iconImage,
            onDismiss = { showEditDialog = false },
            onSave = { newName, newProfile, newHeader, newIcon ->
                username = newName
                profile = newProfile
                headerImage = newHeader
                iconImage = newIcon
                showEditDialog = false
            }
        )
    }
}

// -----------------------------------------------------------------------------
// プロフィール表示
// -----------------------------------------------------------------------------

@Composable
fun ProfileSection(name: String, profile: String, onEditClick: () -> Unit, iconRes: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = profile, fontSize = 14.sp, color = Color.Gray, maxLines = 2)
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "ユーザーアイコン",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable { }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 29.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onEditClick,
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text("編集", fontSize = 14.sp)
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 編集ダイアログ
// -----------------------------------------------------------------------------

@Composable
fun EditUserDialog(
    username: String,
    profile: String,
    headerRes: Int,
    iconRes: Int,
    onDismiss: () -> Unit,
    onSave: (String, String, Int, Int) -> Unit
) {
    var newName by remember { mutableStateOf(username) }
    var newProfile by remember { mutableStateOf(profile) }
    var newHeader by remember { mutableStateOf(headerRes) }
    var newIcon by remember { mutableStateOf(iconRes) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("プロフィール編集", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))

                Text("ヘッダー画像（タップで変更）")
                Image(
                    painter = painterResource(newHeader),
                    contentDescription = "ヘッダー",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable { newHeader = R.drawable.footer },
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(16.dp))

                Text("アイコン（タップで変更）")
                Image(
                    painter = painterResource(newIcon),
                    contentDescription = "アイコン",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .clickable { newIcon = R.drawable.footer }
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("名前") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = newProfile,
                    onValueChange = { newProfile = it },
                    label = { Text("プロフィール") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) { Text("キャンセル") }
                    Button(onClick = {
                        onSave(newName, newProfile, newHeader, newIcon)
                    }) { Text("保存") }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 投稿処理
// -----------------------------------------------------------------------------

@Composable
fun PostInputSection(
    text: String,
    onTextChange: (String) -> Unit,
    onCancel: () -> Unit,
    onPost: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .focusRequester(focusRequester),
            label = { Text("つぶやきを書く...") },
            placeholder = { Text("いま何してる？") },
            singleLine = false,
            maxLines = 6
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onCancel) { Text("キャンセル") }
            Button(onClick = onPost) { Text("投稿") }
        }
    }
}

@Composable
fun PostListNoGap(posts: List<String>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.background(Color.White),
        contentPadding = PaddingValues(0.dp)
    ) {
        items(posts) { post ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                color = Color.White
            ) {
                Text(
                    text = post,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    fontSize = 16.sp
                )
            }
            Divider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserScreenPreview() {
    TriviaSwampTheme {
        UserScreen()
    }
}
