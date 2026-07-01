package com.xby.shop624.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.ActivityChatBinding
import com.xby.shop624.databinding.ItemChatMessageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(val id: Long, val content: String, val isSelf: Boolean, val timestamp: Long = System.currentTimeMillis())

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.toolbar.title = "在线客服"

        adapter = ChatAdapter(messages)
        binding.rvMessages.layoutManager = LinearLayoutManager(this)
        binding.rvMessages.adapter = adapter

        addSystemMessage("欢迎咨询帮便利客服")

        binding.btnSend.setOnClickListener {
            val content = binding.etMessage.text?.toString()?.trim()
            if (!content.isNullOrEmpty()) {
                sendMessage(content)
                binding.etMessage.text?.clear()
                simulateReply(content)
            }
        }
    }

    private fun sendMessage(content: String) {
        val message = ChatMessage(System.currentTimeMillis(), content, true)
        messages.add(message)
        adapter.notifyItemInserted(messages.size - 1)
        binding.rvMessages.scrollToPosition(messages.size - 1)
    }

    private fun simulateReply(content: String) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            val replies = listOf(
                "您好，请问有什么可以帮您？",
                "这款商品库存充足，下单后次日即可配送到店",
                "批发价格已在商品详情页显示，量大可议价",
                "配送范围覆盖西安市及周边区域",
                "支持支付宝、微信支付和货到付款",
                "如有其他问题，请随时联系我们"
            )
            val randomReply = replies[(0 until replies.size).random()]
            val message = ChatMessage(System.currentTimeMillis(), randomReply, false)
            messages.add(message)
            adapter.notifyItemInserted(messages.size - 1)
            binding.rvMessages.scrollToPosition(messages.size - 1)
        }
    }

    private fun addSystemMessage(content: String) {
        val message = ChatMessage(System.currentTimeMillis(), content, false)
        messages.add(message)
        adapter.notifyItemInserted(messages.size - 1)
    }

    inner class ChatAdapter(private val messages: List<ChatMessage>) :
        RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemChatMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(messages[position])
        }

        override fun getItemCount() = messages.size

        inner class ViewHolder(private val binding: ItemChatMessageBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(message: ChatMessage) {
                if (message.isSelf) {
                    binding.layoutSelf.visibility = View.VISIBLE
                    binding.layoutOther.visibility = View.GONE
                    binding.tvContentSelf.text = message.content
                } else {
                    binding.layoutSelf.visibility = View.GONE
                    binding.layoutOther.visibility = View.VISIBLE
                    binding.tvContent.text = message.content
                }
            }
        }
    }
}