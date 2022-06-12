package com.example.craftsmanship_forum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {

    lateinit var _db: DatabaseReference
    var _post: Post? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        Static.addCurrentLocation_longitute = null
        Static.addCurrentLocation_latitute = null

        _db = FirebaseDatabase.getInstance("https://craftsmanship-forum-default-rtdb.firebaseio.com/").reference

        findViewById<FloatingActionButton>(R.id.btnPostComplete).setOnClickListener { view -> btnPostCompleteOnclicked(view) }
        findViewById<FloatingActionButton>(R.id.btnAddMap).setOnClickListener { view -> btnAddMapOnclicked(view) }

        if (Static.mainActivityFragment == 2) {
            findViewById<EditText>(R.id.editTextPostTitle).visibility = View.GONE
            findViewById<FloatingActionButton>(R.id.btnAddMap).visibility = View.GONE
            _db.orderByKey().addValueEventListener(_postListener)
        }
    }

    fun btnPostCompleteOnclicked(view: View) {
        var title = findViewById<EditText>(R.id.editTextPostTitle).text.toString()
        var content = findViewById<EditText>(R.id.editTextPostCotent).text.toString()

        if (Static.mainActivityFragment == 1) {
            val post = Post.create()

            post.title = title
            post.postDate =
                SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date());
            post.creator = LoginInfo.email
            post.content = content
            if (Static.addCurrentLocation_latitute != null && Static.addCurrentLocation_longitute != null) {
                post.latitute = Static.addCurrentLocation_latitute.toString()
                post.longitute = Static.addCurrentLocation_longitute.toString()
            }

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

        } else if (Static.mainActivityFragment == 2) {
            if (_post == null) {
                Toast.makeText(
                    this,
                    "Loading reply, please try later",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            val newReply = _db.child("Post").child(Static.viewPostObjectId!!).child("replys")
            val reply = Reply(LoginInfo.email, content, SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date()))
            _post!!.replys!!.add(reply)
            newReply.setValue(_post!!.replys!!)
            this.finish()
        }
    }

    fun btnAddMapOnclicked(view: View) {
        val intent = Intent(this, AddCurrentLocationActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadPostList(dataSnapshot: DataSnapshot) {
        Log.d(Static.logTag, "loadTaskList")

        val tasks = dataSnapshot.children.iterator()

        //Check if current database contains any collection
        if (tasks.hasNext()) {

            val listIndex = tasks.next()
            val itemsIterator = listIndex.children.iterator()

            //check if the collection has any task or not
            while (itemsIterator.hasNext()) {

                //get current task
                val currentItem = itemsIterator.next()
                val post = Post.create()

                //get current data in a map
                val map = currentItem.getValue() as HashMap<String, Any>

                //key will return the Firebase ID
                post.objectId = currentItem.key
                post.title = map.get("title") as String?
                post.postDate = map.get("postDate") as String?
                post.creator = map.get("creator") as String?
                post.content = map.get("content") as String?
                post.latitute = map.get("latitute") as String?
                post.longitute = map.get("longitute") as String?
                val _replys = map.get("replys") as ArrayList<HashMap<String, String>>?
                post.replys = ArrayList<Reply>()
                if (_replys != null) {
                    for (reply in _replys) {
                        post.replys!!.add(Reply(reply.get("creator")!!, reply.get("content")!!, reply.get("postDate")!!))
                    }
                }

                if (post.objectId == Static.viewPostObjectId) {
                    _post = post
                }
            }
        }
    }

    var _postListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            loadPostList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w(Static.logTag, "loadItem:onCancelled", databaseError.toException())
        }
    }
}