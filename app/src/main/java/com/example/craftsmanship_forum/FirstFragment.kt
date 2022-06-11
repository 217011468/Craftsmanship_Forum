package com.example.craftsmanship_forum

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.craftsmanship_forum.databinding.FragmentFirstBinding
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment() : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    lateinit var _db: DatabaseReference
    lateinit var _adapter: PostAdapter

    var _postList: MutableList<Post> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _db = FirebaseDatabase.getInstance("https://craftsmanship-forum-default-rtdb.firebaseio.com/").reference
        _adapter = PostAdapter(view.context, _postList!!)
        binding.listViewPosts.setAdapter(_adapter)
        _db.orderByKey().addValueEventListener(_postListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadPostList(dataSnapshot: DataSnapshot) {
        Log.d(Static.logTag, "loadTaskList")

        val tasks = dataSnapshot.children.iterator()

        //Check if current database contains any collection
        if (tasks.hasNext()) {

            _postList!!.clear()

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
                _postList!!.add(post)
            }
        }

        //alert adapter that has changed
        _adapter.notifyDataSetChanged()

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