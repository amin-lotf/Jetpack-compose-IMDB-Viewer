package com.example.imdbviewer.ui.mainscreen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.ui.focus
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp



@ExperimentalFocus
@Composable
fun SearchInputText(
    text:String,
    onTextChanged: (String)->Unit,
    modifier: Modifier =Modifier,
    onImeAction:()->Unit={}
) {
    val focusRequester= remember { FocusRequester() }
    TextField(
        value = text,
        onValueChange = onTextChanged,
        backgroundColor = Color.Transparent,
        placeholder = { Text("Search...") },
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Go),
        onImeActionPerformed = {action,keyboardController->
            if (action==ImeAction.Go){
                onImeAction()
                keyboardController?.hideSoftwareKeyboard()
            }
        },
        textStyle =  MaterialTheme.typography.subtitle2,
        modifier = modifier.fillMaxWidth().fillMaxHeight().fillMaxHeight(.8f).focusRequester(focusRequester)
    )
    onActive{
        focusRequester.requestFocus()
    }
}

@Composable
fun SearchButton(
    onClick: (Boolean) -> Unit,
    inSearchMode:Boolean,
    modifier: Modifier=Modifier
)= IconButton(onClick = {onClick(!inSearchMode)}, modifier = modifier) {
    if(inSearchMode){
        Icon(Icons.Default.Close)
    }else{
        Icon(Icons.Default.Search)
    }
}

