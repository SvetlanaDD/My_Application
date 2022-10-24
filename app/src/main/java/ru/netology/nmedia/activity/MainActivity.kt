package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels()
    private val interactionListener = object : OnInteractionListener {
        override fun onEdit(post: Post) {
            viewModel.edit(post)
        }

        override fun onLike(post: Post) {
            viewModel.likeById(post.id)
        }

        override fun onRepost(post: Post) {
            viewModel.repostById(post.id)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.content)
                type = "text/plain"
            }
            val repostIntent = Intent.createChooser(intent, getString(R.string.choose_repost_content))
            startActivity(repostIntent)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post.id)
        }

        override fun onVideo(urlVideo: String) {
            val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo))
            startActivity(intentVideo)
        }
    }

    val adapter = PostsAdapter(interactionListener)

    val newPostLauncher = registerForActivityResult(NewPostActivity.Contract) { result ->
        result ?: return@registerForActivityResult
        viewModel.changeContentAndSave(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()          // функция инициализации всех View
        subscribe()         // все observe
        setupListeners()    // все Listeners
    }

    private fun initView() {
        binding.list.adapter = adapter
    }

    private fun subscribe() {
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts) // обновляем данные
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                newPostLauncher.launch(post.content)
            }
        }
    }

    private fun setupListeners() {
        binding.add.setOnClickListener{
            newPostLauncher.launch(null)
        }
    }
}