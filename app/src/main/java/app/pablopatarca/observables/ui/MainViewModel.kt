package app.pablopatarca.observables.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var currentJob: Job? = null

    private val _liveData = MutableLiveData("...")
    val liveData: LiveData<String> = _liveData

    private val _stateFlow = MutableStateFlow("...")
    val stateFlow: StateFlow<String> = _stateFlow.asStateFlow()

    // To emit an state once
    // Examples: Navigation, Snackbar messages, etc
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerLiveData(text: String){
        _liveData.value = text
    }

    fun triggerStateFlow(text: String){

        currentJob?.let {
            it.cancel()
            _stateFlow.value = "..."
            currentJob = null
        } ?: run {
            var count = 0
            currentJob = viewModelScope.launch {
                while(true) {
                    _stateFlow.value = "$text $count"
                    count++
                    delay(500)
                }
            }
        }
    }

    fun triggerFlow(text: String): Flow<String> {
        return flow {
            var count = 0
            while(true) {
                emit("$text $count")
                count++
                delay(500)
            }
        }
    }

    fun triggerSharedFlow() {
        viewModelScope.launch {
            _sharedFlow.emit("Shared Flow")
        }
    }

}