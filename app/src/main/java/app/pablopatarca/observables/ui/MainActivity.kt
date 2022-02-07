package app.pablopatarca.observables.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.pablopatarca.observables.ui.theme.ObservablesTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val liveDataMutableState = mutableStateOf("...")
    private val stateFlowMutableState = mutableStateOf("...")
    private val flowMutableState = mutableStateOf("...")
    private val sharedFlowMutableState = mutableStateOf("...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldState = rememberScaffoldState()
            ObservablesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = { scaffoldState.snackbarHostState }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp)
                        ) {

                            MainScreen(
                                state = liveDataMutableState,
                                buttonName = "Live Data"
                            ){
                                viewModel.triggerLiveData("Live Data")
                            }

                            MainScreen(
                                state = stateFlowMutableState,
                                buttonName = "State Flow"
                            ){
                                viewModel.triggerStateFlow("State Flow")
                            }

                            MainScreen(
                                state = flowMutableState,
                                buttonName = "Flow"
                            ){
                                lifecycleScope.launch {
                                    viewModel.triggerFlow("Flow").collectLatest {
                                        flowMutableState.value = it
                                    }
                                }
                            }

                            MainScreen(
                                state = sharedFlowMutableState,
                                buttonName = "Shared Flow"
                            ){
                                viewModel.triggerSharedFlow()
                            }
                        }
                    }

                    // Subscribe to the observable
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            viewModel.liveData.observe(this@MainActivity ){
                                liveDataMutableState.value = it
                            }

                            viewModel.stateFlow.collectLatest {
                                stateFlowMutableState.value = it
                            }

                            viewModel.sharedFlow.collect {
                                sharedFlowMutableState.value = it
                                scaffoldState.snackbarHostState
                                    .showSnackbar(it)
                            }
                        }
                    }

                }
            }
        }
    }
}