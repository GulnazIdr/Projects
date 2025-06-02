@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.englishreviser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishreviser.helpers.DataStoreManager
import com.example.englishreviser.room.ActionEvent
import com.example.englishreviser.room.ActionViewModel
import com.example.englishreviser.room.ActionViewModelFactory
import com.example.englishreviser.room.FolderInfoEntity
import com.example.englishreviser.room.UsersDatabase
import com.example.englishreviser.ui.theme.EnglishReviserTheme
import com.example.englishreviser.ui_helpers.ViewModelStates
import kotlinx.coroutines.launch

class NavigationDrawer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewmodel: ViewModelStates by viewModels()
        val dataStoreManager = DataStoreManager(this)

        val application = requireNotNull(this).application

        val folderDao = UsersDatabase.getDatabase(application).folderDao()
        val cardDao = UsersDatabase.getDatabase(application).cardDao()

        val viewModelFactory = ActionViewModelFactory(folderDao, cardDao)
        val dbViewModel = ViewModelProvider(this, viewModelFactory)[ActionViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            var currentUser = dataStoreManager.getCurrentUser.collectAsState("developer").value
            var folderList = folderDao.getFoldersByUser(currentUser).collectAsState(null).value

            EnglishReviserTheme {
                    NavigationDrawerApp(dbViewModel, folderList, viewmodel)
            }
        }
    }
}

@Composable
fun NavigationDrawerApp(
    dbViewModel: ActionViewModel,
    listOfFolders: List<FolderInfoEntity>?,
    viewmodel: ViewModelStates
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(navController, drawerState)
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.topAppBarColors(Color.White),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {viewmodel.showAddDialog()},
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 80.dp)
                ){
                    Icon(Icons.Filled.Add, contentDescription = "Add item")
                }

                if(viewmodel.isAddDialogShown)
                    DialogAddFolder(dbViewModel, onDismiss = {viewmodel.dismissAddDialog()})

            }

        ){ paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)){
                NavigationGraph(navController, listOfFolders)
            }
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavHostController,
    drawerState: DrawerState
){
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        val scope = rememberCoroutineScope()

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            listOf("Home", "Profile", "Settings").forEach { screen ->
                Text(
                    text = screen,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(screen.lowercase())
                            scope.launch { drawerState.close() }
                        }
                        .padding(12.dp) //padding between items
                )
            }
            IconButton(onClick = {

            }) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)) { // TODO: make it look normal
                    Icon(Icons.Filled.Logout, contentDescription = "Log out")
                    Text(text = "Log out")
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, listOfFolders: List<FolderInfoEntity>?){
    NavHost(navController, startDestination = "home"){
        composable("home") {
            LazyColumn(contentPadding = PaddingValues(4.dp)) {
                if(listOfFolders != null) {
                    items(listOfFolders.size) { index ->
                        FolderItem(index, listOfFolders)
                    }
                }
            }

        }
        composable("profile") { ScreenContent("Profile screen") }
        composable("settings") { ScreenContent("Settings screen") }
    }
}

@Composable
fun ScreenContent(text: String){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            fontSize = 24.sp
        )
    }
}

@Composable
fun FolderItem(index: Int, listOfFolders: List<FolderInfoEntity>?, modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .padding(10.dp)
            .wrapContentSize(),
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

@Composable
fun DialogAddFolder(
    dbViewModel: ActionViewModel,
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
                        dbViewModel.onEvent(ActionEvent.AddFolder(folderName))
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
