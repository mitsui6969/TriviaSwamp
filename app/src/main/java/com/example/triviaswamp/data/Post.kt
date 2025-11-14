package com.example.triviaswamp.data

import java.time.LocalDateTime

/**
 * ツイッター風のポストデータクラス
 */
data class Post(
    val id: String,
    val author: String,
    val authorHandle: String,
    val content: String,
    val timestamp: LocalDateTime,
    val likes: Int = 0,
    val retweets: Int = 0,
    val replies: Int = 0,
    val isLiked: Boolean = false,
    val isRetweeted: Boolean = false
)
