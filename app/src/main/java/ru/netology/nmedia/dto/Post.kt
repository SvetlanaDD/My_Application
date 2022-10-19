package ru.netology.nmedia.dto

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: Int,
    var repost: Int,
    var view: Int,
    var video: String?
)