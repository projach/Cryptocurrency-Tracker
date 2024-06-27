package com.example.cryptocurrency_tracker.fragments

import android.view.View
import android.os.Bundle
import androidx.room.Room
import android.text.Editable
import android.view.ViewGroup
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.cryptocurrency_tracker.R
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency_tracker.database.UserEntity
import com.example.cryptocurrency_tracker.viewmodels.MyViewModel
import com.example.cryptocurrency_tracker.database.DatabaseInstance
import com.example.cryptocurrency_tracker.recyclerview.RecyclerViewAdapter
import com.example.cryptocurrency_tracker.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

   val viewModel: MyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var nameSymbol = ""
        showUIOnline(nameSymbol)
        binding.searchInput.addTextChangedListener(object: TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameSymbol = s.toString()
                showUIOnline(nameSymbol)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun showUIOnline(nameSymbol:String) {
        val data = getDatabase(nameSymbol)
        if (data != null) {
            val recyclerViewAdapter = RecyclerViewAdapter(
                data,
                viewModel,
                onDisplayClick = { coin ->
                    viewModel.selectCoin(coin)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.description_fragment, CoinDescriptionFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                }
            )
            binding.recyclerView.adapter = recyclerViewAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getDatabase(nameSymbol:String): List<UserEntity>? {
        val ctx = context
        if (ctx != null) {
            val database =
                Room.databaseBuilder(ctx, DatabaseInstance::class.java, "UserDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            val data = database.getUserDao().search(nameSymbol)
            if (data.isEmpty()) {
                return null
            } else {
                return data
            }
        }
        return null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}
