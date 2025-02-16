package com.mrbaikal.nesineandroidcase.ui.compose.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mrbaikal.nesineandroidcase.R
import com.mrbaikal.nesineandroidcase.base.ui.compose.BaseScreen
import com.mrbaikal.nesineandroidcase.domain.model.PostModel
import com.mrbaikal.nesineandroidcase.ui.compose.MainComposeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    activityViewModel: MainComposeViewModel,
    viewModel: ListViewModel = hiltViewModel(),
    navigateDetail: () -> Unit
) {
    val postList by viewModel.postList.collectAsStateWithLifecycle()
    val selectedPostModel by activityViewModel.selectedPost.collectAsStateWithLifecycle()

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()


    LaunchedEffect(selectedPostModel) {
        viewModel.updateItem(selectedPostModel)
    }

    BaseScreen(viewModel) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.screen_list),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.blue))
                )
            }
        ) { paddingValues ->
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                state = pullToRefreshState,
                onRefresh = {
                    viewModel.getPosts()
                    isRefreshing = false
                }
            ) {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    itemsIndexed(
                        items = postList,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        var isItemRemoved by remember(item.id) { mutableStateOf(false) }
                        val swipeToDismissState = rememberSwipeToDismissBoxState(
                            positionalThreshold = { it * 0.25f },
                            confirmValueChange = {
                                when (it) {
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        if (!isItemRemoved) {
                                            isItemRemoved = true
                                            viewModel.deleteItem(index)
                                            true
                                        } else false
                                    }
                                    SwipeToDismissBoxValue.StartToEnd -> false
                                    SwipeToDismissBoxValue.Settled -> false
                                }
                            }
                        )

                        if (!isItemRemoved) {
                            SwipeToDismissBox(
                                modifier = Modifier.animateItem(),
                                state = swipeToDismissState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Red)
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .size(40.dp)
                                                .align(Alignment.CenterEnd),
                                            imageVector = Icons.Default.Delete,
                                            tint = Color.White,
                                            contentDescription = null
                                        )
                                    }
                                },
                                content = {
                                    PostItem(
                                        modifier = Modifier.background(Color.White),
                                        postModel = item,
                                        onClick = {
                                            activityViewModel.setPostModel(item)
                                            navigateDetail()
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostItem(
    modifier: Modifier = Modifier,
    postModel: PostModel,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp),
            model = postModel.imgUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = postModel.title,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = postModel.body,
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = Color.Black.copy(0.1f)
    )
}