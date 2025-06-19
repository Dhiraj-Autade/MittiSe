package com.example.mittise.ui.marketplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mittise.data.model.Product
import com.example.mittise.databinding.FragmentMarketplaceBinding
import com.example.mittise.databinding.ItemProductBinding
import com.example.mittise.ui.base.BaseAdapter
import com.example.mittise.ui.base.createDiffCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MarketplaceFragment : Fragment() {

    private var _binding: FragmentMarketplaceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarketplaceViewModel by viewModels()
    private val productAdapter = ProductAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        binding.productsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.marketplaceState.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: MarketplaceState) {
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        productAdapter.submitList(state.products)
        
        state.error?.let { error ->
            // Show error message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private class ProductAdapter : BaseAdapter<Product, ItemProductBinding, ProductViewHolder>(
    createDiffCallback(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override fun createViewHolder(binding: ItemProductBinding): ProductViewHolder =
        ProductViewHolder(binding)

    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemProductBinding =
        ItemProductBinding.inflate(inflater, parent, false)

    override fun bindViewHolder(holder: ProductViewHolder, item: Product) {
        holder.bind(item)
    }
} 