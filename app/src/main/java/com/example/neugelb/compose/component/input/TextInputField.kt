package com.example.neugelb.compose.component.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.neugelb.compose.component.text.ContentText
import com.example.neugelb.compose.component.text.SecondaryText
import com.example.neugelb.compose.theme.DarkThemePreviewParamProvider
import com.example.neugelb.compose.theme.EightDp
import com.example.neugelb.compose.theme.SixteenDp
import com.example.neugelb.compose.theme.NeugelbTheme

enum class InputType(
    val keyboardType: KeyboardType,
    val keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None
) {
    Text(KeyboardType.Text, KeyboardCapitalization.Sentences),
}

@Composable
fun TextInputField(
    modifier: Modifier = Modifier,
    inputType: InputType,
    text: String = "",
    placeHolder: String? = null,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit
) {

    var isFocused by remember { mutableStateOf(false) }
    val borderColor = when {
        isFocused -> NeugelbTheme.colors.mainColor
        isError -> MaterialTheme.colors.error
        text.isBlank() -> NeugelbTheme.colors.textPlaceholder
        else -> NeugelbTheme.colors.textPrimary
    }
    val textStyle = NeugelbTheme.types.body1.copy(
        color = when {
            isError && isFocused.not() -> MaterialTheme.colors.error
            text.isBlank() -> NeugelbTheme.colors.textPlaceholder
            else -> NeugelbTheme.colors.textPrimary
        }
    )

    BasicTextField(
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fieldBorder(borderColor = borderColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    innerTextField()
                    placeHolder?.let {
                        PlaceholderHint(
                            value = if (text.isEmpty()) it else "",
                            isError = isError,
                            isFocused = isFocused
                        )
                    }
                }

            }
        },
        value = text,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it != FocusState.Inactive
                onFocusChange.invoke(isFocused)
            }
            .then(modifier),
        textStyle = textStyle,
        cursorBrush = SolidColor(NeugelbTheme.colors.textPrimary),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = inputType.keyboardType,
            capitalization = inputType.keyboardCapitalization,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        onValueChange = {
            if (inputType.keyboardType == KeyboardType.Number) {
                if (it.isDigitsOnly())
                    onValueChange(it)
            } else
                onValueChange(it)
        },
        enabled = enabled
    )
}

@Composable
fun FormInputField(
    modifier: Modifier = Modifier,
    type: InputType,
    label: String? = null,
    text: String = "",
    hintText: String? = null,
    placeHolder: String = "",
    isError: Boolean = false,
    focusRequester: FocusRequester = FocusRequester(),
    onFocusChange: (Boolean) -> Unit = {},
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true,
    onImeAction: () -> Unit = {},
    onValueChange: (String?) -> Unit
) {
    Column(modifier = modifier) {
        label?.takeIf { it.trim().isNotEmpty() }?.let {
            Spacer(Modifier.height(EightDp))
        }
        TextInputField(
            inputType = type,
            text = text,
            placeHolder = placeHolder,
            isError = isError,
            imeAction = imeAction,
            onImeAction = onImeAction,
            focusRequester = focusRequester,
            onValueChange = onValueChange,
            onFocusChange = onFocusChange,
            enabled = enabled
        )
        hintText?.takeIf { it.trim().isNotEmpty() }?.let {
            Spacer(Modifier.height(EightDp))
            SecondaryText(
                textAlign = TextAlign.Start,
                text = it,
                color = if (isError) MaterialTheme.colors.error else NeugelbTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun PlaceholderHint(value: String, isError: Boolean, isFocused: Boolean) {
    ContentText(
        text = value,
        color = if (isError && isFocused.not()) MaterialTheme.colors.error else NeugelbTheme.colors.textPlaceholder,
    )
}

fun Modifier.fieldBorder(
    borderColor: Color? = null,
    clickable: Boolean = false,
    onClick: () -> Unit = {}
) = composed {
    heightIn(56.dp)
        .background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium)
        .border(
            BorderStroke(1.dp, borderColor ?: NeugelbTheme.colors.textSecondary),
            shape = MaterialTheme.shapes.medium
        )
        .clickable(enabled = clickable, onClick = onClick)
        .padding(SixteenDp)
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "SampleInputField", showBackground = true)
@Composable
fun SampleInputField(@PreviewParameter(DarkThemePreviewParamProvider::class) isDarkTheme: Boolean) {
    NeugelbTheme(darkTheme = isDarkTheme) {
        FormInputField(
            type = InputType.Text,
            label = "I'm the label",
            text = "I'm the text"
        ) {}
    }
}
