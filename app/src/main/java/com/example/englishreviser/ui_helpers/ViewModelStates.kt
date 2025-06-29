package com.example.englishreviser.ui_helpers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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
            passwordValue.length < 6 -> ERR_LEN
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
    var isAddFolderDialogShown by mutableStateOf(false)
        private set

    fun showAddFolderDialog(){
        isAddFolderDialogShown = true
    }

    fun dismissAddFolderDialog(){
        isAddFolderDialogShown = false
    }

    var isAddCardDialogShown by mutableStateOf(false)
        private set

    fun showAddCardDialog(){
        isAddCardDialogShown = true
    }

    fun dismissAddCardDialog(){
        isAddCardDialogShown = false
    }

    //camera
    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap){
        _bitmaps.value += bitmap
    }

    //user photo
    private val _bitmapState = mutableStateOf<Bitmap?>(null)
    var bitmapState : State<Bitmap?> = _bitmapState

    fun loadUserPhoto(context: Context){
        try {
            val fin = File(context.filesDir ,"userPhotos.txt")
            _bitmapState.value = BitmapFactory.decodeFile(fin.absolutePath)

        }catch (e: IOException){
            Log.d("CAMERA2", "can't load the image $e")
        }
    }

    fun saveUserPhoto(context: Context, bitmap: Bitmap){
        try {
            val fos: FileOutputStream = context.openFileOutput("userPhotos.txt", MODE_PRIVATE)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.flush()
            fos.close()
        }catch (e: okio.IOException){
            Log.d("CAMERA", "could not save the photo $e")
        }
    }

    //permissions

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissPermissionDialog(){
        visiblePermissionDialogQueue.removeAt(0)
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ){
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)){
            visiblePermissionDialogQueue.add(permission)
        }
    }
}