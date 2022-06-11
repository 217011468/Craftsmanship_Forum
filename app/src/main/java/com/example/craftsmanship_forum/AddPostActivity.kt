package com.example.craftsmanship_forum

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {

    lateinit var _db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        _db = FirebaseDatabase.getInstance("https://craftsmanship-forum-default-rtdb.firebaseio.com/").reference

        findViewById<FloatingActionButton>(R.id.btnPostComplete).setOnClickListener { view -> btnPostCompleteOnclicked(view) }
    }

    fun btnPostCompleteOnclicked(view: View) {
        var title = findViewById<EditText>(R.id.editTextPostTitle).text.toString()
        var content = findViewById<EditText>(R.id.editTextPostCotent).text.toString()

        val post = Post.create()

        post.title = title
        post.postDate = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date());
        post.creator = LoginInfo.email
        post.content = content
        post.replys?.add(Reply("Test", "faesrnwea", SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())))

        //Get the object id for the new task from the Firebase Database
        val newPost = _db.child("Post").push()
        post.objectId = newPost.key

        newPost.setValue(post)

        Toast.makeText(
            this,
            "Your post is published!",
            Toast.LENGTH_SHORT
        ).show()

        this.finish()
    }
}