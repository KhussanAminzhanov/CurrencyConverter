package com.example.currencyconverter.ui.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.databinding.FragmentUserProfileBinding
import com.google.android.material.tabs.TabLayoutMediator

const val REQUEST_IMAGE_CAPTURE = 1

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.userName.text = "User Name"
        binding.userEmail.text = "example@mail.com"

        val fragmentNames = arrayListOf("Main", "All Users", "Statistics")
        val fragmentList = arrayListOf(
            MainInfoFragment(),
            AllUsersFragment(),
            StatisticsFragment()
        )

        val adapter =
            ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        binding.vpProfile.adapter = adapter

        TabLayoutMediator(binding.tlUserProfile, binding.vpProfile) { tab, position ->
            tab.text = fragmentNames[position]
        }.attach()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}