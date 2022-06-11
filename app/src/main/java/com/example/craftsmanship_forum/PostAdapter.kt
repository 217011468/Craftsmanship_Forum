package com.example.craftsmanship_forum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class PostAdapter(context: Context, taskList: MutableList<Post>) : BaseAdapter() {

    private val _inflater: LayoutInflater = LayoutInflater.from(context)
    private var _postList = taskList

    //private var _rowListener: PostRowListener = context as PostRowListener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val objectId: String? = _postList.get(position).objectId
        val title: String? = _postList.get(position).title
        val postDate: String? = _postList.get(position).postDate
        val content: String? = _postList.get(position).content
        val creator: String? = _postList.get(position).creator

        val view: View
        val listRowHolder: ListRowHolder
        if (convertView == null) {
            view = _inflater.inflate(R.layout.posts_list_item, parent, false)
            listRowHolder = ListRowHolder(view)
            view.tag = listRowHolder
        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }

        listRowHolder.title.text = title
        listRowHolder.postDate.text = postDate
        listRowHolder.creator.text = creator

        listRowHolder.title.setOnClickListener {
            Toast.makeText(
                null,
                "Your post is published!",
                Toast.LENGTH_SHORT
            ).show()
        }

        return view
    }

    override fun getItem(index: Int): Any {
        return _postList.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return _postList.size
    }

    private class ListRowHolder(row: View?) {
        val title: TextView = row!!.findViewById(R.id.textViewTitle)
        val postDate: TextView = row!!.findViewById(R.id.textViewPostDate)
        val creator: TextView = row!!.findViewById(R.id.textViewUsername)
    }


}
