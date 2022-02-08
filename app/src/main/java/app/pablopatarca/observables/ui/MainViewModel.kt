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

    private val _liveData = MutableLiveData(
        "LiveData is a state holder just recommended for projects in Java, " +
                "but not to be used in projects with flow because it " +
                "doesn't persist after configuration changes"
    )
    val liveData: LiveData<String> = _liveData

    private val _stateFlow = MutableStateFlow(
        stateFlowDescription
    )
    val stateFlow: StateFlow<String> = _stateFlow.asStateFlow()

    // To emit an state once
    // Examples: Navigation, SnackBar messages, etc
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerLiveData(text: String){
        _liveData.value = text
    }

    fun triggerStateFlow(){

        val words = stateFlowDescription
            .split(" ").iterator()

        currentJob?.let {
            it.cancel()
            _stateFlow.value = "Press again to trigger the phrase"
            currentJob = null
        } ?: run {
            currentJob = viewModelScope.launch {
                var phrase = ""
                while(words.hasNext()) {
                    phrase += words.next() + " "
                    _stateFlow.value = phrase
                    delay(500)
                }
                currentJob = null
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

    companion object {
        const val stateFlowDescription = "State Flow: state holder " +
                "(hot flow: emmit values even if there are no collectors) " +
                "recommended if you need to emit " +
                "data more than once within a life cycle period."
    }
}