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

    var _postList: MutableList<Post>? = null

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

        /*val db = DatabaseHandler(this)
        val cards = db.getAllCards()

        val listViewPosts: ListView = binding.listViewPosts
        val listAdapter =
            ContactAdapter(view.context, R.layout.posts_list_item, cards)
        listViewPosts.adapter = listAdapter

        listViewPosts.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(view.context, "You clicked ${cards[position].word}", Toast.LENGTH_LONG).show()
        }*/

        _postList = mutableListOf()

        _db = FirebaseDatabase.getInstance("https://craftsmanship-forum-default-rtdb.firebaseio.com/").reference
        _adapter = PostAdapter(view.context, _postList!!)
        binding.listViewPosts.setAdapter(_adapter)
        _db.orderByKey().addValueEventListener(_taskListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadTaskList(dataSnapshot: DataSnapshot) {
        Log.d("MainActivity", "loadTaskList")

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

    var _taskListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            loadTaskList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    class ContactAdapter(
        context: Context,
        resource: Int,
        objects: MutableList<Post>
    ) : ArrayAdapter<Post>(context, resource, objects) {
        private var resource = resource
        private var post = objects
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var v = convertView
            if (v == null) {
                val layoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                v = layoutInflater.inflate(resource, parent, false)
            }
            var textViewTitle: TextView = v!!.findViewById<TextView>(R.id.textViewTitle)
            var textViewUsername: TextView = v!!.findViewById<TextView>(R.id.textViewUsername)
            var textViewPostDate: TextView = v!!.findViewById<TextView>(R.id.textViewPostDate)
            textViewTitle.text = post[position].title
            textViewUsername.text = post[position].creator
            textViewPostDate.text = post[position].postDate
            return v!!
        }
    }
}