package com.example.englishreviser.ui_helpers

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ViewModelStates : ViewModel()  {
    //password
    val ERR_LEN = "Password must have at least 6 characters!"
    val ERR_WHITESPACE = "Password must not contain whitespace!"
    val ERR_DIGIT = "Password must contain at least 1 digit!"
    val ERR_UPPER = "Password must have at least 1 uppercase letter!"
    val ERR_SPECIAL = "Password must have at least 1 of _%-=+#@"
    val regexSymbol = Regex(".*[!@#$%].*")

    var passwordValue by mutableStateOf("")
        private set

    var passwordError by mutableStateOf("")
        private set

    fun setPassword(value: String){
        passwordValue = value
    }

    fun validatePassword(): Boolean {
        passwordError =  when{
            passwordValue.length < 5 -> ERR_LEN
            !passwordValue.any {it.isDigit()} -> ERR_DIGIT
            !passwordValue.matches(regexSymbol) -> ERR_SPECIAL
            !passwordValue.any {it.isUpperCase()} -> ERR_UPPER
             passwordValue.any {it.isWhitespace()} -> ERR_WHITESPACE
            else -> return true
        }
        return false
    }

    //email
    var emailValue by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    fun setEmail(value: String){
        emailValue = value
        emailError = if(!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches())
                        "Invalid email format"
                     else null
    }

    //phone
    var phoneValue by mutableStateOf("")
        private set

    var phoneError by mutableStateOf<String?>(null)
        private set

    fun setPhone(value: String){
        val stripped = value.filter { it.isDigit() }

        phoneValue = if (phoneValue.length >= 10) stripped.substring(0..9)
                     else stripped

        phoneError = if(phoneValue.length != 10) "Invalid phone format"
                     else null
    }

    //dialog agg
    var isAddDialogShown by mutableStateOf(false)
        private set

    fun showAddDialog(){
        isAddDialogShown = true
    }

    fun dismissAddDialog(){
        isAddDialogShown = false
    }
}