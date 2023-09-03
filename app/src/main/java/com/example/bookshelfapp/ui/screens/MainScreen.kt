package com.example.bookshelfapp.ui.screens

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelfapp.R
import com.example.bookshelfapp.model.Book
import com.example.bookshelfapp.model.ImageLinks
import com.example.bookshelfapp.model.VolumeInfo
import com.example.bookshelfapp.ui.BookshelfUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: BookshelfUiState,
    phase: String,
    searchAction: () -> Unit,
    phaseChangeAction: (String) -> Unit,
    loadData: () -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            SearchBar(
                phase = phase,
                onPhaseChange = phaseChangeAction,
                onPhaseEntered = loadData
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ){
            when(uiState){
                is BookshelfUiState.Start -> StartScreen(modifier = modifier)
                is BookshelfUiState.Success -> SuccessScreen(uiState.books, modifier = modifier)
                is BookshelfUiState.Loading -> LoadingScreen(modifier = modifier)
                is BookshelfUiState.Error -> ErrorScreen(loadData, modifier = modifier)
                is BookshelfUiState.NoContent -> NoContentScreen(uiState.phase, modifier)
            }
        }
    }
}

@Composable
fun StartScreen(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.hello_message),
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
fun NoContentScreen(phase: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.no_content, phase),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ErrorScreen(onReloadClicked: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        Text(
            text = "Couldn't load data. Try clicking reload",
            textAlign = TextAlign.Center
        )
        Button(onClick = onReloadClicked) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null
            )
        }
    }
}

@Composable
fun SuccessScreen(books: List<Book>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxWidth()
    ){
        items(
            items = books, key = { book -> book.id}
        ){
            if(!it.volumeInfo.imageLinks?.thumbnail.isNullOrBlank()){
                BookCard(
                    image = it,
                    modifier = modifier
                        .fillMaxWidth()
                        .aspectRatio(0.5f)
                        .border(
                            width = 2.dp,
                            color = Color.Black
                        )
                )
            }

        }
    }
}

@Composable
fun BookCard(image: Book, modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
    ){
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(image.volumeInfo.imageLinks?.thumbnail)
                .crossfade(true)
                .build(),
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_downloading),
            error = painterResource(id = R.drawable.ic_broken_image),
            contentScale = ContentScale.FillBounds,
            modifier = modifier
        )
    }


}
@Preview
@Composable
fun LoadingScreen(
    colors: List<Color> = listOf(
        Color(0x4D82AEAD),
        Color(0xFF82AEAD),
    ),
    size: Dp = 100.dp,
    animationDuration: Int = 500,
    modifier: Modifier = Modifier
){
    val infiniteTransition = rememberInfiniteTransition(label = "1")
    val rotAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            )
        ), label = "2"
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ){
        CircularProgressIndicator(
            modifier = Modifier
                .size(size)
                .rotate(degrees = rotAnimation)
                .border(
                    width = 5.dp,
                    brush = Brush.sweepGradient(colors),
                    shape = CircleShape
                ),
            progress = 1f,
            strokeWidth = 1.dp,
            color = MaterialTheme.colorScheme.background
        )
    }

}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(phase: String, onPhaseChange: (String) -> Unit, onPhaseEntered: () -> Unit){
    Row(){
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = phase,
            singleLine = true,
            onValueChange = onPhaseChange,
            label = {Text(stringResource(id = R.string.search_label))},
            isError = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if(phase.isNotBlank())
                        onPhaseEntered()
                    keyboardController?.hide()
                }
            ),
            trailingIcon = {
                if(phase.isNotBlank()){
                    Row(){
                        IconButton(
                            onClick = {
                                onPhaseChange("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = {
                                onPhaseEntered()
                                keyboardController?.hide()
                            }
                        ){
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = null
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun GridCheck(){
    var imageLinks = ImageLinks()
    var volumeInfo = VolumeInfo(imageLinks = imageLinks)
    var book = Book(id = "xd", volumeInfo = volumeInfo)
    var list = listOf(
        Book(
            "id",
            VolumeInfo(
                imageLinks = ImageLinks(
                    "http://books.google.com/books/content?id=jazzAAAAMAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
                )
            )
        ),
        Book(
            "di",
            VolumeInfo(
                imageLinks = ImageLinks(
                    "http://books.google.com/books/content?id=lB95dxQYs8MC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                )
            )
        ),
    )
    SuccessScreen(books = list)
}

@Preview
@Composable
fun NoContentPreview(){
    val phase = "aaad".repeat(10)
    NoContentScreen(phase = phase, Modifier.fillMaxSize())
}