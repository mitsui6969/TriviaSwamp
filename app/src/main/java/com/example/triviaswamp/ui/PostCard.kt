package com.example.triviaswamp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triviaswamp.data.Post
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ツイッター風のポストカードコンポーネント
 */
@Composable
fun PostCard(
    post: Post,
    onLikeClick: (Post) -> Unit = {},
    onRetweetClick: (Post) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var isRetweeted by remember { mutableStateOf(post.isRetweeted) }
    var likeCount by remember { mutableStateOf(post.likes) }
    var retweetCount by remember { mutableStateOf(post.retweets) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { /* ポスト詳細画面への遷移 */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ヘッダー部分（作者情報）
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // プロフィール画像（円形のプレースホルダー）
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = post.author,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "@${post.authorHandle}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = formatTimestamp(post.timestamp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // ポスト内容
            Text(
                text = post.content,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // アクションボタン
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // リツイートボタン
                IconButton(
                    onClick = {
                        isRetweeted = !isRetweeted
                        retweetCount = if (isRetweeted) retweetCount + 1 else retweetCount - 1
                        onRetweetClick(post)
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "リツイート",
                            tint = if (isRetweeted) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (retweetCount > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = retweetCount.toString(),
                                color = if (isRetweeted) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                
                // いいねボタン
                IconButton(
                    onClick = {
                        isLiked = !isLiked
                        likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                        onLikeClick(post)
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "いいね",
                            tint = if (isLiked) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (likeCount > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = likeCount.toString(),
                                color = if (isLiked) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * タイムスタンプをフォーマットする関数
 */
private fun formatTimestamp(timestamp: LocalDateTime): String {
    val now = LocalDateTime.now()
    val diff = java.time.Duration.between(timestamp, now)
    
    return when {
        diff.toMinutes() < 1 -> "たった今"
        diff.toMinutes() < 60 -> "${diff.toMinutes()}分前"
        diff.toHours() < 24 -> "${diff.toHours()}時間前"
        diff.toDays() < 7 -> "${diff.toDays()}日前"
        else -> timestamp.format(DateTimeFormatter.ofPattern("M月d日"))
    }
}

/**
 * プレビュー用のサンプルポスト
 */
@Preview(showBackground = true)
@Composable
fun PostCardPreview() {
    val samplePost = Post(
        id = "1",
        author = "田中太郎",
        authorHandle = "tanaka_taro",
        content = "今日はいい天気ですね！散歩に出かけてみようと思います。皆さんも良い一日を！",
        timestamp = LocalDateTime.now().minusHours(2),
        likes = 42,
        retweets = 8,
    )
    
    PostCard(post = samplePost)
}
