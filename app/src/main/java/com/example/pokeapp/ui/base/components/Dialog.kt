package com.example.pokeapp.ui.base.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.pokeapp.R

@Composable
fun BaseDialog(
    @StringRes title: Int,
    @StringRes subtitle: Int,
    @StringRes confirmButton: Int?,
    onConfirmClicked: () -> Unit = {},
    @StringRes dismissButton: Int?,
    onDismissClicked: () -> Unit = {}
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(stringResource(id = title))
            },
            text = {
                Text(stringResource(id = subtitle))
            },
            confirmButton = {
                if (confirmButton != null) {
                    Button(
                        onClick = {
                            openDialog.value = false
                            onConfirmClicked()
                        }) {
                        Text(
                            stringResource(id = confirmButton)
                        )
                    }
                }
            },
            dismissButton = {
                if (dismissButton != null) {
                    Button(
                        onClick = {
                            openDialog.value = false
                            onDismissClicked()
                        }) {
                        Text(
                            stringResource(id = dismissButton)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun ErrorDialog(
    @StringRes subtitle: Int,
    onDismissClicked: () -> Unit = {}
){
    BaseDialog(
        title = R.string.error_dialog_title,
        subtitle = subtitle,
        confirmButton = null,
        dismissButton = R.string.error_dialog_dismiss,
        onDismissClicked = onDismissClicked
    )
}

@Composable
fun ErrorRetryDialog(
    @StringRes subtitle: Int,
    onRetryClicked: () -> Unit = {},
    onDismissClicked: () -> Unit = {}
){
    BaseDialog(
        title = R.string.error_dialog_title,
        subtitle = subtitle,
        confirmButton = R.string.error_dialog_retry,
        dismissButton = R.string.error_dialog_dismiss,
        onConfirmClicked = onRetryClicked,
        onDismissClicked = onDismissClicked
    )
}