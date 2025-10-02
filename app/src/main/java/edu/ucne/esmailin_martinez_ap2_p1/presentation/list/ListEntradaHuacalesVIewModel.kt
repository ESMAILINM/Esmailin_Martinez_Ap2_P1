package edu.ucne.esmailin_martinez_ap2_p1.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.esmailin_martinez_ap2_p1.data.local.mapper.toDomain
import edu.ucne.esmailin_martinez_ap2_p1.domain.repository.EntradaHuacalesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListEntradaHuacalesVIewModel @Inject constructor(
    private val repository: EntradaHuacalesRepository
) : ViewModel() {

    val state: StateFlow<ListEntradaHuacalesUiState> =
        repository.observeEntradas()
            .map { entradas ->
                ListEntradaHuacalesUiState(
                    entradas = entradas.map { it.toDomain() },
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = ListEntradaHuacalesUiState(isLoading = true)
            )

    fun onEvent(event: ListEntradaHuacalesUiEvent) {
        when (event) {
            is ListEntradaHuacalesUiEvent.OnDeleteEntradaClick -> {
                viewModelScope.launch {
                    repository.deleteEntrada(event.entrada.identradaHuacales)
                }
            }
        }
    }
}
