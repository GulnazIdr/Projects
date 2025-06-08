package com.example.englishreviser.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.englishreviser.room.ActionViewModel
import com.example.englishreviser.room.CardDAO
import com.example.englishreviser.room.CardInfoEntity
import com.example.englishreviser.room.FolderInfoEntity

@Composable
fun CardListPage(
    cardDAO: CardDAO,
    currentFolderId: Int
){
    var listOfCards = cardDAO.getCardsByFolder(currentFolderId).collectAsState(null).value

    LazyColumn(contentPadding = PaddingValues(4.dp)) {
        if(listOfCards != null){
            items(listOfCards.size){ index ->
                CardItem(index, listOfCards)
            }
        }
    }
}

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
            Text(text = listOfCards[index]?.nativeWord ?: "", modifier = modifier.weight(1f).padding(7.dp))
            Text(text = listOfCards[index]?.translatedWord ?: "", modifier = modifier.weight(1f).padding(7.dp))
        }
    }
}

@Composable
fun FolderItem(
    index: Int,
    listOfFolders: List<FolderInfoEntity>?,
    navController: NavHostController,
    dbViewModel: ActionViewModel,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .padding(5.dp)
            .wrapContentSize()
            .clickable(onClick = {
                dbViewModel.folderId = listOfFolders?.get(index)?.folderId ?: 0
                navController.navigate("card")
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
                    text = "0",
                    fontSize = 18.sp)
                Text(text = listOfFolders?.get(index)?.folderName ?: "",
                    fontSize = 18.sp)
            }
            Button(
                onClick = {}
            ) {
                Text(
                    text = "0%",
                    fontSize = 18.sp
                )
            }
        }
    }
}