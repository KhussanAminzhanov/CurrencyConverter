package com.example.currencyconverter.presentation.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class ChatFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            startActivity(Intent(requireContext(), SignInActivity::class.java))
            requireActivity().finish()
            return
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            startActivity(Intent(requireContext(), SignInActivity::class.java))
            requireActivity().finish()
            return
        }
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

    private fun signOut() {
        AuthUI.getInstance().signOut(requireContext())
        startActivity(Intent(requireContext(), SignInActivity::class.java))
        requireActivity().finish()
    }

    companion object {
        private const val TAG = "ChatFragment"
        const val MESSAGE_CHILD = "message"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}

