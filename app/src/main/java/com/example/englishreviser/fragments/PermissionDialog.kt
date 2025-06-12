package com.example.englishreviser.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialogRationale(
    permissionTextProvider: PermissionTextProvider,
    isPermanentDeclined: Boolean,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    onGoToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier.widthIn(max = 300.dp).wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = modifier.fillMaxWidth().padding(24.dp)
            ) {
                Text(
                    text = "Permission required",
                    fontWeight = FontWeight.Medium,
                    modifier = modifier.padding(bottom = 7.dp)
                )
                Text(text = permissionTextProvider.getDescr(isPermanentDeclined))
                Spacer(modifier = modifier.height(20.dp))
                Text(
                    text = if (isPermanentDeclined) "Grant permission"
                           else "Okay",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxWidth().clickable {
                        if (isPermanentDeclined) onGoToSettings()
                        else onAccept()
                    }
                )
            }
        }
    }
}

interface PermissionTextProvider{
    fun getDescr(isPermanentDeclined: Boolean): String
}

class CameraPermissonTextProvider: PermissionTextProvider{
    override fun getDescr(isPermanentDeclined: Boolean): String {
        return if(isPermanentDeclined)
            "It seems you declined camera permission. " +
                    "You can go to the app settings to grant it"
               else "This app needs an access to your camera in order to" +
                "set a profile photo to your account"
    }
}