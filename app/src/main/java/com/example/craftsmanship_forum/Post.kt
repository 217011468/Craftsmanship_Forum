package com.example.craftsmanship_forum

class Post() {
    companion object Factory {
        fun create(): Post = Post()
    }

    var objectId: String? = ""
    var title: String? = ""
    var postDate: String? = ""
    var creator: String? = ""
    var content: String? = ""
}
