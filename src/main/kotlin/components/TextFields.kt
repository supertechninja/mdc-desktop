import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TextFields() {
    var scrollState = rememberScrollState()
    Column(modifier = Modifier.padding(10.dp).verticalScroll(scrollState)) {
        var text by remember { mutableStateOf("") }
        var leadingChecked by remember { mutableStateOf(false) }
        var trailingChecked by remember { mutableStateOf(false) }
        val characterCounterChecked by remember { mutableStateOf(false) }
        var singleLineChecked by remember { mutableStateOf(true) }
        var selectedOption by remember { mutableStateOf(Option.None) }
        var selectedTextField by remember { mutableStateOf(TextFieldType.Filled) }

        val textField: @Composable () -> Unit = @Composable {
            when (selectedTextField) {
                TextFieldType.Filled ->
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        singleLine = singleLineChecked,
                        label = {
                            val label =
                                "Label" + if (selectedOption == Option.Error) "*" else ""
                            Text(text = label)
                        },
                        leadingIcon = {
                            if (leadingChecked) Icon(
                                imageVector = Icons.Filled.Favorite, contentDescription = "Favorite"
                            )
                        },
                        trailingIcon = {
                            if (trailingChecked) Icon(
                                imageVector =
                                Icons.Filled.Info, contentDescription = ""
                            )
                        },
                        isError = selectedOption == Option.Error,
                        modifier = Modifier.widthIn(max = 300.dp)
                    )
                TextFieldType.Outlined ->
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
//                        singleLine = singleLineChecked,
                        label = {
                            val label =
                                "Label" + if (selectedOption == Option.Error) "*" else ""
                            Text(text = label)
                        },
                        leadingIcon = {
                            if (leadingChecked) Icon(
                                imageVector = Icons.Filled.Favorite, contentDescription = "Favorite"
                            )
                        },
                        trailingIcon = {
                            if (trailingChecked) Icon(
                                imageVector =
                                Icons.Filled.Info, contentDescription = ""
                            )
                        },
                        isError = selectedOption == Option.Error,
                        modifier = Modifier.widthIn(max = 300.dp)
                    )
            }
        }

        Box(
            Modifier.height(80.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (selectedOption == Option.None) {
                textField()
            } else {
                TextFieldWithMessage(selectedOption, textField)
            }
        }

        Column {
            Title("TextField Type")
            Column {
                TextFieldType.values().map { it.name }
                    .forEach { textType ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (textType == selectedTextField.name),
                                    onClick = {
                                        selectedTextField =
                                            TextFieldType.valueOf(
                                                textType
                                            )
                                    }
                                )
                                .padding(horizontal = 16.dp)
                        ) {
                            RadioButton(
                                selected = (textType == selectedTextField.name),
                                onClick = {
                                    selectedTextField =
                                        TextFieldType.valueOf(
                                            textType
                                        )
                                },
                                modifier = Modifier.align(
                                    Alignment.CenterVertically
                                )
                            )
                            Text(
                                text = textType,
                                style = MaterialTheme.typography.body1.merge(),
                                modifier = Modifier.padding(start = 16.dp).align(
                                    Alignment.CenterVertically
                                )
                            )
                        }
                    }
            }

            Title("Options")
            OptionRow(
                title = "Leading icon",
                checked = leadingChecked,
                onCheckedChange = { leadingChecked = it }
            )
            OptionRow(
                title = "Trailing icon",
                checked = trailingChecked,
                onCheckedChange = { trailingChecked = it }
            )
            OptionRow(
                title = "Single line",
                checked = singleLineChecked,
                onCheckedChange = { singleLineChecked = it }
            )
            OptionRow(
                title = "Character counter (TODO)",
                checked = characterCounterChecked,
                enabled = false,
                onCheckedChange = { /* TODO */ }
            )

            Spacer(Modifier.height(16.dp))

            Column {
                Option.values().map { it.name }.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption.name),
                                onClick = {
                                    selectedOption =
                                        Option.valueOf(text)
                                }
                            )
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = (text == selectedOption.name),
                            onClick = {
                                selectedOption =
                                    Option.valueOf(text)
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.body1.merge(),
                            modifier = Modifier.padding(start = 16.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.Title(title: String) {
    Spacer(Modifier.height(16.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    )
    Spacer(Modifier.height(16.dp))
}

@Composable
fun OptionRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

/**
 * Text field with helper or error message below.
 */
@Composable
private fun TextFieldWithMessage(
    helperMessageOption: Option,
    content: @Composable () -> Unit
) {
    val typography = MaterialTheme.typography.caption
    val color = when (helperMessageOption) {
        Option.Helper -> {
            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
        }
        Option.Error -> MaterialTheme.colors.error
        else -> Color.Unspecified
    }

    Column {
        Box(modifier = Modifier.weight(1f, fill = false)) { content() }
        Text(
            text = "Helper message",
            color = color,
            style = typography,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

/**
 * Helper message option
 */
private enum class Option { None, Helper, Error }
private enum class TextFieldType { Filled, Outlined }
