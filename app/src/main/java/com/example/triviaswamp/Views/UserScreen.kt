package com.example.triviaswamp.Views

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.triviaswamp.R
import com.example.triviaswamp.data.Post
import com.example.triviaswamp.ui.theme.TriviaSwampTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

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
    val context = LocalContext.current

    // -- States --
    val posts = remember { mutableStateListOf<Post>() }
    var showPostBox by remember { mutableStateOf(false) }
    var newPostText by remember { mutableStateOf("") }

    var username by remember { mutableStateOf("ユーザー名") }
    var profile by remember { mutableStateOf("これはプロフィールです。") }
    var headerImage by remember { mutableStateOf<Any?>(R.drawable.footer) }
    var iconImage by remember { mutableStateOf<Any?>(R.drawable.footer) }

    var showEditDialog by remember { mutableStateOf(false) }
    var tempUsername by remember { mutableStateOf("") }
    var tempProfile by remember { mutableStateOf("") }
    var tempHeaderImage by remember { mutableStateOf<Any?>(null) }
    var tempIconImage by remember { mutableStateOf<Any?>(null) }

    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    // -- Modern Photo Picker Launchers --
    val pickHeaderLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let { tempHeaderImage = it }
    }
    val pickIconLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let { tempIconImage = it }
    }

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
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            AsyncImage(
                model = headerImage,
                contentDescription = "ヘッダー画像",
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentScale = ContentScale.Crop
            )

            ProfileSection(
                name = username,
                profile = profile,
                icon = iconImage,
                onEditClick = {
                    tempUsername = username
                    tempProfile = profile
                    tempHeaderImage = headerImage
                    tempIconImage = iconImage
                    showEditDialog = true
                }
            )

            Divider()

            if (showPostBox) {
                PostInputSection(
                    text = newPostText,
                    onTextChange = { newPostText = it },
                    onCancel = { showPostBox = false },
                    onPost = {
                        if (newPostText.isNotBlank()) {
                            posts.add(0, Post(UUID.randomUUID().toString(), username, "@$username", newPostText, LocalDateTime.now()))
                            newPostText = ""
                            showPostBox = false
                        }
                    },
                    focusRequester = focusRequester
                )
            }

            PostListNoGap(posts, username, iconImage, Modifier.weight(1f))
        }
    }

    if (showEditDialog) {
        EditUserDialog(
            username = tempUsername,
            profile = tempProfile,
            header = tempHeaderImage,
            icon = tempIconImage,
            onUsernameChange = { tempUsername = it },
            onProfileChange = { tempProfile = it },
            onHeaderClick = { pickHeaderLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            onIconClick = { pickIconLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            onDismiss = { showEditDialog = false },
            onSave = {
                username = tempUsername
                profile = tempProfile
                headerImage = tempHeaderImage
                iconImage = tempIconImage
                showEditDialog = false
            }
        )
    }
}


@Composable
fun ProfileSection(
    name: String,
    profile: String,
    icon: Any?,
    onEditClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 32.dp)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.height(6.dp))
                Text(profile, fontSize = 14.sp, color = Color.Gray, maxLines = 2)
            }
            AsyncImage(
                model = icon,
                contentDescription = "アイコン",
                modifier = Modifier.size(100.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 29.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onEditClick, modifier = Modifier.height(30.dp)) {
                Text("編集", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun EditUserDialog(
    username: String,
    profile: String,
    header: Any?,
    icon: Any?,
    onUsernameChange: (String) -> Unit,
    onProfileChange: (String) -> Unit,
    onHeaderClick: () -> Unit,
    onIconClick: () -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 6.dp) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("プロフィール編集", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))

                Text("ヘッダー画像（タップで変更）")
                AsyncImage(
                    model = header,
                    contentDescription = "ヘッダー",
                    modifier = Modifier.fillMaxWidth().height(100.dp).clickable(onClick = onHeaderClick),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(16.dp))

                Text("アイコン（タップで変更）")
                AsyncImage(
                    model = icon,
                    contentDescription = "アイコン",
                    modifier = Modifier.size(80.dp).clip(CircleShape).clickable(onClick = onIconClick),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(value = username, onValueChange = onUsernameChange, label = { Text("名前") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = profile, onValueChange = onProfileChange, label = { Text("プロフィール") }, modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("キャンセル") }
                    Button(onClick = onSave) { Text("保存") }
                }
            }
        }
    }
}

@Composable
fun PostInputSection(
    text: String,
    onTextChange: (String) -> Unit,
    onCancel: () -> Unit,
    onPost: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).focusRequester(focusRequester),
            label = { Text("つぶやきを書く...") },
            placeholder = { Text("いま何してる？") }
        )
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onCancel) { Text("キャンセル") }
            Button(onClick = onPost) { Text("投稿") }
        }
    }
}

@Composable
fun PostListNoGap(
    posts: List<Post>,
    username: String,
    icon: Any?,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(posts) { post ->
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.Top) {
                AsyncImage(
                    model = icon,
                    contentDescription = "User Icon",
                    modifier = Modifier.size(48.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(username, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(post.content, fontSize = 16.sp)
                }
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
