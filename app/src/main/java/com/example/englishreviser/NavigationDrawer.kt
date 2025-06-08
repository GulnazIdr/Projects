@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.englishreviser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.englishreviser.fragments.CardListPage
import com.example.englishreviser.fragments.DialogAddCard
import com.example.englishreviser.fragments.DialogAddFolder
import com.example.englishreviser.fragments.DrawerContent
import com.example.englishreviser.fragments.FolderItem
import com.example.englishreviser.fragments.ProfileScreen
import com.example.englishreviser.helpers.CameraPhoto
import com.example.englishreviser.helpers.DataStoreManager
import com.example.englishreviser.room.ActionViewModel
import com.example.englishreviser.room.ActionViewModelFactory
import com.example.englishreviser.room.CardDAO
import com.example.englishreviser.room.FolderDAO
import com.example.englishreviser.room.FolderInfoEntity
import com.example.englishreviser.room.UserInfoEntity
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

        val userDao = UsersDatabase.getDatabase(application).userDao()

        val folderDao = UsersDatabase.getDatabase(application).folderDao()
        val cardDao = UsersDatabase.getDatabase(application).cardDao()

        val viewModelFactory = ActionViewModelFactory(folderDao, cardDao)
        val dbViewModel = ViewModelProvider(this, viewModelFactory)[ActionViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            var currentUser = dataStoreManager.getCurrentUser.collectAsState("").value
            var folderList = folderDao.getFoldersByUser(currentUser).collectAsState(null).value

            val userInfo by userDao.getUserInfo(currentUser)
                .collectAsStateWithLifecycle(initialValue = null)

            EnglishReviserTheme {
                    NavigationDrawerApp(dbViewModel, folderList, cardDao, folderDao, currentUser, viewmodel, dataStoreManager, userInfo)
            }
        }
    }
}

@Composable
fun NavigationDrawerApp(
    dbViewModel: ActionViewModel,
    listOfFolders: List<FolderInfoEntity>?,
    cardDao: CardDAO,
    folderDao: FolderDAO,
    currentUser: String,
    viewmodel: ViewModelStates,
    dataStoreManager: DataStoreManager,
    userInfo: UserInfoEntity?
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var currentFolderId = dbViewModel.folderId
    var currentFolderName = folderDao.getFolderName(currentFolderId).collectAsState("").value
    var context = LocalContext.current

    ModalNavigationDrawer(
        drawerContent = { DrawerContent(navController, drawerState, dataStoreManager) },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                if(currentRoute == "home"){
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name)) },
                        colors = TopAppBarDefaults.topAppBarColors(Color.White),
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }

                else if(currentRoute == "profile"){
                    TopAppBar(
                        title = {Text("")},
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) { Icon(Icons.Default.ArrowBack, "Back") }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Box(modifier = Modifier.padding(5.dp)){
                                    IconButton(onClick = {expanded = !expanded}) {
                                        Icon(Icons.Default.MoreVert, "More setting profile")
                                    }

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = {expanded = false}
                                    ) {
                                        DropdownMenuItem(
                                            text = {Text("Edit")},
                                            leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = "edit info") },
                                            onClick = {}
                                        )
                                        DropdownMenuItem(
                                            text = {Text("Set photo")},
                                            leadingIcon = { Icon(Icons.Filled.PhotoCamera, contentDescription = "edit photo") },
                                            onClick = {
                                                context.startActivity(Intent(context, CameraPhoto::class.java))
                                            }
                                        )
                                    }
                                }
                            }

                        }
                    )
                }else if(currentRoute == "card"){
                    TopAppBar(
                        title = {Text(currentFolderName.toString())},
                        navigationIcon = {
                            IconButton(onClick = {navController.popBackStack()}) {
                                Icon(Icons.Default.ArrowBack, "Back to folders")
                            }
                        }
                    )
                }
            },

            floatingActionButton = {
                if(currentRoute == "home" || currentRoute == "card"){
                    FloatingActionButton(
                        onClick = {
                            if(currentRoute == "home")
                                viewmodel.showAddFolderDialog()
                            if(currentRoute == "card")
                                viewmodel.showAddCardDialog()
                        },
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 80.dp)
                    ){
                        Icon(Icons.Filled.Add, contentDescription = "Add item")
                    }

                    if(viewmodel.isAddFolderDialogShown)
                        DialogAddFolder(dbViewModel, currentUser, onDismiss = {viewmodel.dismissAddFolderDialog()})
                    if(viewmodel.isAddCardDialogShown)
                        DialogAddCard(currentFolderId, dbViewModel, onDismiss = {viewmodel.dismissAddCardDialog()})
                }
            }

        ){ paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)){
                NavigationGraph(navController, listOfFolders, userInfo, dbViewModel, cardDao, viewmodel)
            }
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    listOfFolders: List<FolderInfoEntity>?,
    userInfo: UserInfoEntity?,
    dbViewModel: ActionViewModel,
    cardDAO: CardDAO,
    viewmodel: ViewModelStates
){
    var folderId = dbViewModel.folderId

    NavHost(navController, startDestination = "home"){
        composable("home") {
            LazyColumn(contentPadding = PaddingValues(4.dp)) {
                if(listOfFolders != null) {
                    items(listOfFolders.size) { index ->
                        FolderItem(index, listOfFolders, navController, dbViewModel)
                    }
                }
            }
        }

        composable("settings") { ScreenContent("Settings screen") }

        composable("profile") { ProfileScreen(userInfo, viewmodel) }

        composable("card"){ CardListPage(cardDAO, folderId)}
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




