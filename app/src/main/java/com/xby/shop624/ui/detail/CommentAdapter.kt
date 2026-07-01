package com.xby.shop624.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.ItemCommentBinding
import com.xby.shop624.databinding.ItemReplyBinding
import com.xby.shop624.data.local.entity.CommentEntity
import com.xby.shop624.model.CommentItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentAdapter(
    private val onReplyClick: (CommentEntity) -> Unit
) : ListAdapter<CommentItem, CommentAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onReplyClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemCommentBinding,
        private val onReplyClick: (CommentEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommentItem) {
            val comment = item.comment
            binding.tvUser.text = comment.userName
            binding.tvAvatar.text = comment.userName.take(1)
            binding.tvContent.text = comment.content
            binding.tvTime.text = formatTime(comment.createTime)
            binding.btnReply.setOnClickListener { onReplyClick(comment) }

            binding.layoutReplies.removeAllViews()
            item.replies.forEach { reply ->
                val replyBinding = ItemReplyBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    binding.layoutReplies,
                    false
                )
                replyBinding.tvUser.text = reply.userName
                replyBinding.tvAvatar.text = reply.userName.take(1)
                replyBinding.tvContent.text = reply.content
                replyBinding.tvTime.text = formatTime(reply.createTime)
                binding.layoutReplies.addView(replyBinding.root)
            }
            binding.layoutReplies.visibility =
                if (item.replies.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
        }

        private fun formatTime(time: Long): String {
            val sdf = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
            return sdf.format(Date(time))
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<CommentItem>() {
        override fun areItemsTheSame(a: CommentItem, b: CommentItem) = a.comment.id == b.comment.id
        override fun areContentsTheSame(a: CommentItem, b: CommentItem) = a == b
    }
}
