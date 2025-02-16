package com.mrbaikal.nesineandroidcase.ui.compose.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mrbaikal.nesineandroidcase.R
import com.mrbaikal.nesineandroidcase.ui.compose.MainComposeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    activityViewModel: MainComposeViewModel,
    viewModel: DetailViewModel = hiltViewModel(),
    navigateToBack: () -> Unit
) {
    val postModel by activityViewModel.selectedPost.collectAsStateWithLifecycle()
    val editMode by viewModel.editMode.collectAsStateWithLifecycle()
    var titleState by remember(postModel) { mutableStateOf(postModel?.title.orEmpty()) }
    var bodyState by remember(postModel) { mutableStateOf(postModel?.body.orEmpty()) }

    val focusManager = LocalFocusManager.current
    val (titleFocusRequester, bodyFocusRequester) = remember { FocusRequester.createRefs() }

    fun sendPostModel() {
        activityViewModel.editPost(titleState, bodyState)
        viewModel.setEditMode(false)
        focusManager.clearFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_detail),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.blue)),
                navigationIcon = {
                    IconButton(
                        onClick = navigateToBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            postModel?.let {
                AsyncImage(
                    modifier = Modifier
                        .padding(40.dp)
                        .clip(CircleShape)
                        .size(100.dp),
                    model = it.imgUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )

                if (editMode) {
                    Row(
                        modifier = Modifier.padding(top = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(titleFocusRequester),
                            value = titleState,
                            onValueChange = { value ->
                                titleState = value
                            },
                            textStyle = TextStyle.Default.copy(
                                fontSize = 16.sp
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onAny = {
                                    titleFocusRequester.freeFocus()
                                    bodyFocusRequester.requestFocus()
                                }
                            )
                        )
                        IconButton(
                            onClick = { sendPostModel() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                        }
                    }
                    TextField(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .focusRequester(bodyFocusRequester),
                        value = bodyState,
                        onValueChange = { value ->
                            bodyState = value
                        },
                        textStyle = TextStyle.Default.copy(
                            fontSize = 16.sp
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onAny = {
                                sendPostModel()
                            }
                        )
                    )
                } else {
                    Row(
                        modifier = Modifier.padding(top = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = it.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = {
                                viewModel.setEditMode(true)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = it.body,
                        fontSize = 16.sp
                    )
                }
            }

        }
    }
}