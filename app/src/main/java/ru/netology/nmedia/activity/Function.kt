package ru.netology.nmedia.activity

import java.math.RoundingMode
import java.text.DecimalFormat

class Function {
    fun rulesCount(like: Int): String {
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