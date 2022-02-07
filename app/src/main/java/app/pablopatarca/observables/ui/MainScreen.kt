package app.pablopatarca.observables.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.pablopatarca.observables.ui.theme.ObservablesTheme

@Composable
fun MainScreen(
    state: State<String>,
    buttonName: String,
    onClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp, 4.dp, 32.dp, 4.dp)
    ) {

        Text(
            text = state.value,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            textAlign = Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onClick) {
            Text(
                text = buttonName,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ObservablesTheme {
        MainScreen(
            remember{mutableStateOf("...")},
            "Button"
        ) {

        }
    }
}