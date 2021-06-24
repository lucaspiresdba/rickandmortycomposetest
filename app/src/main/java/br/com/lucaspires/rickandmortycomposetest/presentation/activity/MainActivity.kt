package br.com.lucaspires.rickandmortycomposetest.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.lucaspires.rickandmortycomposetest.R
import br.com.lucaspires.rickandmortycomposetest.presentation.screen.CreateAllCharactersScreen
import br.com.lucaspires.rickandmortycomposetest.presentation.screen.CreatePersonScreen
import br.com.lucaspires.rickandmortycomposetest.presentation.theme.RickAndMortyComposeTestTheme
import br.com.lucaspires.rickandmortycomposetest.presentation.viewmodel.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            RickAndMortyComposeTestTheme(darkTheme = true) {
                Scaffold(bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        listOf(Screen.AllCharacters, Screen.SearchCharacter).forEach { screen ->
                            BottomNavigationItem(selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                icon = { Icon(screen.bottomIcon, contentDescription = null) },
                                label = { Text(stringResource(id = screen.screenName)) },
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
                ) { innerPadding ->
                    SystemUi()
                    NavHost(
                        navController,
                        startDestination = Screen.AllCharacters.route,
                        Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.AllCharacters.route) { CreateAllCharactersScreen(viewModel = viewModel) }
                        composable(Screen.SearchCharacter.route) { CreatePersonScreen(viewModel = viewModel) }
                    }
                }
            }
        }
    }

    @Composable
    private fun SystemUi() {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Black
            )
        }
    }
}

sealed class Screen(
    val route: String,
    @StringRes val screenName: Int,
    val bottomIcon: ImageVector
) {
    companion object {
        private const val ALL_CHARACTER_ROUTE = "AllCharactersRouter"
        private const val PERSON_ROUTE = "PersonRouter"
    }

    object AllCharacters :
        Screen(ALL_CHARACTER_ROUTE, R.string.bottom_navigation_all, Icons.Outlined.Home)

    object SearchCharacter :
        Screen(PERSON_ROUTE, R.string.bottom_navigation_peson, Icons.Outlined.Person)
}

