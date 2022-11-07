package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
            CREATE TABLE ${PostColumns.TABLE} (
            	${PostColumns.COLUMN_ID} INTEGER NOT NULL UNIQUE,
            	${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            	${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            	${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            	${PostColumns.COLUMN_LIKED_BY_ME} INTEGER NOT NULL DEFAULT 0,
            	${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            	${PostColumns.COLUMN_REPOST} INTEGER NOT NULL DEFAULT 0,
            	${PostColumns.COLUMN_VIEW} INTEGER NOT NULL DEFAULT 0,
            	${PostColumns.COLUMN_VIDEO} TEXT,
            	PRIMARY KEY("id" AUTOINCREMENT)
            );
        """.trimIndent()

        val addFirstPost = """
            INSERT INTO posts (${PostColumns.COLUMN_AUTHOR}, 
                                ${PostColumns.COLUMN_CONTENT}, 
                                ${PostColumns.COLUMN_PUBLISHED}, 
                                ${PostColumns.COLUMN_LIKES}, 
                                ${PostColumns.COLUMN_REPOST}, 
                                ${PostColumns.COLUMN_VIEW})
            VALUES ("Нетология. Университет интернет-профессий будущего",
                    "Делиться впечатлениями о любимых фильмах легко, а что если рассказать так, чтобы все заскучали",
                    "22 сентября в 10:14",
                    11,
                    2,
                    32
            )
        """.trimIndent()

        val addSecondPost = """
            INSERT INTO posts (${PostColumns.COLUMN_AUTHOR}, 
                                ${PostColumns.COLUMN_CONTENT}, 
                                ${PostColumns.COLUMN_PUBLISHED}, 
                                ${PostColumns.COLUMN_LIKES}, 
                                ${PostColumns.COLUMN_REPOST}, 
                                ${PostColumns.COLUMN_VIEW}, 
                                ${PostColumns.COLUMN_VIDEO})
            VALUES ("Нетология. Университет интернет-профессий будущего", 
                    "Освоение новой профессии — это не только открывающиеся возможности и перспективы, но и настоящий вызов самому себе. Приходится выходить из зоны комфорта и перестраивать привычный образ жизни: менять распорядок дня, искать время для занятий, быть готовым к возможным неудачам в начале пути. В блоге рассказали, как избежать стресса на курсах профпереподготовки → http://netolo.gy/fPD",
                    "23 сентября в 10:12",
                    10,
                    5,
                    20,
                    "https://www.youtube.com/watch?v=h4s0llOpKrU"
            
            )
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_REPOST = "repost"
        const val COLUMN_VIEW = "view"
        const val COLUMN_VIDEO = "video"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES,
            COLUMN_REPOST,
            COLUMN_VIEW,
            COLUMN_VIDEO
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "now")
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun repostById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               repost = repost + 1
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                likes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                repost = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_REPOST)),
                view = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEW)),
                video = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO))
            )
        }
    }
}