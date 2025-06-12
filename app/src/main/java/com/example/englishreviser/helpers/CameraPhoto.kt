package com.example.englishreviser.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.englishreviser.helpers.ui.theme.PhotoBottomSheetContent
import com.example.englishreviser.ui_helpers.ViewModelStates
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPhoto(


){
    val viewModel = viewModel<ViewModelStates>()

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    val bitmaps = viewModel.bitmaps.collectAsStateWithLifecycle() // TODO: watch a vid of philipp about it
    val context = LocalContext.current.applicationContext
    val navController = rememberNavController()

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            PhotoBottomSheetContent(
                bitmaps = bitmaps.value,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ){
            CameraPreview(
                controller = controller,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = {
                    controller.cameraSelector = if(controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    else CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier.offset(32.dp, 32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch camera"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand() //animates the expansion of bottomsheet, suspend function
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Photo,
                        contentDescription = "open gallery"
                    )
                }

                IconButton(
                    onClick = {
                        takePhoto(
                            controller = controller,
                            onPhotoTaken = viewModel::onTakePhoto,
                            viewModel,
                            context
                        )

                        navController.navigate("profile")
                    }
                ) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = "take photo"
                    )
                }
            }
        }
    }


}

private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    viewModelStates: ViewModelStates,
    context: Context
){
    controller.takePicture(
        ContextCompat.getMainExecutor(context), //runs enqueued tasks on the main thread, the thread is used to dispatch calls to app components
        object : ImageCapture.OnImageCapturedCallback(){ // TODO: probably onimagesavedcallback will be needed
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat()) //to not have a rotated image
                    postScale(-1f, 1f) //to not mirror the image
                }

                val rotateBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                viewModelStates.saveUserPhoto(context, rotateBitmap)
                viewModelStates.loadUserPhoto(context)

                onPhotoTaken(rotateBitmap)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("CAMERA", "Couldnt take photo", exception)
            }
        }
    )
}

//companion object{ //alternative to static keyword
//    private val CAMERAX_PERMISSIONS = arrayOf(
//        Manifest.permission.CAMERA,
//        Manifest.permission.RECORD_AUDIO
//    )
//}
//
//private fun hasRequiredPermission(): Boolean{
//    return CAMERAX_PERMISSIONS.all{
//        ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
//    }
//}
//
//class CameraPhoto : ComponentActivity() {
//    @OptIn(ExperimentalMaterial3Api::class)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        if(!hasRequiredPermission()){
//            ActivityCompat.requestPermissions(
//                this, CAMERAX_PERMISSIONS, 0 //not a real ask for permission
//            )
//        }
//
//        setContent {
//            EnglishReviserTheme {
//                val scope = rememberCoroutineScope()
//                val scaffoldState = rememberBottomSheetScaffoldState()
//                val viewModel = viewModel<ViewModelStates>()
//                val bitmaps = viewModel.bitmaps.collectAsStateWithLifecycle() // TODO: watch a vid of philipp about it
//                val context = LocalContext.current.applicationContext
//                val navController = rememberNavController()
//
//                val controller = remember {
//                    LifecycleCameraController(applicationContext).apply {
//                        setEnabledUseCases(
//                            CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
//                        )
//                    }
//                }
//
//                BottomSheetScaffold(
//                    scaffoldState = scaffoldState,
//                    sheetPeekHeight = 0.dp,
//                    sheetContent = {
//                        PhotoBottomSheetContent(
//                            bitmaps = bitmaps.value,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                ) { padding ->
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(padding)
//                    ){
//                        CameraPreview(
//                            controller = controller,
//                            modifier = Modifier.fillMaxSize()
//                        )
//
//                        IconButton(
//                            onClick = {
//                                controller.cameraSelector = if(controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
//                                                                CameraSelector.DEFAULT_FRONT_CAMERA
//                                                            else CameraSelector.DEFAULT_BACK_CAMERA
//                            },
//                            modifier = Modifier.offset(32.dp, 32.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Cameraswitch,
//                                contentDescription = "Switch camera"
//                            )
//                        }
//
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .align(Alignment.BottomCenter)
//                                .padding(16.dp),
//                            horizontalArrangement = Arrangement.SpaceAround
//                        ) {
//                            IconButton(
//                                onClick = {
//                                    scope.launch {
//                                        scaffoldState.bottomSheetState.expand() //animates the expansion of bottomsheet, suspend function
//                                    }
//                                }
//                            ) {
//                                Icon(
//                                    Icons.Default.Photo,
//                                    contentDescription = "open gallery"
//                                )
//                            }
//
//                            IconButton(
//                                onClick = {
//                                    takePhoto(
//                                        controller = controller,
//                                        onPhotoTaken = viewModel::onTakePhoto,
//                                        viewModel,
//                                        context
//                                    )
//
//                                    navController.navigate("profile")
//                                }
//                            ) {
//                                Icon(
//                                    Icons.Default.PhotoCamera,
//                                    contentDescription = "take photo"
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun takePhoto(
//        controller: LifecycleCameraController,
//        onPhotoTaken: (Bitmap) -> Unit,
//        viewModelStates: ViewModelStates,
//        context: Context
//    ){
//        controller.takePicture(
//            ContextCompat.getMainExecutor(applicationContext), //runs enqueued tasks on the main thread, the thread is used to dispatch calls to app components
//            object : ImageCapture.OnImageCapturedCallback(){ // TODO: probably onimagesavedcallback will be needed
//                override fun onCaptureSuccess(image: ImageProxy) {
//                    super.onCaptureSuccess(image)
//
//                    val matrix = Matrix().apply {
//                        postRotate(image.imageInfo.rotationDegrees.toFloat()) //to not have a rotated image
//                        postScale(-1f, 1f) //to not mirror the image
//                    }
//
//                    val rotateBitmap = Bitmap.createBitmap(
//                        image.toBitmap(),
//                        0,
//                        0,
//                        image.width,
//                        image.height,
//                        matrix,
//                        true
//                    )
//
//                    viewModelStates.saveUserPhoto(context, rotateBitmap)
//                    viewModelStates.loadUserPhoto(context)
//
//                    onPhotoTaken(rotateBitmap)
//                    image.close()
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    super.onError(exception)
//                    Log.e("CAMERA", "Couldnt take photo", exception)
//                }
//            }
//        )
//    }
//
//    companion object{ //alternative to static keyword
//        private val CAMERAX_PERMISSIONS = arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO
//        )
//    }
//
//    private fun hasRequiredPermission(): Boolean{
//        return CAMERAX_PERMISSIONS.all{
//            ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
//        }
//    }
//}
