package com.example.uicardactivity.payment

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uicardactivity.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.drawWithContent

private object PaymentColors {
    val BackgroundTop = Color(0xFF020611)
    val BackgroundBottom = Color(0xFF000205)

    val CenterGlow = Color(0xFF176FC6)
    val SideGlow = Color(0xFF4C55D9)

    val WaveMain = Color(0xFF4B95E6)
    val WaveGlow = Color(0xFF9FD0FF)

    val GlassTop = Color.White.copy(alpha = 0.22f)
    val GlassBottom = Color.White.copy(alpha = 0.12f)
    val GlassStroke = Color.White.copy(alpha = 0.28f)

    val QrPanelTop = Color.White.copy(alpha = 0.24f)
    val QrPanelBottom = Color.White.copy(alpha = 0.14f)
    val QrPanelStroke = Color.White.copy(alpha = 0.26f)

    val TextPrimary = Color(0xFFF7F8FB)
    val TextSecondary = Color(0xFFE7EDF7)
    val TextHint = Color(0xFFEAF0F8)

    val QrDark = Color(0xFF5E7490)
}

private object PaymentMotion {
    val CardEasing = CubicBezierEasing(0.22f, 0.86f, 0.2f, 1f)
    val FadeEasing = CubicBezierEasing(0.24f, 0.8f, 0.2f, 1f)
    val WaveEasing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
}

private val MontserratFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_bold, FontWeight.Bold),
)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    amount: String = "3 999 ₽",
    onBackClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    onSpqrClick: () -> Unit = {},
    onManualInputClick: () -> Unit = {},
) {
    var showCard by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showAmount by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }
    var showQr by remember { mutableStateOf(false) }
    var showBottom by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        showCard = true
        delay(110)
        showTitle = true
        delay(90)
        showAmount = true
        delay(110)
        showButtons = true
        delay(120)
        showQr = true
        delay(120)
        showBottom = true
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PaymentColors.BackgroundTop,
                        PaymentColors.BackgroundBottom
                    )
                )
            )
            .drawWithCache {
                val centerGlow = Brush.radialGradient(
                    colors = listOf(
                        PaymentColors.CenterGlow.copy(alpha = 0.66f),
                        PaymentColors.CenterGlow.copy(alpha = 0.22f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.50f, size.height * 0.42f),
                    radius = size.minDimension * 0.70f
                )

                val sideGlow = Brush.radialGradient(
                    colors = listOf(
                        PaymentColors.SideGlow.copy(alpha = 0.14f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.84f, size.height * 0.18f),
                    radius = size.minDimension * 0.34f
                )

                val bottomShade = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFF010205).copy(alpha = 0.14f),
                        Color(0xFF000103).copy(alpha = 0.30f)
                    ),
                    startY = size.height * 0.62f,
                    endY = size.height
                )

                onDrawBehind {
                    drawRect(centerGlow)
                    drawRect(sideGlow)
                    drawRect(bottomShade)
                }
            }
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        val designWidth = 720f
        val designHeight = 1600f

        fun dx(value: Float): Dp = screenWidth * (value / designWidth)
        fun dy(value: Float): Dp = screenHeight * (value / designHeight)
        fun fsp(value: Float): TextUnit = (screenHeight.value * value / designHeight).sp

        val buttonWidth = dx(253f)
        val buttonHeight = dy(177f)
        val buttonGap = dx(32f)
        val buttonsTotalWidth = buttonWidth + buttonGap + buttonWidth
        val buttonsStartX = (screenWidth - buttonsTotalWidth) / 2

        val qrPanelSize = screenWidth * 0.75f
        val qrPanelX = (screenWidth - qrPanelSize) / 2

        Box(modifier = Modifier.fillMaxSize()) {
            TopWavesLayer(modifier = Modifier.fillMaxSize())

            Box(modifier = Modifier.fillMaxSize()) {
                TopBarIconButton(
                    kind = TopIconKind.Close,
                    onClick = onCloseClick,
                    size = dx(42f),
                    modifier = Modifier.offset(
                        x = screenWidth - dx(34f) - dx(42f),
                        y = dy(30f)
                    )
                )
            }

            if (showCard) {
                AnimatedCardLayer(
                    modifier = Modifier.offset(
                        x = dx(322f),
                        y = dy(102f)
                    ),
                    width = dx(660f),
                    height = dy(470f)
                )
            }

            if (showTitle) {
                Text(
                    text = "к оплате",
                    color = PaymentColors.TextSecondary,
                    style = TextStyle(
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = fsp(28f),
                        lineHeight = fsp(28f),
                        letterSpacing = 0.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = dy(452f))
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }

            if (showAmount) {
                Text(
                    text = amount,
                    color = PaymentColors.TextPrimary,
                    style = TextStyle(
                        fontFamily = MontserratFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = fsp(84f),
                        lineHeight = fsp(84f),
                        letterSpacing = (-1.6).sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = dy(505f))
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }

            if (showButtons) {
                ActionButtonsLayer(
                    modifier = Modifier.offset(
                        x = buttonsStartX,
                        y = dy(700f)
                    ),
                    buttonWidth = buttonWidth,
                    buttonHeight = buttonHeight,
                    gap = buttonGap,
                    iconSize = dx(42f),
                    textSize = fsp(22f),
                    onSpqrClick = onSpqrClick,
                    onManualInputClick = onManualInputClick
                )
            }

            if (showQr) {
                QrPanelLayer(
                    modifier = Modifier.offset(
                        x = qrPanelX,
                        y = dy(909f)
                    ),
                    panelSize = qrPanelSize
                )
            }

            if (showBottom) {
                BottomHintLayer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = dy(1500f))
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    textSize = fsp(22f),
                    arrowWidth = dx(42f),
                    arrowHeight = dy(34f)
                )
            }
        }
    }
}

@Composable
private fun TopWavesLayer(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "topWaves")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3600,
                easing = CubicBezierEasing(0.22f, 0f, 0.18f, 1f)
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Canvas(modifier = modifier) {
        val center = Offset(
            x = size.width * 0.5f,
            y = -size.height * 0.04f
        )

        val ringCount = 5
        val minRadius = size.width * 0.10f
        val maxRadius = size.width * 0.62f
        val strokePx = 2.2f
        val glowStrokePx = 6.0f

        repeat(ringCount) { index ->
            val delay = index * 0.13f
            val local = (progress - delay)

            if (local > 0f) {
                val clamped = (local / (1f - delay)).coerceIn(0f, 1f)
                val eased = CubicBezierEasing(0.22f, 0f, 0.18f, 1f).transform(clamped)

                val radius = lerp(minRadius, maxRadius, eased)
                val alpha = (1f - eased).coerceIn(0f, 1f)

                drawCircle(
                    color = PaymentColors.WaveMain.copy(alpha = 0.30f * alpha),
                    radius = radius,
                    center = center,
                    style = Stroke(width = strokePx)
                )

                drawCircle(
                    color = PaymentColors.WaveGlow.copy(alpha = 0.10f * alpha),
                    radius = radius + 3f,
                    center = center,
                    style = Stroke(width = glowStrokePx)
                )
            }
        }
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction
}

@Composable
private fun TopIconsLayer(
    backX: Dp,
    backY: Dp,
    closeX: Dp,
    closeY: Dp,
    iconSize: Dp,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TopBarIconButton(
            kind = TopIconKind.Back,
            onClick = onBackClick,
            size = iconSize,
            modifier = Modifier.offset(backX, backY)
        )
        TopBarIconButton(
            kind = TopIconKind.Close,
            onClick = onCloseClick,
            size = iconSize,
            modifier = Modifier.offset(closeX, closeY)
        )
    }
}

private enum class TopIconKind { Back, Close }

@Composable
private fun TopBarIconButton(
    kind: TopIconKind,
    onClick: () -> Unit,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = this.size.minDimension * 0.08f

            when (kind) {
                TopIconKind.Back -> {
                    drawLine(
                        color = Color.White,
                        start = Offset(this.size.width * 0.68f, this.size.height * 0.18f),
                        end = Offset(this.size.width * 0.32f, this.size.height * 0.50f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(this.size.width * 0.32f, this.size.height * 0.50f),
                        end = Offset(this.size.width * 0.68f, this.size.height * 0.82f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round
                    )
                }

                TopIconKind.Close -> {
                    drawLine(
                        color = Color.White,
                        start = Offset(this.size.width * 0.22f, this.size.height * 0.22f),
                        end = Offset(this.size.width * 0.78f, this.size.height * 0.78f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(this.size.width * 0.78f, this.size.height * 0.22f),
                        end = Offset(this.size.width * 0.22f, this.size.height * 0.78f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedCardLayer(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
) {
    val translationXAnim = remember { Animatable(220f) }
    val translationYAnim = remember { Animatable(-180f) }
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.86f) }
    val rotationAnim = remember { Animatable(22f) }

    val auraAlphaAnim = remember { Animatable(0f) }
    val auraScaleAnim = remember { Animatable(0.82f) }
    val contactPulseAnim = remember { Animatable(0f) }

    val shinePrimary = remember { Animatable(-0.40f) }
    val shineSecondary = remember { Animatable(-0.55f) }

    LaunchedEffect(Unit) {
        launch {
            translationXAnim.animateTo(
                targetValue = -18f,
                animationSpec = tween(
                    durationMillis = 980,
                    easing = CubicBezierEasing(0.14f, 0.92f, 0.16f, 1f)
                )
            )
            translationXAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 240,
                    easing = CubicBezierEasing(0.28f, 0f, 0.18f, 1f)
                )
            )
        }

        launch {
            translationYAnim.animateTo(
                targetValue = 16f,
                animationSpec = tween(
                    durationMillis = 1120,
                    easing = CubicBezierEasing(0.14f, 0.92f, 0.16f, 1f)
                )
            )
            translationYAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 260,
                    easing = CubicBezierEasing(0.28f, 0f, 0.18f, 1f)
                )
            )
        }

        launch {
            alphaAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 420,
                    easing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1f)
                )
            )
        }

        launch {
            scaleAnim.animateTo(
                targetValue = 1.03f,
                animationSpec = tween(
                    durationMillis = 980,
                    easing = CubicBezierEasing(0.14f, 0.92f, 0.16f, 1f)
                )
            )
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 240,
                    easing = CubicBezierEasing(0.28f, 0f, 0.18f, 1f)
                )
            )
        }

        launch {
            rotationAnim.animateTo(
                targetValue = 10.8f,
                animationSpec = tween(
                    durationMillis = 1180,
                    easing = CubicBezierEasing(0.14f, 0.92f, 0.16f, 1f)
                )
            )
        }

        launch {
            auraAlphaAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 760,
                    easing = CubicBezierEasing(0.20f, 0.8f, 0.18f, 1f)
                )
            )
        }

        launch {
            auraScaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1040,
                    easing = CubicBezierEasing(0.14f, 0.86f, 0.16f, 1f)
                )
            )
        }

        delay(980)

        // Эффект "приложили к экрану"
        launch {
            scaleAnim.animateTo(
                targetValue = 0.972f,
                animationSpec = tween(
                    durationMillis = 190,
                    easing = CubicBezierEasing(0.32f, 0f, 0.2f, 1f)
                )
            )
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 280,
                    easing = CubicBezierEasing(0.22f, 0.82f, 0.2f, 1f)
                )
            )
        }

        launch {
            translationYAnim.animateTo(
                targetValue = 8f,
                animationSpec = tween(
                    durationMillis = 190,
                    easing = CubicBezierEasing(0.32f, 0f, 0.2f, 1f)
                )
            )
            translationYAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 280,
                    easing = CubicBezierEasing(0.22f, 0.82f, 0.2f, 1f)
                )
            )
        }

        launch {
            contactPulseAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 210,
                    easing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1f)
                )
            )
            contactPulseAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 320,
                    easing = CubicBezierEasing(0.22f, 0.82f, 0.2f, 1f)
                )
            )
        }

        delay(140)

        launch {
            shinePrimary.animateTo(
                targetValue = 1.18f,
                animationSpec = tween(
                    durationMillis = 980,
                    easing = CubicBezierEasing(0.22f, 0f, 0.18f, 1f)
                )
            )
        }

        delay(180)

        launch {
            shineSecondary.animateTo(
                targetValue = 1.18f,
                animationSpec = tween(
                    durationMillis = 760,
                    easing = CubicBezierEasing(0.22f, 0f, 0.18f, 1f)
                )
            )
        }
    }

    Box(
        modifier = modifier.size(width = width, height = height)
    ) {
        Box(
            modifier = Modifier
                .offset(x = (-110).dp, y = (-90).dp)
                .size(width = width + 220.dp, height = height + 180.dp)
                .graphicsLayer {
                    this.translationX = translationXAnim.value
                    this.translationY = translationYAnim.value
                    this.alpha = auraAlphaAnim.value * (0.68f + contactPulseAnim.value * 0.18f)
                    this.scaleX = auraScaleAnim.value + contactPulseAnim.value * 0.03f
                    this.scaleY = auraScaleAnim.value + contactPulseAnim.value * 0.03f
                    this.rotationZ = rotationAnim.value
                    clip = false
                    transformOrigin = TransformOrigin(0.72f, 0.30f)
                }
                .drawWithContent {
                    val w = size.width
                    val h = size.height

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF39D3FF).copy(alpha = 0.20f + contactPulseAnim.value * 0.08f),
                                Color(0xFF39D3FF).copy(alpha = 0.10f),
                                Color(0xFF39D3FF).copy(alpha = 0.03f),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.26f, h * 0.42f),
                            radius = w * 0.34f
                        ),
                        radius = w * 0.34f,
                        center = Offset(w * 0.26f, h * 0.42f)
                    )

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF5F62FF).copy(alpha = 0.18f + contactPulseAnim.value * 0.06f),
                                Color(0xFF5F62FF).copy(alpha = 0.09f),
                                Color(0xFF5F62FF).copy(alpha = 0.025f),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.76f, h * 0.56f),
                            radius = w * 0.38f
                        ),
                        radius = w * 0.38f,
                        center = Offset(w * 0.76f, h * 0.56f)
                    )
                }
        )

        Image(
            painter = painterResource(id = R.drawable.container_card_full),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.translationX = translationXAnim.value
                    this.translationY = translationYAnim.value
                    this.alpha = alphaAnim.value
                    this.scaleX = scaleAnim.value
                    this.scaleY = scaleAnim.value
                    this.rotationZ = rotationAnim.value
                    transformOrigin = TransformOrigin(0.72f, 0.30f)
                    compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen
                }
                .drawWithContent {
                    drawContent()

                    val cardWidth = size.width
                    val cardHeight = size.height

                    val primaryBandWidth = cardWidth * 0.09f
                    val primaryStartX = cardWidth * (shinePrimary.value - 0.06f)
                    val primaryEndX = primaryStartX + primaryBandWidth

                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x99DDF2FF).copy(alpha = 0.00f),
                                Color(0x99DDF2FF).copy(alpha = 0.06f),
                                Color.White.copy(alpha = 0.16f),
                                Color(0x99DDF2FF).copy(alpha = 0.06f),
                                Color(0x99DDF2FF).copy(alpha = 0.00f),
                                Color.Transparent,
                            ),
                            start = Offset(primaryStartX, 0f),
                            end = Offset(primaryEndX, cardHeight)
                        ),
                        topLeft = Offset(primaryStartX, 0f),
                        size = Size(primaryBandWidth, cardHeight),
                        cornerRadius = CornerRadius(
                            x = primaryBandWidth * 0.7f,
                            y = primaryBandWidth * 0.7f
                        ),
                        blendMode = androidx.compose.ui.graphics.BlendMode.SrcAtop
                    )

                    val secondaryBandWidth = cardWidth * 0.08f
                    val secondaryStartX = cardWidth * (shineSecondary.value - 0.10f)
                    val secondaryEndX = secondaryStartX + secondaryBandWidth

                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.00f),
                                Color.White.copy(alpha = 0.08f),
                                Color.White.copy(alpha = 0.14f),
                                Color.White.copy(alpha = 0.08f),
                                Color.White.copy(alpha = 0.00f),
                                Color.Transparent,
                            ),
                            start = Offset(secondaryStartX, 0f),
                            end = Offset(secondaryEndX, cardHeight)
                        ),
                        blendMode = androidx.compose.ui.graphics.BlendMode.SrcAtop
                    )
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun ActionButtonsLayer(
    modifier: Modifier = Modifier,
    buttonWidth: Dp,
    buttonHeight: Dp,
    gap: Dp,
    iconSize: Dp,
    textSize: TextUnit,
    onSpqrClick: () -> Unit,
    onManualInputClick: () -> Unit,
) {
    Row(modifier = modifier) {
        GlassActionButton(
            text = "СPQR",
            icon = ActionIcon.Spqr,
            iconSize = iconSize,
            textSize = textSize,
            modifier = Modifier.size(buttonWidth, buttonHeight),
            onClick = onSpqrClick
        )
        Spacer(modifier = Modifier.size(gap))
        GlassActionButton(
            text = "ручной ввод",
            icon = ActionIcon.Card,
            iconSize = iconSize,
            textSize = textSize,
            modifier = Modifier.size(buttonWidth, buttonHeight),
            onClick = onManualInputClick
        )
    }
}

private enum class ActionIcon { Spqr, Card }

@Composable
private fun GlassActionButton(
    text: String,
    icon: ActionIcon,
    iconSize: Dp,
    textSize: TextUnit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        PaymentColors.GlassTop,
                        PaymentColors.GlassBottom
                    )
                )
            )
            .border(
                width = 1.dp,
                color = PaymentColors.GlassStroke,
                shape = RoundedCornerShape(30.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButtonIcon(
                icon = icon,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.size(18.dp))
            Text(
                text = text,
                color = PaymentColors.TextPrimary,
                style = TextStyle(
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = textSize,
                    lineHeight = textSize,
                    letterSpacing = 0.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ActionButtonIcon(
    icon: ActionIcon,
    modifier: Modifier = Modifier,
) {
    val iconRes = when (icon) {
        ActionIcon.Spqr -> R.drawable.ic_spqr_custom
        ActionIcon.Card -> R.drawable.ic_card_custom
    }

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun QrPanelLayer(
    modifier: Modifier = Modifier,
    panelSize: Dp,
) {
    Box(
        modifier = modifier
            .size(panelSize)
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        PaymentColors.QrPanelTop,
                        PaymentColors.QrPanelBottom
                    )
                )
            )
            .border(
                width = 1.dp,
                color = PaymentColors.QrPanelStroke,
                shape = RoundedCornerShape(30.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.qr_custom),
            contentDescription = null,
            modifier = Modifier.size(panelSize * 0.82f),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun BottomHintLayer(
    modifier: Modifier = Modifier,
    textSize: TextUnit,
    arrowWidth: Dp,
    arrowHeight: Dp,
) {
    val transition = rememberInfiniteTransition(label = "arrowPulse")
    val pulse by transition.animateFloat(
        initialValue = 0.200f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "или вставьте карту",
            color = PaymentColors.TextHint,
            style = TextStyle(
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.Normal,
                fontSize = textSize,
                lineHeight = textSize,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.size(14.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_bottom_arrow_custom),
            contentDescription = null,
            modifier = Modifier
                .size(width = arrowWidth, height = arrowHeight)
                .graphicsLayer {
                    alpha = pulse
                    scaleX = pulse
                    scaleY = pulse
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF020611, widthDp = 360, heightDp = 800)
@Composable
private fun PaymentScreenPreview() {
    PaymentScreen()
}