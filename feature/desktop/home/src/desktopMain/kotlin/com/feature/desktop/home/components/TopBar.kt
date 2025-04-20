package com.feature.desktop.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.datas.Services
import com.shared.resources.Google_Drive_logo
import com.shared.resources.Res
import com.shared.resources.google_classroom
import com.shared.resources.moodle_logo
import com.shared.utils.routes.RouteServices
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun TopBar(
    listServices: List<Services> = listOf(
        Services(
            name = "ClassRoom",
            description = "Open ClassRoom",
            icon = Res.drawable.google_classroom,
            route = RouteServices.ClassRoom
        ),
        Services(
            name = "Moodle",
            description = "Open Moodle",
            icon = Res.drawable.moodle_logo,
            route = RouteServices.Moodle
        ),
        Services(
            name = "Drive",
            description = "Open Drive",
            icon = Res.drawable.Google_Drive_logo,
            route = RouteServices.Drive
        ),
        Services(
            name = "ClassRoom",
            description = "Open ClassRoom",
            icon = Res.drawable.google_classroom,
            route = RouteServices.ClassRoom
        ),
        Services(
            name = "Moodle",
            description = "Open Moodle",
            icon = Res.drawable.moodle_logo,
            route = RouteServices.Moodle
        ),
        Services(
            name = "Drive",
            description = "Open Drive",
            icon = Res.drawable.Google_Drive_logo,
            route = RouteServices.Drive
        ),
        Services(
            name = "ClassRoom",
            description = "Open ClassRoom",
            icon = Res.drawable.google_classroom,
            route = RouteServices.ClassRoom
        ),
        Services(
            name = "Moodle",
            description = "Open Moodle",
            icon = Res.drawable.moodle_logo,
            route = RouteServices.Moodle
        ),
        Services(
            name = "Drive",
            description = "Open Drive",
            icon = Res.drawable.Google_Drive_logo,
            route = RouteServices.Drive
        ),
        Services(
            name = "ClassRoom",
            description = "Open ClassRoom",
            icon = Res.drawable.google_classroom,
            route = RouteServices.ClassRoom
        ),
        Services(
            name = "Moodle",
            description = "Open Moodle",
            icon = Res.drawable.moodle_logo,
            route = RouteServices.Moodle
        ),
        Services(
            name = "Drive",
            description = "Open Drive",
            icon = Res.drawable.Google_Drive_logo,
            route = RouteServices.Drive
        ),
    )
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(70.dp),
        contentAlignment = Alignment.Center,
        content = {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height (70.dp)
                    .background(
                        color = colorScheme.onBackground.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                items(listServices) {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                color = colorScheme.background.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Image(
                            painter = painterResource(it.icon),
                            contentDescription = it.description,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                }
            }
        }
    )
}
