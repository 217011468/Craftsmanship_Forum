package com.example.craftsmanship_forum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast

class ViewPostAdapter(context: Context, post: Array<Post>) : BaseAdapter() {
    private val _inflater: LayoutInflater = LayoutInflater.from(context)
    private var _post = post

    //private var _rowListener: PostRowListener = context as PostRowListener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (position == 0) {
            val objectId: String? = _post[0].objectId
            val title: String? = _post[0].title
            val postDate: String? = _post[0].postDate
            val content: String? = _post[0].content
            val creator: String? = _post[0].creator

            val view: View
            val listRowHolder: ListRowHolder
            if (convertView == null) {
                view = _inflater.inflate(R.layout.view_posts_list_item, parent, false)
                listRowHolder = ListRowHolder(view)
                view.tag = listRowHolder
            } else {
                view = convertView
                listRowHolder = view.tag as ListRowHolder
            }

            listRowHolder.title.text = title
            listRowHolder.postDate.text = postDate
            listRowHolder.creator.text = creator
            listRowHolder.content.text = content
            return view
        } else {
            val replys = _post[0].replys
            if (replys != null) {

                val postDate: String? = replys.get(position - 1).postDate
                val content: String? = replys.get(position - 1).content
                val creator: String? = replys.get(position - 1).creator

                val view: View
                val listRowHolder: ListRowHolder
                if (convertView == null) {
                    view = _inflater.inflate(R.layout.view_posts_list_item, parent, false)
                    listRowHolder = ListRowHolder(view)
                    view.tag = listRowHolder
                } else {
                    view = convertView
                    listRowHolder = view.tag as ListRowHolder
                }

                listRowHolder.tvpostDate.text = "Reply Date: "
                listRowHolder.postDate.text = postDate
                listRowHolder.creator.text = creator
                listRowHolder.content.text = content

                listRowHolder.tvTitle.visibility = View.GONE
                listRowHolder.title.visibility = View.GONE
                return view
            }

            return _inflater.inflate(R.layout.view_posts_list_item, parent, false)
        }
    }

    override fun getItem(index: Int): Any {
        return if (index == 0) {
            _post[0]
        } else {
            _post[0].replys!!.get(index - 1)
        }
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return _post[0].replys!!.size + 1
    }

    private class ListRowHolder(row: View?) {
        val tvTitle: TextView = row!!.findViewById(R.id.viewPost_tvTitle)
        val title: TextView = row!!.findViewById(R.id.viewPost_textViewTitle)
        val postDate: TextView = row!!.findViewById(R.id.viewPost_textViewPostDate)
        val tvpostDate: TextView = row!!.findViewById(R.id.viewPost_tvPostDate)
        val creator: TextView = row!!.findViewById(R.id.viewPost_textViewUsername)
        val content: TextView = row!!.findViewById(R.id.viewPost_textViewContent)
    }
}