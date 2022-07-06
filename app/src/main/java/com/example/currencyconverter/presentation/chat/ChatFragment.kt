package com.example.currencyconverter.presentation.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.databinding.FragmentChatBinding
import com.example.currencyconverter.domain.models.ChatMessage
import com.example.currencyconverter.presentation.main.SignInActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment() {

    private lateinit var manager: LinearLayoutManager
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            startActivity(Intent(requireContext(), SignInActivity::class.java))
            requireActivity().finish()
            return
        }

        db = Firebase.database
        val messageRef = db.reference.child(MESSAGE_CHILD)

        val options = FirebaseRecyclerOptions.Builder<ChatMessage>()
            .setQuery(messageRef, ChatMessage::class.java)
            .build()

        adapter = MessageAdapter(options, getUserName())
        manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.progressBar.visibility = View.INVISIBLE
        binding.rvMessages.layoutManager = manager
        binding.rvMessages.adapter = adapter

        adapter.registerAdapterDataObserver(MyScrollToBottomObserver(binding.rvMessages, adapter, manager))

        binding.edtMessage.addTextChangedListener(SendButtonObserver(binding.ibtnSendMessage))

        binding.ibtnSendMessage.setOnClickListener {
            val chatMessage = ChatMessage(
                binding.edtMessage.text.toString(),
                getUserName(),
                getUserEmail(),
                getPhotoUrl(),
                null
            )
            db.reference.child(MESSAGE_CHILD).push().setValue(chatMessage)
            binding.edtMessage.setText("")
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            startActivity(Intent(requireContext(), SignInActivity::class.java))
            requireActivity().finish()
            return
        }
    }

    override fun onResume() {
        adapter.startListening()
        super.onResume()
    }

    override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPhotoUrl(): String? {
        val user = auth.currentUser
        return user?.photoUrl?.toString()
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else ANONYMOUS
    }

    private fun getUserEmail(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.email
        } else ANONYMOUS
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(requireContext())
        startActivity(Intent(requireContext(), SignInActivity::class.java))
        requireActivity().finish()
    }

    companion object {
        private const val TAG = "ChatFragment"
        const val MESSAGE_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}
