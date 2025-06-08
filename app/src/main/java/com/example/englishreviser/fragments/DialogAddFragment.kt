package com.example.englishreviser.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.englishreviser.room.ActionEvent
import com.example.englishreviser.room.ActionViewModel
import com.example.englishreviser.room.CardInfoEntity
import com.example.englishreviser.room.FolderInfoEntity

@Composable
fun DialogAddCard(
    folderId: Int,
    dbViewModel: ActionViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nativeWord by rememberSaveable { mutableStateOf("") }
    var translatedWord by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = {onDismiss()}){
        Card(
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = nativeWord,
                    onValueChange = {nativeWord = it},
                    modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    placeholder = {Text("Русский")}
                )

                TextField(
                    value = translatedWord,
                    onValueChange = {translatedWord = it},
                    modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    placeholder = {Text("English")}
                )

                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    ElevatedButton(onClick = {onDismiss()}) {
                        Text("Cancel")
                    }
                    ElevatedButton(onClick = {
                        dbViewModel.onEvent(ActionEvent.AddCard(CardInfoEntity(
                            nativeWord = nativeWord,
                            translatedWord = translatedWord,
                            folderId = folderId,
                        )))
                        dbViewModel.onEvent(ActionEvent.SaveCard)
                        onDismiss()
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun DialogAddFolder(
    dbViewModel: ActionViewModel,
    currentUser: String,
    onDismiss:() -> Unit,
    modifier: Modifier = Modifier
){
    var folderName by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = {onDismiss()}) {
        Card(
            modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                TextField(
                    value = folderName,
                    onValueChange = { folderName = it},
                    modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                    placeholder = { Text("Folder name") }
                )
                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    ElevatedButton(onClick = {onDismiss()}) {
                        Text("Cancel")
                    }
                    ElevatedButton(onClick = {
                        dbViewModel.onEvent(ActionEvent.AddFolder(
                            FolderInfoEntity(folderName = folderName, userName =  currentUser))
                        )
                        dbViewModel.onEvent(ActionEvent.SaveFolder)
                        onDismiss()
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}