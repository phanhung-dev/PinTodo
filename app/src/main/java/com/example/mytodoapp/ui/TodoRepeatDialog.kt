package com.example.mytodoapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mytodoapp.data.TodoRepeat

@Composable
fun TodoRepeatDialog(
    initialRepeat: TodoRepeat,
    onDismiss: () -> Unit,
    onConfirm: (TodoRepeat) -> Unit
) {
    var draftRepeat by rememberSaveable(initialRepeat) {
        mutableStateOf(initialRepeat)
    }

    val red = Color(0xFFE5484D)
    val darkText = Color(0xFF1F2937)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .widthIn(max = 360.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Lặp lại",
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 8.dp
                    ),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = darkText
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                ) {
                    TodoRepeat.values().forEach { repeat ->
                        val isSelected = repeat == draftRepeat

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) {
                                Color(0xFFFFF1F2)
                            } else {
                                Color.Transparent
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = isSelected,
                                        role = Role.RadioButton,
                                        onClick = {
                                            draftRepeat = repeat
                                        }
                                    )
                                    .heightIn(min = 48.dp)
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp
                                    ),
                                verticalAlignment =
                                    Alignment.CenterVertically,
                                horizontalArrangement =
                                    Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = repeat.displayName,
                                    modifier = Modifier.weight(1f),
                                    fontSize = 16.sp,
                                    color = if (isSelected) red else darkText
                                )

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = red,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.End
                    )
                ) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.heightIn(min = 48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF3F4F6),
                            contentColor = Color(0xFF4B5563)
                        )
                    ) {
                        Text("Hủy", fontSize = 16.sp)
                    }

                    Button(
                        onClick = {
                            onConfirm(draftRepeat)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.heightIn(min = 48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Xong", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}