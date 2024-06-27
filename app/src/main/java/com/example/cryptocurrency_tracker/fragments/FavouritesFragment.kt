package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.cryptocurrency_tracker.R
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter
import com.example.cryptocurrency_tracker.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding

   val viewModel: MyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favourites.observe(viewLifecycleOwner) { favourites ->
            val favouriteItems = favourites.toList()
            val recyclerViewAdapter = RecyclerViewAdapter(
                favouriteItems,
                viewModel,
                onDisplayClick = { coin ->
                    viewModel.selectCoin(coin)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.description_fragment,CoinDescriptionFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                }
            )
            binding.recyclerView.adapter = recyclerViewAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavouritesFragment()
    }
}