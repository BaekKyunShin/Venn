package com.example.weroo.becksco.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weroo.becksco.R
import com.example.weroo.becksco.model.PostSummaryDTO
import kotlinx.android.synthetic.main.item_post_summary.view.*
import retrofit2.http.POST
import java.lang.IllegalArgumentException

// recycler view :

class PostSummaryAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val posts: MutableList<PostSummaryDTO> by lazy { mutableListOf<PostSummaryDTO>() }

    private var loading = false

    private val POST_VIEW = 0
    private val LOADING_VIEW = 1

    fun setLoading(loading: Boolean) {
        this.loading = loading;
    }

    override fun getItemViewType(position: Int): Int {
        if(!loading) {
            return POST_VIEW
        }
        if (position < posts.size) {
            return POST_VIEW;
        }
        return LOADING_VIEW
    }

    // RecyclerView의 행을 표시하는데 사용되는 layout xml을 가져오는 역할
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // viewGroup: view를 자식으로 가질 수 있는 Group
        val inflater: LayoutInflater = LayoutInflater.from(viewGroup.context);
        if (viewType == POST_VIEW) {
            return PostSummaryHolder(inflater.inflate(R.layout.item_post_summary, viewGroup, false));
        }
        if (viewType == LOADING_VIEW) {
            return LoadingHolder(inflater.inflate(R.layout.layout_toolbar, viewGroup, false));
        }
        throw IllegalArgumentException()
    }

    // RecyclerView의 행 갯수 리턴
    override fun getItemCount(): Int {
        if (loading) {
            return posts.size + 1
        }
        return posts.size
    }

    // 마침내 RecyclerView의 행에 보여질 TextView를 binding
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // posts MutableList에서 position index번째의 PostSummaryDTO를 바인딩
        if (holder is PostSummaryHolder) {
            holder.bind(posts.get(position))
            return
        }
        if (holder is LoadingHolder) {
            return
        }
        throw IllegalArgumentException()
    }

    fun appendPosts(posts: List<PostSummaryDTO>) {
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

    class LoadingHolder(view: View): RecyclerView.ViewHolder(view) {

    }
}