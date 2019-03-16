package com.example.weroo.becksco.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weroo.becksco.R
import com.example.weroo.becksco.model.PostSummaryDTO
import kotlinx.android.synthetic.main.item_post_summary.view.*

class PostSummaryAdapter: RecyclerView.Adapter<PostSummaryAdapter.PostSummaryHolder>() {

    private val posts: MutableList<PostSummaryDTO> by lazy { mutableListOf<PostSummaryDTO>() }

    // RecyclerView의 행을 표시하는데 사용되는 layout xml을 가져오는 역할
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostSummaryHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_post_summary, viewGroup, false)
        return PostSummaryHolder(v)
    }
    // RecyclerView의 행 갯수 리턴
    override fun getItemCount(): Int {
        return posts.size
    }

    // 마침내 RecyclerView의 행에 보여질 TextView를 binding
    override fun onBindViewHolder(holder: PostSummaryHolder, position: Int) {
        // posts MutableList에서 position index번째의 PostSummaryDTO를 바인딩
        holder.bind(posts[position])
    }

    fun resetPosts(posts: List<PostSummaryDTO>) {
        this.posts.clear()
        this.posts.addAll(posts)
    }

    class PostSummaryHolder(view: View): RecyclerView.ViewHolder(view) {
        // postSummaryTitle = post_create 시에 적어준 title
        private val postSummaryTitle: TextView = view.postSummaryTitle
        private val postSummaryWriter: TextView = view.postSummaryWriter

        fun bind(postSummary: PostSummaryDTO) {
            postSummaryTitle.text = postSummary.title
            postSummaryWriter.text = postSummary.user.name
        }
    }
}