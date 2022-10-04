package ru.netology.nmedia

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: Int,
    var repostByMe: Boolean = false,
    var repost: Int,
    var view: Int
)