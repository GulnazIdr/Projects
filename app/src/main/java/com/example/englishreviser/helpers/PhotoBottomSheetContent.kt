//package com.example.englishreviser.helpers.ui.theme
//
//import android.graphics.Bitmap
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
//import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun PhotoBottomSheetContent(
//    bitmaps: List<Bitmap>,
//    modifier: Modifier = Modifier
//) {
//    LazyVerticalStaggeredGrid(
//        columns = StaggeredGridCells.Fixed(2),
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        verticalItemSpacing = 16.dp,
//        contentPadding = PaddingValues(16.dp),
//        modifier = modifier
//    ) {
//        items(bitmaps) { bitmap ->
//            Image(
//                bitmap = bitmap.asImageBitmap(),  // Now works!
//                contentDescription = null,
//                modifier = Modifier.clickable(onClick = {})
//            )
//        }
//    }
//}

// TODO: video 20:45  https://www.youtube.com/watch?v=12_iKwGIP64&ab_channel=PhilippLackner

