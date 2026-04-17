package com.example.uicardactivity.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private object PaymentColors {
    val BackgroundTop = Color(0xFF060A17)
    val BackgroundBottom = Color(0xFF010308)
    val CenterGlow = Color(0xFF1F4DA7)

    val WaveMain = Color(0xFF87B7FF)
    val WaveSecondary = Color(0xFF4A78C8)

    val CardGradientStart = Color(0xFF4C74FF)
    val CardGradientMid = Color(0xFF3A56D8)
    val CardGradientEnd = Color(0xFF6C3CE8)

    val GlassFill = Color.White.copy(alpha = 0.08f)
    val GlassBorder = Color.White.copy(alpha = 0.28f)
    val GlassGlow = Color(0xFF9BC3FF).copy(alpha = 0.14f)

    val TextPrimary = Color(0xFFF7FAFF)
    val TextSecondary = Color(0xFFD1DAEE)
    val TextHint = Color(0xFFB6C3DB)
    val QrDark = Color(0xFF0E1A2B)
}

private object PaymentDims {
    val ScreenHorizontal = 24.dp
    val TopPadding = 18.dp

    val CardWidth = 230.dp
    val CardHeight = 145.dp
    val CardCorner = 28.dp

    val ActionButtonHeight = 52.dp
    val ActionButtonCorner = 18.dp

    val QrPanelHeight = 320.dp
    val QrPanelCorner = 38.dp
    val QrSize = 214.dp
}

private object PaymentMotion {
    const val BackgroundRevealMs = 250
    const val WavesRevealMs = 350
    const val CardRevealMs = 1150

    val CardEasing = CubicBezierEasing(0.18f, 0.9f, 0.22f, 1f)
    val ContentEasing = FastOutSlowInEasing
}

/*
 * TODO(fonts): add Montserrat binaries locally (do not commit binaries through Codex PR):
 *  - app/src/main/res/font/montserrat_regular.ttf
 *  - app/src/main/res/font/montserrat_medium.ttf
 *  - app/src/main/res/font/montserrat_semibold.ttf
 *  - app/src/main/res/font/montserrat_bold.ttf
 *
 * When files are added locally, replace fallback with:
 * FontFamily(
 *   Font(R.font.montserrat_regular, FontWeight.Normal),
 *   Font(R.font.montserrat_medium, FontWeight.Medium),
 *   Font(R.font.montserrat_semibold, FontWeight.SemiBold),
 *   Font(R.font.montserrat_bold, FontWeight.Bold),
 * )
 */
private val MontserratFamily = FontFamily.SansSerif

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    amount: String = "3 999 ₽",
    onBackClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    onSpqrClick: () -> Unit = {},
    onManualInputClick: () -> Unit = {},
) {
    var showWaves by remember { mutableStateOf(false) }
    var showCard by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showAmount by remember { mutableStateOf(false) }
    var showActions by remember { mutableStateOf(false) }
    var showQr by remember { mutableStateOf(false) }
    var showBottom by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(PaymentMotion.BackgroundRevealMs.toLong())
        showWaves = true
        delay(PaymentMotion.WavesRevealMs.toLong())
        showCard = true
        delay(170)
        showTitle = true
        delay(130)
        showAmount = true
        delay(130)
        showActions = true
        delay(150)
        showQr = true
        delay(130)
        showBottom = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PaymentColors.BackgroundTop,
                        PaymentColors.BackgroundBottom,
                    )
                )
            )
            .drawWithCache {
                val radial = Brush.radialGradient(
                    colors = listOf(
                        PaymentColors.CenterGlow.copy(alpha = 0.42f),
                        PaymentColors.CenterGlow.copy(alpha = 0f),
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.46f),
                    radius = size.minDimension * 0.7f,
                )
                onDrawBehind {
                    drawRect(radial)
                }
            }
    ) {
        AnimatedVisibility(
            visible = showWaves,
            enter = fadeIn(tween(550, easing = PaymentMotion.ContentEasing)),
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            AnimatedWaves(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = PaymentDims.ScreenHorizontal)
                .padding(top = PaymentDims.TopPadding, bottom = 14.dp)
        ) {
            TopBar(onBackClick = onBackClick, onCloseClick = onCloseClick)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(168.dp)
            ) {
                AnimatedVisibility(
                    visible = showCard,
                    enter = fadeIn(tween(350)),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 52.dp, y = (-10).dp),
                ) {
                    AnimatedCard()
                }
            }

            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(tween(350)) +
                    slideInVertically(
                        animationSpec = tween(430, easing = PaymentMotion.ContentEasing),
                        initialOffsetY = { it / 3 }
                    )
            ) {
                Text(
                    text = "к оплате",
                    color = PaymentColors.TextSecondary,
                    style = TextStyle(
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp,
                        letterSpacing = 0.3.sp,
                    )
                )
            }

            Spacer(Modifier.height(8.dp))

            AnimatedVisibility(
                visible = showAmount,
                enter = fadeIn(tween(420)) +
                    slideInVertically(
                        animationSpec = tween(460, easing = PaymentMotion.ContentEasing),
                        initialOffsetY = { it / 4 }
                    )
            ) {
                AmountBlock(amount = amount)
            }

            Spacer(Modifier.height(20.dp))

            AnimatedVisibility(
                visible = showActions,
                enter = fadeIn(tween(360)) +
                    slideInVertically(
                        animationSpec = tween(430, easing = PaymentMotion.ContentEasing),
                        initialOffsetY = { it / 3 }
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassActionButton(
                        text = "СPQR",
                        modifier = Modifier.weight(1f),
                        onClick = onSpqrClick
                    )
                    GlassActionButton(
                        text = "ручной ввод",
                        modifier = Modifier.weight(1f),
                        onClick = onManualInputClick
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(
                visible = showQr,
                enter = fadeIn(tween(420)) +
                    slideInVertically(
                        animationSpec = tween(520, easing = PaymentMotion.ContentEasing),
                        initialOffsetY = { it / 5 }
                    )
            ) {
                QrPanel(modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.weight(1f))

            AnimatedVisibility(
                visible = showBottom,
                enter = fadeIn(tween(500)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "или вставьте карту",
                        color = PaymentColors.TextHint,
                        style = TextStyle(
                            fontFamily = MontserratFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            letterSpacing = 0.2.sp,
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    BottomArrow()
                }
            }
        }
    }
}

@Composable
fun TopBar(
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TopBarIconButton(kind = TopIconKind.Back, onClick = onBackClick)
        TopBarIconButton(kind = TopIconKind.Close, onClick = onCloseClick)
    }
}

private enum class TopIconKind { Back, Close }

@Composable
private fun TopBarIconButton(
    kind: TopIconKind,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(18.dp)) {
            when (kind) {
                TopIconKind.Back -> {
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width * 0.72f, size.height * 0.18f),
                        end = Offset(size.width * 0.30f, size.height * 0.5f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round,
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width * 0.30f, size.height * 0.5f),
                        end = Offset(size.width * 0.72f, size.height * 0.82f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round,
                    )
                }

                TopIconKind.Close -> {
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width * 0.2f, size.height * 0.2f),
                        end = Offset(size.width * 0.8f, size.height * 0.8f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round,
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width * 0.8f, size.height * 0.2f),
                        end = Offset(size.width * 0.2f, size.height * 0.8f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedWaves(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "waves")
    val breath by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3800, easing = CubicBezierEasing(0.38f, 0f, 0.62f, 1f)),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breath"
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width * 0.5f, size.height * 0.10f + 8f * breath)
        val baseRadius = size.minDimension * 0.35f
        val waveCount = 7

        repeat(waveCount) { index ->
            val fraction = index / waveCount.toFloat()
            val phaseOffset = (fraction * 0.65f)
            val animatedFraction = ((breath + phaseOffset) % 1f)
            val radius = baseRadius + index * size.minDimension * 0.11f + animatedFraction * 20f
            val alpha = (0.15f - index * 0.016f) + (0.05f * (1f - animatedFraction))
            val stroke = 1.2.dp.toPx() + (index % 2) * 0.6.dp.toPx()
            val waveColor = if (index % 2 == 0) PaymentColors.WaveMain else PaymentColors.WaveSecondary

            drawCircle(
                color = waveColor.copy(alpha = alpha.coerceAtLeast(0.025f)),
                radius = radius,
                center = center,
                style = Stroke(width = stroke),
            )

            drawCircle(
                color = waveColor.copy(alpha = (alpha * 0.22f).coerceAtLeast(0.012f)),
                radius = radius + 3.dp.toPx(),
                center = center,
                style = Stroke(width = stroke * 1.8f),
            )
        }
    }
}

@Composable
fun AnimatedCard(
    modifier: Modifier = Modifier,
    last4: String = "0908",
) {
    val translationX = remember { Animatable(72f) }
    val translationY = remember { Animatable(-62f) }
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.93f) }
    val rotation = remember { Animatable(13f) }

    LaunchedEffect(Unit) {
        val cardSpec = tween<Float>(
            durationMillis = PaymentMotion.CardRevealMs,
            easing = PaymentMotion.CardEasing,
        )
        launch { translationX.animateTo(0f, animationSpec = cardSpec) }
        launch { translationY.animateTo(0f, animationSpec = cardSpec) }
        launch { alpha.animateTo(1f, animationSpec = tween(900, easing = PaymentMotion.CardEasing)) }
        launch { scale.animateTo(1f, animationSpec = tween(1000, easing = PaymentMotion.CardEasing)) }
        launch { rotation.animateTo(7.5f, animationSpec = tween(1150, easing = PaymentMotion.CardEasing)) }
    }

    Box(
        modifier = modifier
            .size(width = PaymentDims.CardWidth, height = PaymentDims.CardHeight)
            .graphicsLayer {
                this.translationX = translationX.value
                this.translationY = translationY.value
                this.alpha = alpha.value
                this.scaleX = scale.value
                this.scaleY = scale.value
                this.rotationZ = rotation.value
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.62f, 0.35f)
            }
            .clip(RoundedCornerShape(PaymentDims.CardCorner))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        PaymentColors.CardGradientStart,
                        PaymentColors.CardGradientMid,
                        PaymentColors.CardGradientEnd,
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.24f),
                shape = RoundedCornerShape(PaymentDims.CardCorner),
            )
            .drawBehind {
                val w = size.width
                val h = size.height
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color.White.copy(alpha = 0.24f), Color.Transparent),
                        center = Offset(w * 0.22f, h * 0.24f),
                        radius = h * 0.58f,
                    ),
                    radius = h * 0.58f,
                    center = Offset(w * 0.22f, h * 0.24f)
                )
            }
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            text = "••••  ••••  ••••",
            color = Color.White.copy(alpha = 0.72f),
            style = TextStyle(
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                letterSpacing = 1.6.sp,
            ),
            modifier = Modifier.align(Alignment.BottomStart),
        )
        Text(
            text = last4,
            color = Color.White,
            style = TextStyle(
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 1.4.sp,
            ),
            modifier = Modifier.align(Alignment.BottomEnd),
        )
    }
}

@Composable
fun AmountBlock(amount: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = amount,
            color = PaymentColors.TextPrimary,
            style = TextStyle(
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 52.sp,
                letterSpacing = 0.25.sp,
                lineHeight = 56.sp,
            )
        )
    }
}

@Composable
fun GlassActionButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .requiredHeight(PaymentDims.ActionButtonHeight)
            .clip(RoundedCornerShape(PaymentDims.ActionButtonCorner))
            .background(PaymentColors.GlassFill)
            .border(
                width = 1.dp,
                color = PaymentColors.GlassBorder,
                shape = RoundedCornerShape(PaymentDims.ActionButtonCorner),
            )
            .drawBehind {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            PaymentColors.GlassGlow,
                            Color.Transparent,
                            PaymentColors.GlassGlow.copy(alpha = 0.04f),
                        )
                    )
                )
            }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = PaymentColors.TextPrimary,
            style = TextStyle(
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
            ),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun QrPanel(
    modifier: Modifier = Modifier,
    qrCells: Int = 25,
) {
    Box(
        modifier = modifier
            .height(PaymentDims.QrPanelHeight)
            .clip(RoundedCornerShape(PaymentDims.QrPanelCorner))
            .background(Color.White.copy(alpha = 0.065f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.22f),
                shape = RoundedCornerShape(PaymentDims.QrPanelCorner),
            )
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.11f),
                            Color.Transparent,
                        )
                    )
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        DecorativeQr(size = PaymentDims.QrSize, cells = qrCells)
    }
}

@Composable
private fun DecorativeQr(
    size: Dp,
    cells: Int,
) {
    Canvas(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White.copy(alpha = 0.98f))
            .padding(12.dp)
    ) {
        val totalSize = this.size.minDimension
        val cell = totalSize / cells

        fun drawFinder(startX: Int, startY: Int) {
            drawRect(
                color = PaymentColors.QrDark,
                topLeft = Offset(startX * cell, startY * cell),
                size = Size(cell * 7f, cell * 7f),
            )
            drawRect(
                color = Color.White,
                topLeft = Offset((startX + 1) * cell, (startY + 1) * cell),
                size = Size(cell * 5f, cell * 5f),
            )
            drawRect(
                color = PaymentColors.QrDark,
                topLeft = Offset((startX + 2) * cell, (startY + 2) * cell),
                size = Size(cell * 3f, cell * 3f),
            )
        }

        drawFinder(0, 0)
        drawFinder(cells - 7, 0)
        drawFinder(0, cells - 7)

        for (y in 0 until cells) {
            for (x in 0 until cells) {
                val inFinder = (x in 0..6 && y in 0..6) ||
                    (x in cells - 7 until cells && y in 0..6) ||
                    (x in 0..6 && y in cells - 7 until cells)

                if (inFinder) continue

                val seed = (x * 31 + y * 17 + x * y * 13) % 11
                val draw = seed < 4 || (x + y) % 9 == 0
                if (draw) {
                    drawRoundRect(
                        color = PaymentColors.QrDark,
                        topLeft = Offset(x * cell, y * cell),
                        size = Size(cell * 0.92f, cell * 0.92f),
                        cornerRadius = CornerRadius(cell * 0.18f)
                    )
                }
            }
        }

        val path = Path().apply {
            moveTo(totalSize * 0.3f, totalSize * 0.52f)
            lineTo(totalSize * 0.46f, totalSize * 0.66f)
            lineTo(totalSize * 0.71f, totalSize * 0.37f)
        }
        drawPath(
            path = path,
            color = PaymentColors.QrDark.copy(alpha = 0.7f),
            style = Stroke(width = cell * 1.1f, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun BottomArrow() {
    Canvas(modifier = Modifier.size(width = 28.dp, height = 18.dp)) {
        drawLine(
            color = Color.White.copy(alpha = 0.68f),
            start = Offset(size.width * 0.1f, size.height * 0.2f),
            end = Offset(size.width * 0.5f, size.height * 0.82f),
            strokeWidth = 4f,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Color.White.copy(alpha = 0.68f),
            start = Offset(size.width * 0.9f, size.height * 0.2f),
            end = Offset(size.width * 0.5f, size.height * 0.82f),
            strokeWidth = 4f,
            cap = StrokeCap.Round,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF040815)
@Composable
private fun PaymentScreenPreview() {
    PaymentScreen()
}
