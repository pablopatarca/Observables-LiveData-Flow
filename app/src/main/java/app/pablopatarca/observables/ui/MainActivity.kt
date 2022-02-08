package app.pablopatarca.observables.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.pablopatarca.observables.R
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
                val scope = rememberCoroutineScope()
                Scaffold(
                    scaffoldState = scaffoldState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(id = R.string.app_name),
                            modifier = Modifier.fillMaxWidth()
                                .padding(16.dp),
                            style = MaterialTheme.typography.h3,
                            textAlign = TextAlign.Center
                        )

                        LabelButtonScreenComponent(
                            labelState = liveDataMutableState,
                            buttonName = "Live Data"
                        ){
                            viewModel.triggerLiveData("Live Data")
                        }

                        LabelButtonScreenComponent(
                            labelState = stateFlowMutableState,
                            buttonName = "State Flow"
                        ){
                            viewModel.triggerStateFlow()
                        }

                        LabelButtonScreenComponent(
                            labelState = flowMutableState,
                            buttonName = "Flow"
                        ){
                            scope.launch {
                                viewModel.triggerFlow("Flow").collectLatest {
                                    flowMutableState.value = it
                                }
                            }
                        }
                        LabelButtonScreenComponent(
                            labelState = sharedFlowMutableState,
                            buttonName = "Shared Flow"
                        ){
                            viewModel.triggerSharedFlow()
                        }
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
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
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