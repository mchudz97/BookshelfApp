package com.example.bookshelfapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelfapp.ui.screens.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp(){
    val bookshelfViewModel: BookshelfViewModel = viewModel( factory = BookshelfViewModel.Factory)
    MainScreen(
        uiState = bookshelfViewModel.bookshelfUiState,
        phase = bookshelfViewModel.getPhase(),
        searchAction = bookshelfViewModel::getBooks,
        phaseChangeAction = bookshelfViewModel::updatePhase,
        loadData = bookshelfViewModel::getBooks,
        modifier = Modifier.fillMaxSize()
    )
}
