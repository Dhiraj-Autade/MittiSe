package com.example.mittise.ui.apmc

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mittise.data.model.MandiPrice
import com.example.mittise.data.repository.MandiPricesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApmcViewModel @Inject constructor(
    private val mandiPricesRepository: MandiPricesRepository
) : ViewModel() {

    companion object {
        private const val TAG = "ApmcViewModel"
    }

    private val _uiState = MutableStateFlow(ApmcUiState())
    val uiState: StateFlow<ApmcUiState> = _uiState.asStateFlow()

    init {
        loadMandiPrices()
        loadFilterOptions()
    }

    fun loadMandiPrices() {
        Log.d(TAG, "loadMandiPrices: Starting with filters - ${_uiState.value.filters}")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                Log.d(TAG, "loadMandiPrices: Fetching from repository")
                val filters = _uiState.value.filters
                val result = mandiPricesRepository.getMandiPrices(
                    state = filters.selectedState,
                    district = filters.selectedDistrict,
                    commodity = filters.selectedCommodity,
                    market = filters.selectedMarket,
                    limit = 200
                )
                result.fold(
                    onSuccess = { prices ->
                        Log.d(TAG, "loadMandiPrices: Success - loaded ${prices.size} prices")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            mandiPrices = prices,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "loadMandiPrices: Error fetching mandi prices", exception)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load mandi prices"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "loadMandiPrices: Exception", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load mandi prices"
                )
            }
        }
    }

    fun loadFilterOptions() {
        viewModelScope.launch {
            try {
                // Load states
                mandiPricesRepository.getAvailableStates().fold(
                    onSuccess = { states ->
                        _uiState.value = _uiState.value.copy(
                            availableStates = listOf("All States") + states
                        )
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "loadFilterOptions: Failed to load states", exception)
                    }
                )

                // Load commodities
                mandiPricesRepository.getAvailableCommodities().fold(
                    onSuccess = { commodities ->
                        _uiState.value = _uiState.value.copy(
                            availableCommodities = listOf("All Commodities") + commodities
                        )
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "loadFilterOptions: Failed to load commodities", exception)
                    }
                )

                // Load markets
                mandiPricesRepository.getAvailableMarkets().fold(
                    onSuccess = { markets ->
                        _uiState.value = _uiState.value.copy(
                            availableMarkets = listOf("All Markets") + markets
                        )
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "loadFilterOptions: Failed to load markets", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "loadFilterOptions: Exception", e)
            }
        }
    }

    fun updateStateFilter(state: String) {
        val newFilters = _uiState.value.filters.copy(selectedState = if (state == "All States") null else state)
        _uiState.value = _uiState.value.copy(filters = newFilters)
        loadDistrictsForState(state)
        loadMandiPrices()
    }

    fun updateDistrictFilter(district: String) {
        val newFilters = _uiState.value.filters.copy(selectedDistrict = if (district == "All Districts") null else district)
        _uiState.value = _uiState.value.copy(filters = newFilters)
        loadMarketsForLocation()
        loadMandiPrices()
    }

    fun updateCommodityFilter(commodity: String) {
        val newFilters = _uiState.value.filters.copy(selectedCommodity = if (commodity == "All Commodities") null else commodity)
        _uiState.value = _uiState.value.copy(filters = newFilters)
        loadMandiPrices()
    }

    fun updateMarketFilter(market: String) {
        val newFilters = _uiState.value.filters.copy(selectedMarket = if (market == "All Markets") null else market)
        _uiState.value = _uiState.value.copy(filters = newFilters)
        loadMandiPrices()
    }

    private fun loadDistrictsForState(state: String) {
        viewModelScope.launch {
            try {
                val actualState = if (state == "All States") null else state
                mandiPricesRepository.getAvailableDistricts(actualState).fold(
                    onSuccess = { districts ->
                        _uiState.value = _uiState.value.copy(
                            availableDistricts = listOf("All Districts") + districts
                        )
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "loadDistrictsForState: Failed", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "loadDistrictsForState: Exception", e)
            }
        }
    }

    private fun loadMarketsForLocation() {
        viewModelScope.launch {
            try {
                val filters = _uiState.value.filters
                mandiPricesRepository.getAvailableMarkets(
                    state = filters.selectedState,
                    district = filters.selectedDistrict
                ).fold(
                    onSuccess = { markets ->
                        _uiState.value = _uiState.value.copy(
                            availableMarkets = listOf("All Markets") + markets
                        )
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "loadMarketsForLocation: Failed", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "loadMarketsForLocation: Exception", e)
            }
        }
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            filters = FilterState()
        )
        loadMandiPrices()
    }

    fun refresh() {
        loadMandiPrices()
    }
}

data class FilterState(
    val selectedState: String? = null,
    val selectedDistrict: String? = null,
    val selectedCommodity: String? = null,
    val selectedMarket: String? = null
)

data class ApmcUiState(
    val isLoading: Boolean = false,
    val mandiPrices: List<MandiPrice> = emptyList(),
    val error: String? = null,
    val filters: FilterState = FilterState(),
    val availableStates: List<String> = emptyList(),
    val availableDistricts: List<String> = emptyList(),
    val availableCommodities: List<String> = emptyList(),
    val availableMarkets: List<String> = emptyList()
) 