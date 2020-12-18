package com.example.imdbviewer.ui.mainscreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.imdbviewer.util.ItemTransitionState

@Composable
fun AppIconButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colors.onSurface,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit,
) {
    Surface(contentColor = contentColor, color = Color.Transparent) {
        IconButton(onClick = onClick, modifier = modifier.background(color = backgroundColor)) {
            Icon(icon)
        }

    }
}

@Composable
fun MainInputText(
    text: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = text,
        onValueChange = onTextChanged,
        backgroundColor = Color.Transparent,
        placeholder = { Text(placeholder) },
        maxLines = 1,

        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
        onImeActionPerformed = { action, keyboardController ->
            if (action == ImeAction.Go) {
                onImeAction()
                keyboardController?.hideSoftwareKeyboard()
            }
        },
        textStyle = MaterialTheme.typography.subtitle2,
        modifier = modifier.fillMaxHeight(.8f).focusRequester(focusRequester)
    )
    onActive {
        focusRequester.requestFocus()
    }
}

@Composable
fun SearchButton(
    onClick: (Boolean) -> Unit,
    inSearchMode: Boolean,
    modifier: Modifier = Modifier
) = IconButton(onClick = { onClick(!inSearchMode) }, modifier = modifier) {
    if (inSearchMode) {
        Icon(Icons.Default.Close)
    } else {
        Icon(Icons.Default.Search)
    }
}


@Composable
fun GridLayout(
    modifier: Modifier = Modifier,
    cols: Int = 2,
    content: @Composable () -> Unit
) {
    ScrollableColumn {
        Layout(modifier = modifier, content = content) { measurables, constraints ->

            val colMaxWidths = IntArray(cols) { 0 }
            val colHeights = IntArray(cols) { 0 }

            val placeables = measurables.mapIndexed { index, measurable ->

                val placeable = measurable.measure(
                    constraints.copy(
                        minHeight = 100,
                        maxHeight = constraints.maxHeight,
                        maxWidth = constraints.maxWidth / cols
                    )
                )

                val col = index % cols

                colHeights[col] += placeable.height
                colMaxWidths[col] = kotlin.math.max(colMaxWidths[col], placeable.width)

                placeable
            }

            var width = colMaxWidths.sumBy { it }
                .coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth))

            val height = colHeights.maxOrNull()
                ?.coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))
                ?: constraints.minHeight

            val padding = (constraints.maxWidth - width) / 2

            width += padding
            val colX = IntArray(cols) { 0 }
            colX[0] = padding
            for (i in 1 until cols) {
                colX[i] = colX[i - 1] + colMaxWidths[i - 1]
            }

            layout(width, height) {
                val colY = IntArray(cols) { 0 }

                placeables.forEachIndexed { index, placeable ->
                    val col = index % cols

                    placeable.placeRelative(
                        x = colX[col],
                        y = colY[col]
                    )

                    colY[col] += placeable.height
                }
            }
        }
    }
}

val Alpha = FloatPropKey("alpha")
val Offset = FloatPropKey("offset")

@Composable
fun getChoiceChipTransitionDefinition(
    duration: Int = 183,
    offsetPx: Float,
    reverse: Boolean = false
): TransitionDefinition<ItemTransitionState> = remember(reverse, offsetPx, duration) {
    transitionDefinition {
        state(ItemTransitionState.Visible) {
            this[Alpha] = 1f
            this[Offset] = 0f
        }
        state(ItemTransitionState.BecomingVisible) {
            this[Alpha] = 0f
            this[Offset] = if (reverse) -offsetPx else offsetPx
        }
        state(ItemTransitionState.BecomingNotVisible) {
            this[Alpha] = 0f
            this[Offset] = if (reverse) offsetPx else -offsetPx
        }

        transition(
            fromState = ItemTransitionState.BecomingVisible,
            toState = ItemTransitionState.Visible
        ) {
            Alpha using tween(
                durationMillis = duration,
                delayMillis = duration,
                easing = LinearEasing
            )
            Offset using tween(
                durationMillis = duration,
                delayMillis = duration,
                easing = LinearOutSlowInEasing
            )
        }

        transition(
            fromState = ItemTransitionState.Visible,
            toState = ItemTransitionState.BecomingNotVisible
        ) {
            Alpha using tween(
                durationMillis = 1,
                easing = LinearEasing,
                delayMillis = 1
            )
            Offset using tween(
                durationMillis = 1,
                easing = LinearOutSlowInEasing,
                delayMillis = 1
            )
        }
    }
}
