//package com.example.englishreviser.helpers
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.ImageProxy
//import androidx.camera.view.CameraController
//import androidx.camera.view.LifecycleCameraController
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Cameraswitch
//import androidx.compose.material.icons.filled.Photo
//import androidx.compose.material.icons.filled.PhotoCamera
//import androidx.compose.material3.BottomSheetScaffold
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.rememberBottomSheetScaffoldState
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.englishreviser.helpers.ui.theme.EnglishReviserTheme
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
//                val scaffoldState = rememberBottomSheetScaffoldState()
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
//
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
//
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
//
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
//        onPhotoTaken: (Bitmap) -> Unit
//    ){
//        controller.takePicture(
//            ContextCompat.getMainExecutor(applicationContext),
//            object : ImageCapture.OnImageCapturedCallback(){
//                override fun onCaptureSuccess(image: ImageProxy) {
//                    super.onCaptureSuccess(image)
//                    onPhotoTaken(image.toBitmap())
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
//    private fun hasRequiredPermission(): Boolean{
//        return CAMERAX_PERMISSIONS.all{
//            ContextCompat.checkSelfPermission(
//                applicationContext,
//                it
//            ) == PackageManager.PERMISSION_GRANTED
//        }
//    }
//
//    companion object{
//        private val CAMERAX_PERMISSIONS = arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO
//        )
//    }
//}
