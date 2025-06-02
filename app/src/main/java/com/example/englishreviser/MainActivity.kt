package com.example.englishreviser

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.englishreviser.helpers.DataStoreManager
import com.example.englishreviser.helpers.SplashActivity
import com.example.englishreviser.room.FIELDS
import com.example.englishreviser.room.UserInfoEvent
import com.example.englishreviser.room.UserInfoViewModel
import com.example.englishreviser.room.UserInfoViewModelFactory
import com.example.englishreviser.room.UsersDatabase
import com.example.englishreviser.ui.theme.EnglishReviserTheme
import com.example.englishreviser.ui_helpers.PhoneVisualTransformation
import com.example.englishreviser.ui_helpers.ViewModelStates
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val dataStoreManager = DataStoreManager(this)
        val formValidationViewModel : ViewModelStates by viewModels()

        val application = requireNotNull(this).application

        val dao = UsersDatabase.getDatabase(application).userDao()

        val viewModelFactory = UserInfoViewModelFactory(dao)
        val viewmodel = ViewModelProvider(this, viewModelFactory)[UserInfoViewModel::class.java]

        setContent {
            EnglishReviserTheme {
                RegistrationForm(
                    viewmodel,
                    dataStoreManager,
                    formValidationViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun RegistrationForm(
    dbViewModel: UserInfoViewModel,
    dataStoreManager: DataStoreManager,
    formValidationViewModel : ViewModelStates,
    modifier: Modifier = Modifier
){
    var userName by rememberSaveable { mutableStateOf("") }
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    val isFormValid by remember { derivedStateOf {
        userName.isNotEmpty() &&
        formValidationViewModel.validatePassword() == true &&
        formValidationViewModel.passwordValue.isNotEmpty() &&
        formValidationViewModel.emailError == null &&
        formValidationViewModel.emailValue.isNotEmpty() &&
        formValidationViewModel.phoneError == null &&
        formValidationViewModel.phoneValue.isNotEmpty()
    }}

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Registration form",
            modifier = Modifier.padding(vertical = 20.dp),
            fontSize = 30.sp
        )

        OutlinedTextField(
            value = userName,
            onValueChange = {
                userName = it
                if (userName.isNotEmpty())
                    dbViewModel.onEvent(UserInfoEvent.InsertUserInfo(FIELDS.NAME, it))
            },
            label = { Text("Name") },
            isError = userName.isEmpty(),
            supportingText = { if(userName.isEmpty()) Text("Necessary field") }
        )

        UserEmailValidate(dbViewModel, formValidationViewModel)

        UserPhoneForm(dbViewModel, formValidationViewModel)

        UserPassword(dbViewModel, formValidationViewModel)

        OutlinedButton (
            onClick = {
                dbViewModel.onEvent(UserInfoEvent.SaveUserInfo)
                coroutine.launch {
                    dataStoreManager.saveCurrentUser(userName)
                }
                context.startActivity(Intent(context, SplashActivity::class.java))
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            enabled = isFormValid

        ) {
            Text("Submit")
        }

        Row(Modifier.padding(vertical = 15.dp)) {
            Text(text = "Already have an account?")
            Text(
                text = "Log in",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 5.dp).clickable(
                    onClick = { context.startActivity(Intent(context, LogInActivity::class.java)) }
                )
            )
        }

    }
}

@Composable
fun UserEmailValidate(
    dbViewModel: UserInfoViewModel,
    formValidationViewModel : ViewModelStates
){
    var validator : Boolean = if(formValidationViewModel.emailValue.isNotEmpty())
                                        formValidationViewModel.emailError!=null
                                    else false

    OutlinedTextField(
        value = formValidationViewModel.emailValue,
        onValueChange = { email ->
            formValidationViewModel.setEmail(email)
            if (formValidationViewModel.emailError == null)
                dbViewModel.onEvent(UserInfoEvent.InsertUserInfo(FIELDS.EMAIL, email))
        },
        isError = validator,
        supportingText = { if(validator) Text(formValidationViewModel.emailError.toString()) },
        label = { Text("Email") }
    )
}

@Composable
fun UserPhoneForm(
    dbViewModel: UserInfoViewModel,
    formValidationViewModel : ViewModelStates
){
    var validator : Boolean = if(formValidationViewModel.phoneValue.isNotEmpty())
                                formValidationViewModel.phoneError!=null
                             else false

    OutlinedTextField(
        value = formValidationViewModel.phoneValue,
        onValueChange = {
            formValidationViewModel.setPhone(it)
            if (formValidationViewModel.phoneError == null)
                dbViewModel.onEvent(UserInfoEvent.InsertUserInfo(FIELDS.PHONE, it))
        },
        label = { Text("Phone number") },
        visualTransformation = PhoneVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = validator,
        supportingText = { if(validator) Text(formValidationViewModel.phoneError.toString()) }
    )
}

@Composable
fun UserPassword(
    dbViewModel: UserInfoViewModel,
    formValidationViewModel : ViewModelStates
){
    var showPassword by remember {mutableStateOf(false)}
    var validator : Boolean = if(formValidationViewModel.passwordValue.isNotEmpty())
                                !formValidationViewModel.validatePassword()
                              else false

    OutlinedTextField(
        value = formValidationViewModel.passwordValue,
        onValueChange = {
            formValidationViewModel.setPassword(it)
            if (formValidationViewModel.validatePassword())
                dbViewModel.onEvent(
                    UserInfoEvent.InsertUserInfo(FIELDS.PASSWORD, it)
                )
        },
        label = { Text("Password") },
        isError = validator,
        supportingText = { if(validator) Text(formValidationViewModel.passwordError) },
        visualTransformation = if(showPassword) VisualTransformation.None
                               else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = {showPassword = !showPassword}) {
                Icon(
                    imageVector = if(showPassword) Icons.Filled.Visibility
                                  else Icons.Filled.VisibilityOff,
                    contentDescription = "Toggle password visibility"
                )
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun RegistrationPreview(){
    EnglishReviserTheme {
     //   RegistrationForm(editValue = "", setPassword = {}, isError = false, setError = "")
    }
}