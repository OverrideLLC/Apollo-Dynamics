package com.feature.desktop.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shared.resources.Res
import com.shared.resources.TTNegro
import com.shared.resources.menu_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.person_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun TopBar(
    iconProfile: DrawableResource = Res.drawable.person_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(70.dp),
        contentAlignment = Alignment.Center,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.TTNegro),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TaskTec",
                            fontSize = 22.sp,
                            color = colorScheme.primary
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.size(30.dp),
                            content = {
                                Icon(
                                    painter = painterResource(Res.drawable.menu_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    tint = colorScheme.primary
                                )
                            }
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.size(30.dp).background(
                                color = colorScheme.primary,
                                shape = CircleShape
                            ),
                            content = {
                                Image(
                                    painter = painterResource(iconProfile),
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = null,
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}
