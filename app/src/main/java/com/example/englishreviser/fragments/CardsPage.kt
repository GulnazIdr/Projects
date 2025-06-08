package com.example.englishreviser.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishreviser.room.ActionViewModel
import com.example.englishreviser.room.CardInfoEntity
import com.example.englishreviser.room.FolderInfoEntity

@Composable
fun CardItem(index: Int, listOfCards: List<CardInfoEntity?>, modifier: Modifier = Modifier){
    Card(
        modifier = modifier.padding(5.dp).wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){
        Row(
            modifier = modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = listOfCards[index]?.nativeWord ?: "")
            Text(text = listOfCards[index]?.translatedWord ?: "")
        }
    }
}

@Composable
fun FolderItem(
    index: Int,
    listOfFolders: List<FolderInfoEntity>?,
    dbViewModel: ActionViewModel,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .padding(10.dp)
            .wrapContentSize()
            .clickable(onClick = {
                dbViewModel.folderId = listOfFolders?.get(index)?.folderId ?: 0
            }),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column {
                Text(
                    //text = listOfFolders[index].wordCounter.toString(),
                    text = "0",
                    fontSize = 18.sp)
                Text(text = listOfFolders?.get(index)?.folderName ?: "",
                    fontSize = 18.sp)
            }
            Button(
                onClick = {}
            ) {
                Text(
                    //text = listOfFolders.get(index).learntPercent.toString(),
                    text = "0%",
                    fontSize = 18.sp
                )
            }
        }
    }
}