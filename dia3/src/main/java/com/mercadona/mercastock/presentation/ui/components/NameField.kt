package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.mercadona.mercastock.dia3.R

@Composable
fun NameField(
    name: String,
    onNameChange: (String) -> Unit,
    nameError: String = "",
    focusRequester: FocusRequester? = null,
    onNext: (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = name,
        onValueChange = { newName ->
            onNameChange(newName)
        },
        label = { Text(stringResource(R.string.form_name_label)) },
        isError = nameError.isNotEmpty(),
        supportingText = {
            if (nameError.isNotEmpty()) {
                Text(
                    text = nameError,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(stringResource(R.string.form_name_hint))
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (onNext != null) ImeAction.Next else ImeAction.Default
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext?.invoke() }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .let { modifier ->
                if (focusRequester != null) {
                    modifier.focusRequester(focusRequester)
                } else {
                    modifier
                }
            }
    )
}

@Composable
@Preview
fun NameFieldPreview() {
    MaterialTheme {
        Surface {
            NameField(
                name = "Nombre",
                onNameChange = {},
                nameError = "",
                focusRequester = null,
                onNext = null
            )
        }
    }
}
