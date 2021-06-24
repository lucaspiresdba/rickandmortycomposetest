package br.com.lucaspires.rickandmortycomposetest.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import br.com.lucaspires.rickandmortycomposetest.data.model.SimpleCharacterModel
import br.com.lucaspires.rickandmortycomposetest.presentation.viewmodel.MainViewModel
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun CreatePersonScreen(viewModel: MainViewModel) {
    val text = remember { mutableStateOf(TextFieldValue("")) }
    val personList by viewModel.personList.observeAsState()
    val noContent by viewModel.noContent.observeAsState(initial = false)
    val loading by viewModel.loadingPerson.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        BasicTextField(
            value = text.value,
            onValueChange = {
                viewModel.getCharacterByName(it.text)
                text.value = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .background(Color.Black, RoundedCornerShape(25))
                .border(width = 1.dp, color = Color.Green, RoundedCornerShape(25))
                .padding(8.dp),
            cursorBrush = Brush.verticalGradient(listOf(Color.Transparent, Color.Green, Color.Transparent)),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.White)
        )

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 50.dp)
                    .size(75.dp),
                color = Color.Green
            )
        } else {
            if (noContent) {
                Text(
                    text = "Personagem não encontrado",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 50.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                LazyColumn {
                    personList?.let {
                        items(it) { char ->
                            CharacterRow(character = char)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateAllCharactersScreen(viewModel: MainViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ListOfCharacters(viewModel)
    }
}

@Composable
private fun ListOfCharacters(viewModel: MainViewModel) {
    val lazyPaging: LazyPagingItems<SimpleCharacterModel> =
        viewModel.getCharacters()
            .collectAsLazyPagingItems()

    LazyColumn {
        items(lazyPaging) { char ->
            char?.let { CharacterRow(character = it) }
        }
    }
}

@Composable
private fun CharacterRow(character: SimpleCharacterModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                Color.Green,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clip(RoundedCornerShape(6.dp))
                    .padding(8.dp)

            ) {
                Image(
                    painter = rememberCoilPainter(
                        request = character.imageUrl
                    ), contentDescription = "",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .height(100.dp)
                        .width(100.dp)

                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                TextSpannabled("Name", character.name)
                TextSpannabled("Specie", character.specie)
                TextSpannabled("Origin", character.origin)
                TextSpannabled("Location", character.location)
            }
        }
    }

}

@Composable
private fun TextSpannabled(firstText: String, secondText: String) {
    Text(buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(firstText.plus(": "))
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
            append(secondText)
        }
    })
}

