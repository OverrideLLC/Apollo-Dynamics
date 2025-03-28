package com.feature.desktop.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shared.ui.BackgroundAnimated
import com.shared.utils.routes.RoutesHome
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import com.shared.resources.Res
import com.shared.resources.*
import com.shared.ui.TextFieldCustom

@Composable
fun HomeScreen(
    navigationHome: @Composable (navController: NavHostController) -> Unit
) = Screen(
    navigationHome = navigationHome
)

@Composable
private fun Screen(
    navigationHome: @Composable (navController: NavHostController) -> Unit
) {
    val navController = rememberNavController()
    Scaffold(
        floatingActionButton = { FloatingAction() },
        content = { navigationHome(navController) },
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController) },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = BackgroundAnimated(
                    colorBackground = colorScheme.background,
                    colorAnimated = colorScheme.primary.copy(alpha = 0.7f)
                )
            )
    )
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val hover = remember { MutableInteractionSource() }
    val isHovered by hover.collectIsHoveredAsState()
    var selectedIndex by remember { mutableStateOf(2) }
    val width by animateDpAsState(
        targetValue = if (isHovered) 300.dp else 70.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val icons = listOf(
        Pair(
            RoutesHome.Settings.route,
            Res.drawable.settings_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
        ),
        Pair(RoutesHome.Ai.route, Res.drawable.chat_bubble_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
        Pair(
            RoutesHome.Dashboard.route,
            Res.drawable.dashboard_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
        ),
        Pair(RoutesHome.Profile.route, Res.drawable.person_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
        Pair(
            RoutesHome.AddBusiness.route,
            Res.drawable.add_business_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .width(width)
                .background(
                    color = colorScheme.onBackground.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(16.dp)
                .hoverable(
                    interactionSource = hover,
                    enabled = true
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isHovered) {
                icons.forEachIndexed { index, pair ->
                    ButtonBottomBar(
                        icon = pair.second,
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                            navController.navigate(pair.first)
                        }
                    )
                }
            } else {
                icons.indexOf(icons[selectedIndex]).let {
                    ButtonBottomBar(
                        icon = icons[it].second,
                        selected = true,
                        onClick = {
                            selectedIndex = it
                            navController.navigate(icons[it].first)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonBottomBar(
    icon: DrawableResource,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val size by animateDpAsState(
        targetValue = if (isHovered) 50.dp else 40.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .size(size)
            .hoverable(interactionSource = interactionSource)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = if (selected) colorScheme.primary else colorScheme.tertiary
        )
    }
}

@Composable
private fun TopBar() {
    val hover = remember { MutableInteractionSource() }
    val isHovered by hover.collectIsHoveredAsState()
    var isSearching by remember { mutableStateOf(false) }
    var paddingValues by remember { mutableStateOf(16.dp) }
    var alignment by remember { mutableStateOf(Alignment.TopCenter) }
    var searchText by remember { mutableStateOf("") }
    val width by animateDpAsState(
        targetValue = if (isHovered) if (isSearching) 70.dp else 400.dp else 70.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = alignment,
        content = {
            Row(
                modifier = Modifier
                    .height(70.dp)
                    .width(width)
                    .hoverable(
                        interactionSource = hover,
                        enabled = true
                    )
                    .background(
                        color = colorScheme.onBackground.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(32.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isSearching) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                    LaunchedEffect(Unit) {
                        if (isSearching) {
                            paddingValues = 0.dp
                            alignment = Alignment.Center
                            delay(2000)
                            paddingValues = 16.dp
                            alignment = Alignment.TopCenter
                            isSearching = !isSearching
                        }
                    }
                } else if (isHovered) {
                    Icon(
                        painter = painterResource(Res.drawable.search_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { isSearching = !isSearching }
                            .size(50.dp),
                        tint = colorScheme.tertiary
                    )
                    TextFieldCustom(
                        value = searchText,
                        onValueChange = { searchText = it },
                        onDone = { isSearching = !isSearching },
                        onGo = { isSearching = !isSearching },
                        onSearch = { isSearching = !isSearching },
                        onSend = { isSearching = !isSearching },
                        keyboardType = KeyboardType.Text,
                        placeholder = "Search",
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.search_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = colorScheme.tertiary
                    )
                }
            }
        }
    )
}

@Composable
private fun FloatingAction() {
    val hover = remember { MutableInteractionSource() }
    val isHovered by hover.collectIsHoveredAsState()
    val size by animateDpAsState(
        targetValue = if (isHovered) 200.dp else 70.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val iconsList = listOf(
        Res.drawable.edit_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
        Res.drawable.widgets_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24,
        Res.drawable.dark_mode_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
    )
    var indexSelected by remember { mutableStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
            .offset(
                x = 0.dp,
                y = 100.dp
            ),
        contentAlignment = Alignment.CenterEnd,
        content = {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(size)
                    .background(
                        color = colorScheme.onBackground.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(16.dp)
                    .hoverable(
                        interactionSource = hover,
                        enabled = true
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    if (isHovered) {
                        iconsList.forEachIndexed { index, drawableResource ->
                            Icon(
                                painter = painterResource(drawableResource),
                                tint = colorScheme.tertiary,
                                modifier = Modifier
                                    .clickable { indexSelected = index }
                                    .size(40.dp),
                                contentDescription = null,
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(iconsList[iconsList.indexOf(iconsList[indexSelected])]),
                            tint = colorScheme.tertiary,
                            modifier = Modifier
                                .clickable { }
                                .size(40.dp),
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    )
}
