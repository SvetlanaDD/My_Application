package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "26 маяв 18:36",
            likedByMe = false,
            likes = 1199,
            repostByMe = false,
            repost = 1099999,
            view = 10
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount?.text = rulesCount(post.likes)
            repostCount?.text = rulesCount(post.repost)
            viewCount?.text = post.view.toString()

            if (post.likedByMe) {
                like?.setImageResource(R.drawable.ic_liked_24)
            }

            like?.setOnClickListener {
                post.likedByMe = !post.likedByMe
                like.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                if (post.likedByMe) post.likes++ else post.likes--
                likeCount?.text = rulesCount(post.likes)
            }


            repost?.setOnClickListener {
                post.repost++
                repostCount?.text = rulesCount(post.repost)
            }

        }

    }

    private fun rulesCount(like: Int): String {
        if (like > 1000 && like < 10000 && like % 1000 != 0 && like % 1000 >= 100) {
            val likeFormat = like / 1000.0
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.DOWN
            return String.format("%sK", df.format(likeFormat))
        } else
            if (like >= 1000 && like < 10000 && (like % 1000 == 0 || like % 1000 < 100)) return String.format(
                "%dK",
                like / 1000
            )
            else
                if (like >= 10000 && like < 1000000) return String.format("%dK", like / 1000)
                else
                    if (like >= 1000000 && (like / 1000 % 1000 == 0 || like / 1000 % 1000 < 100)) return String.format(
                        "%dM",
                        like / 1000000
                    )
                    else
                        if (like >= 1000000) {
                            val likeFormat = like / 1000000.0
                            val df = DecimalFormat("#.#")
                            df.roundingMode = RoundingMode.DOWN
                            return String.format("%sM", df.format(likeFormat))
                        }
        return String.format("%d", like)
    }
}