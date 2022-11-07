package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private val viewModel by viewModels<PostViewModel>(ownerProducer = ::requireParentFragment)
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
            val repostIntent =
                Intent.createChooser(intent, getString(R.string.choose_repost_content))
            startActivity(repostIntent)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post.id)
        }

        override fun onFragment(post: Post) {
            findNavController().navigate(R.id.action_feedFragment_to_postFragment, Bundle().apply {
                textArg = post.id.toString()
            })
        }

        override fun onVideo(urlVideo: String) {
            val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo))
            startActivity(intentVideo)
        }
    }

    val adapter = PostsAdapter(interactionListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)

        initView()          // функция инициализации всех View
        subscribe()         // все observe
        setupListeners()    // все Listeners
        return binding.root
    }

    private fun initView() {
        binding.list.adapter = adapter
    }

    private fun subscribe() {
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts) // обновляем данные
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id != 0L) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content  // переход между фрагментами по id перехода, воторой аргумент - через бандл - текст поста
                    })
            }
        }
    }

    private fun setupListeners() {
        binding.add.setOnClickListener {                 // при нажатии на + (add) работали с контрактами, теперь переходим на другой фрагмент
            //           newPostLauncher.launch(null)
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment) // по id перехода между фрагментами
        }
    }
}
