package com.example.currencyconverter.presentation.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.FragmentChatBinding
import com.example.currencyconverter.domain.models.ChatMessage
import com.example.currencyconverter.presentation.main.SignInActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class ChatFragment : Fragment() {

    private lateinit var manager: LinearLayoutManager
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter
    private lateinit var menuHost: MenuHost
    private lateinit var menuProvider: MenuProvider

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val openDocument = registerForActivityResult(OpenDocumentContract()) { uri ->
        if (uri == null) return@registerForActivityResult
        onImageSelected(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.database

        if (auth.currentUser == null) {
            startActivity(Intent(requireContext(), SignInActivity::class.java))
            requireActivity().finish()
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupLayout()
        setupMenu()

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
        menuHost.removeMenuProvider(menuProvider)
        _binding = null
    }

    private fun setupRecyclerView() {
        val messageRef = db.reference.child(MESSAGE_CHILD)

        val options = FirebaseRecyclerOptions.Builder<ChatMessage>()
            .setQuery(messageRef, ChatMessage::class.java)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()

        Log.e(TAG, "${getUserName()}")
        adapter = MessageAdapter(options, getUserName())
        manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true

        binding.rvMessages.layoutManager = manager
        binding.rvMessages.adapter = adapter

        adapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(
                binding.rvMessages,
                adapter,
                manager
            )
        )
    }

    private fun setupLayout() {
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
        binding.ibtnSendImage.setOnClickListener {
            openDocument.launch(arrayOf("image/*"))
        }
    }

    private fun setupMenu() {
        menuHost = requireActivity()
        menuProvider = getMenuProvider()
        menuHost.addMenuProvider(menuProvider)
    }

    private fun getMenuProvider() = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_chat, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.menu_sign_out -> signOut()
            }
            return true
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

    private fun onImageSelected(uri: Uri) {
        Log.d(TAG, "Uri: $uri")
        val user = auth.currentUser
        val tempMessage =
            ChatMessage(null, getUserName(), getUserEmail(), getPhotoUrl(), LOADING_IMAGE_URL)
        db.reference
            .child(MESSAGE_CHILD)
            .push()
            .setValue(tempMessage, DatabaseReference.CompletionListener { error, ref ->
                if (error != null) {
                    Log.w(TAG, "Unable to write messages to database.", error.toException())
                    return@CompletionListener
                }
                val key = ref.key
                val storageReference = Firebase.storage
                    .getReference(user!!.uid)
                    .child(key!!)
                    .child(uri.lastPathSegment!!)
                putImageInStorage(storageReference, uri, key)
            })
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
        storageReference.putFile(uri)
            .addOnSuccessListener(requireActivity()) { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        val chatMessage = ChatMessage(
                            null,
                            getUserName(),
                            getUserEmail(),
                            getPhotoUrl(),
                            uri.toString()
                        )
                        db.reference
                            .child(MESSAGE_CHILD)
                            .child(key!!)
                            .setValue(chatMessage)
                    }
            }
            .addOnFailureListener(requireActivity()) {
                Log.w(TAG, "Image upload task was unsuccessful.", it)
            }
    }

    companion object {
        private const val TAG = "ChatFragment"
        const val MESSAGE_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}

