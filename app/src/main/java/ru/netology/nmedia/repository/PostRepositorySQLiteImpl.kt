package ru.netology.nmedia.repository
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import ru.netology.nmedia.dao.PostDao
//import ru.netology.nmedia.dto.Post
//
//class PostRepositorySQLiteImpl(private val dao: PostDao) : PostRepository {
//    private var posts = emptyList<Post>()
//    private val data = MutableLiveData(posts)
//
//    init {
//        posts = dao.getAll()
//        data.value = posts
//    }
//
//    override fun getAll(): LiveData<List<Post>> = data
//
//    override fun likeById(id: Long) {
//        dao.likeById(id)
//        posts = posts.map {
//            if (it.id != id) it else {
//                if (it.likedByMe)
//                    it.copy(likedByMe = false, likes = it.likes - 1) else
//                    it.copy(likedByMe = true, likes = it.likes + 1)
//            }
//        }
//        data.value = posts
//    }
//
//    override fun repostById(id: Long) {
//        dao.repostById(id)
//        posts = posts.map {
//            if (it.id != id) it else it.copy(repost = it.repost + 1)
//        }
//        data.value = posts
//    }
//
//    override fun removeById(id: Long) {
//        dao.removeById(id)
//        posts = posts.filter { it.id != id }
//        data.value = posts
//    }
//
//    override fun save(post: Post) {
//        val id = post.id
//        val saved = dao.save(post)
//        posts = if (post.id == 0L) {
//            listOf(saved) + posts
//        } else {
//            posts.map {
//                if (it.id != id) it else saved
//            }
//        }
//        data.value = posts
//    }
//}