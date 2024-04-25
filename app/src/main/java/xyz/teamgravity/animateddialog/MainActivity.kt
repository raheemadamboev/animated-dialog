package xyz.teamgravity.animateddialog

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import xyz.teamgravity.animateddialog.ui.theme.AnimatedDialogTheme
import xyz.teamgravity.animateddialog.ui.theme.Pink
import xyz.teamgravity.animateddialog.ui.theme.Red
import xyz.teamgravity.animateddialog.ui.theme.Yellow

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimatedDialogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var visible by rememberSaveable { mutableStateOf(false) }
                    val onDismiss = remember { { visible = false } }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button(
                            onClick = {
                                visible = true
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.info)
                            )
                        }
                    }

                    AnimatedDialog(
                        visible = visible,
                        onDismiss = onDismiss
                    ) {
                        ResetWarning(
                            onDismiss = onDismiss
                        )
                    }
                }
            }
        }
    }

    @ReadOnlyComposable
    @Composable
    private fun getDialogWindow(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window

    @Composable
    private fun AnimatedDialog(
        visible: Boolean,
        onDismiss: () -> Unit,
        content: @Composable BoxScope.() -> Unit
    ) {
        var showAnimatedDialog by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(
            key1 = visible,
            block = {
                if (visible) showAnimatedDialog = true
            }
        )

        if (showAnimatedDialog) {
            Dialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                val window = getDialogWindow()

                SideEffect {
                    window?.setDimAmount(0F)
                    window?.setWindowAnimations(-1)
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    var animate by rememberSaveable { mutableStateOf(false) }

                    LaunchedEffect(
                        key1 = Unit,
                        block = {
                            animate = true
                        }
                    )

                    AnimatedVisibility(
                        visible = animate && visible,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        onDismiss()
                                    }
                                }
                                .background(Color.Black.copy(alpha = 0.56F))
                        )
                    }
                    AnimatedVisibility(
                        visible = animate && visible,
                        enter = fadeIn(
                            animationSpec = spring(
                                stiffness = Spring.StiffnessHigh
                            )
                        ) + scaleIn(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            ),
                            initialScale = 0.8F
                        ),
                        exit = slideOutVertically { it / 8 } + fadeOut() + scaleOut(
                            targetScale = 0.95F
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .width(300.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .pointerInput(Unit) {
                                    detectTapGestures { }
                                },
                            content = content
                        )

                        DisposableEffect(
                            key1 = Unit,
                            effect = {
                                onDispose {
                                    showAnimatedDialog = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ResetWarning(
        onDismiss: () -> Unit
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            var visible by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(
                key1 = Unit,
                block = {
                    visible = true
                }
            )

            AnimatedVisibility(
                visible = visible,
                enter = expandVertically(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow
                    ),
                    expandFrom = Alignment.CenterVertically
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Pink,
                                    Yellow,
                                )
                            )
                        )
                ) {
                    Image(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = null
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.nexia2_legend),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.nexia2_legend_description),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Row(
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onDismiss() }
                        .weight(1F)
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.hell_no),
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08F),
                            shape = RoundedCornerShape(10.dp)
                        )
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onDismiss() }
                        .weight(1F)
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.for_sure),
                        fontWeight = FontWeight.Bold,
                        color = Red
                    )
                }
            }
        }
    }
}