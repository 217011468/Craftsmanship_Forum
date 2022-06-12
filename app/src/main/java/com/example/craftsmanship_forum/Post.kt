package com.example.craftsmanship_forum

data class Reply(val creator: String, val content: String, val postDate: String)

class Post() {
    companion object Factory {
        fun create(): Post = Post()
    }

    var objectId: String? = ""
    var title: String? = ""
    var postDate: String? = ""
    var creator: String? = ""
    var content: String? = ""
    var latitute: String? = null
    var longitute: String? = null
    var replys: ArrayList<Reply>? = ArrayList<Reply>()
}
