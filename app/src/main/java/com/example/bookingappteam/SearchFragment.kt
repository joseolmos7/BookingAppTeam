package com.example.bookingappteam

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.bookingappteam.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)


        binding.roomCard.setOnClickListener {
            val intent = Intent(requireContext(), RoomCap::class.java)
            startActivity(intent)
        }

        binding.dateCard.setOnClickListener{

        }

        binding.purporseCard.setOnClickListener{
            val intent = Intent(requireContext(), PurposeView::class.java)
            startActivity(intent)
        }

        binding.equipmentCard.setOnClickListener{
            val intent = Intent(requireContext(), EquiptmentViews::class.java)
            startActivity(intent)
        }


        return binding.root
    // end of on create
    }





    // On destroy
    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}

