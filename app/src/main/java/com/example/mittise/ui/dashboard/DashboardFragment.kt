package com.example.mittise.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mittise.R
import com.example.mittise.adapter.CategoriesAdapter
import com.example.mittise.adapter.UpdatesAdapter
import com.example.mittise.base.BaseFragment
import com.example.mittise.databinding.FragmentDashboardBinding
import com.example.mittise.model.Category
import com.example.mittise.model.Update

class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupClickListeners()
        animateViews()
    }

    private fun setupRecyclerViews() {
        // Setup Categories RecyclerView
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoriesAdapter(getDummyCategories())
        }

        // Setup Updates RecyclerView
        binding.updatesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UpdatesAdapter(getDummyUpdates()) { update ->
                Toast.makeText(requireContext(), "Clicked: ${update.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.weatherCard.setOnClickListener {
            animateView(it)
            // Navigate to weather details
            // findNavController().navigate(R.id.navigation_weather)
        }

        binding.viewAllUpdates.setOnClickListener {
            animateView(it)
            // Navigate to all updates
        }
    }

    private fun animateViews() {
        // Animate weather card
        binding.weatherCard.alpha = 0f
        binding.weatherCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .start()

        // Animate categories section
        binding.categoriesRecyclerView.alpha = 0f
        binding.categoriesRecyclerView.animate()
            .alpha(1f)
            .setStartDelay(200)
            .setDuration(500)
            .start()

        // Animate updates section
        binding.updatesRecyclerView.alpha = 0f
        binding.updatesRecyclerView.animate()
            .alpha(1f)
            .setStartDelay(400)
            .setDuration(500)
            .start()
    }

    private fun getDummyCategories(): List<Category> {
        return listOf(
            Category(1, "Seeds", R.drawable.ic_seeds),
            Category(2, "Fertilizers", R.drawable.ic_fertilizer),
            Category(3, "Equipment", R.drawable.ic_equipment),
            Category(4, "Pesticides", R.drawable.ic_pesticide)
        )
    }

    private fun getDummyUpdates(): List<Update> {
        return listOf(
            Update(
                1,
                "New Seeds Available",
                "Check out our latest collection of high-yield seeds",
                "2 hours ago",
                R.drawable.ic_seeds
            ),
            Update(
                2,
                "Weather Alert",
                "Heavy rainfall expected in your area",
                "5 hours ago",
                R.drawable.ic_weather
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 