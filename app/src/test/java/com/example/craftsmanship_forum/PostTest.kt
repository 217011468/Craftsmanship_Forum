package com.example.craftsmanship_forum

import android.content.Context
import android.content.SharedPreferences
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class PostTest {
    @Test
    fun test() {
        val post = Post()
        val result = Post.create()
        assertEquals(post.postDate, result.postDate)
        assertEquals(post.creator, result.creator)
        assertEquals(post.title, result.title)
        assertEquals(post.content, result.content)
        assertEquals(post.objectId, result.objectId)
        assertEquals(post.latitute, result.latitute)
        assertEquals(post.longitute, result.longitute)
        assertEquals(post.replys, result.replys)
    }
}