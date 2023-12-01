package com.wisky.tiku.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.wisky.tiku.viewmodel.TextBookViewModel


@Composable
internal fun WrongQuestionRoute(
    modifier: Modifier = Modifier,
    viewModel: TextBookViewModel = hiltViewModel(),
) {
//    val onboardingUiState by viewModel.onboardingUiState.collectAsStateWithLifecycle()
//    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
//    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
//    val deepLinkedUserNewsResource by viewModel.deepLinkedNewsResource.collectAsStateWithLifecycle()

    WrongQuestionScreen(
//    isSyncing = isSyncing,
//    onTopicCheckedChanged = viewModel::updateTopicSelection,
//    onDeepLinkOpened = viewModel::onDeepLinkOpened,
//    onTopicClick = onTopicClick,
//    saveFollowedTopics = viewModel::dismissOnboarding,
//    onNewsResourcesCheckedChanged = viewModel::updateNewsResourceSaved,
//    onNewsResourceViewed = { viewModel.setNewsResourceViewed(it, true) },
        modifier = modifier,
    )
}

@Composable
internal fun WrongQuestionScreen(
//    isSyncing: Boolean,
//    onTopicCheckedChanged: (String, Boolean) -> Unit,
//    onTopicClick: (String) -> Unit,
//    onDeepLinkOpened: (String) -> Unit,
//    saveFollowedTopics: () -> Unit,
//    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit,
//    onNewsResourceViewed: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
//        ComposeContent()
        Button(
            onClick = {
            },
            modifier = modifier,
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
            ),
            contentPadding = PaddingValues(
                start = 24.dp,
                top = 4.dp,
                end = 24.dp,
                bottom = 4.dp,
            ),
            content = { Text("错题本----------") },
         )
    }



}
//@Composable
//fun ComposeContent() {
//    AndroidView(factory = {
//        //这里也可以通过 layoutInflater.inflate(R.layout.xxxxxx) 的方式返回原生View
//        val mBrushGLSurfaceView = ZenBrushGLSurfaceView(it)
//        // 设置画布背景颜色
//        // 设置画布背景颜色
//        mBrushGLSurfaceView.brushRenderer.setBackgroundColor(255f, 255f, 255f)
//        // 设置画笔颜色
//        // 设置画笔颜色
//        mBrushGLSurfaceView.brushRenderer.setBrushTintColor(
//            0f, 0f, 0f, 1.0f, 0f, 0f, 0f,
//            1.0f
//        )
//        // 设置画笔类型
//        // 设置画笔类型
//        mBrushGLSurfaceView.brushRenderer.setBrushType(0)
//        mBrushGLSurfaceView.brushRenderer.setBrushAlpha(1.0f)
//        // 设置画笔大小
//        // 设置画笔大小
//        mBrushGLSurfaceView.brushRenderer.setBrushSize(3f)
//        mBrushGLSurfaceView
//    }, modifier = Modifier.fillMaxWidth(), update = {
//
//    })
//}