package com.example.mytodoapp.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytodoapp.R
import com.example.mytodoapp.ui.theme.TodoGray
import com.example.mytodoapp.ui.theme.TodoHomeRed
import com.example.mytodoapp.ui.theme.TodoWhite

@Composable
fun HomeBottomBar(
    modifier: Modifier = Modifier
) {
    val items = listOf(
        "Home" to R.drawable.ic_home,
        "Calendar" to R.drawable.ic_calendar,
        "Focus" to R.drawable.ic_focus,
        "Overdue" to R.drawable.ic_overdue
    )

    NavigationBar(
        modifier = modifier,
        containerColor = TodoWhite,
        tonalElevation = 0.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == 0,
                enabled = index == 0,
                onClick = {
                    // Đang ở Home nên chưa cần chuyển màn hình.
                },
                icon = {
                    Icon(
                        painter = painterResource(item.second),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.first,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TodoHomeRed,
                    selectedTextColor = TodoHomeRed,
                    indicatorColor = TodoWhite,
                    unselectedIconColor = TodoGray,
                    unselectedTextColor = TodoGray,
                    disabledIconColor = TodoGray,
                    disabledTextColor = TodoGray
                )
            )
        }
    }
}