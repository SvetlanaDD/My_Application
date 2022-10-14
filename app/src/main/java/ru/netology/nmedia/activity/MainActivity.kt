package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels()
    private val interactionListener = object : OnInteractionListener {
        override fun onEdit(post: Post) {
            binding.groupEdit.visibility = View.VISIBLE
            viewModel.edit(post)
        }

        override fun onLike(post: Post) {
            viewModel.likeById(post.id)
        }

        override fun onRepost(post: Post) {
            viewModel.repostById(post.id)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post.id)
        }
    }

    val adapter = PostsAdapter(interactionListener)


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
        binding.groupEdit.visibility = View.GONE // скрыть группу View редактирования поста
    }

    private fun subscribe() {
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts) // обновляем данные
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                binding.textPost.text = post.content // заполняем поле Редактирование поста
                with(binding.content) {
                    requestFocus()              // как только edited меняется, обновляем текст
                    setText(post.content)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.save.setOnClickListener {
            with(binding.content) {
                // показ всплывающей подсказки error_empty_content
                if (text.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    viewModel.changeContent(text.toString())
                    viewModel.save()
                    binding.groupEdit.visibility = View.GONE // скрыть группу View редактирования поста, если она есть
                    setText("")                             // обнуление поля ввода
                    clearFocus()
                    AndroidUtils.hideKeyboard(this)     // скрыть клавиатуру
                }
            }
        }
        binding.removeEdit.setOnClickListener {
            with(binding.content) {
                viewModel.save()
                binding.groupEdit.visibility = View.GONE // скрыть группу View редактирования поста
                setText("")                             // обнуление поля ввода
                clearFocus()
                AndroidUtils.hideKeyboard(this)     // скрыть клавиатуру
            }
        }
    }

}