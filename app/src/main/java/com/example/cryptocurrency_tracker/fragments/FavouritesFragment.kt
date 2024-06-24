package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import android.app.Activity
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency_tracker.R
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter
import com.example.cryptocurrency_tracker.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding

    // private val myViewModel: MyViewModel by activityViewModels()
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val act = activity
        viewModel = when (act) {
            is Activity -> ViewModelProvider(act).get(MyViewModel::class.java)
            else -> ViewModelProvider(this).get(MyViewModel::class.java)
        }
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
            val recyclerViewAdapter = RecyclerViewAdapter(
                favourites,
                onDisplayClick = { coin ->
                    viewModel.selectCoin(coin)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.description_fragment,CoinDescriptionFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                },
                onShareClick = { coin ->
                    viewModel.shareCoin(coin)
                },
                onFavouriteClick = { coin ->
                    viewModel.removeFromFavourites(coin)
                },
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