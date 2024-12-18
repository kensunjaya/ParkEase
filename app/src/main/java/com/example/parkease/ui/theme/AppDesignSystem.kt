package com.example.parkease.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

//colors
data class AppColorScheme (
    val background: Color,
    val onBackground: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val anchor: Color,
    val softBlue: Color,
    val blueTeal: Color,
    val bluePale: Color,
)

//typography
data class AppTypography (
    val titleLarge: TextStyle,
    val titleBig: TextStyle,
    val titleNormal: TextStyle,
    val body: TextStyle,
    val labelLarge: TextStyle,
    val labelNormal: TextStyle,
    val labelSmall: TextStyle,
    val labelLargeSemiBold: TextStyle,
    val labelNormalSemiBold: TextStyle,
    val labelSmallSemiBold: TextStyle,
)

//shape
data class AppShape(
    val container: Shape,
    val button: Shape,
)

//size
data class AppSize(
    val large: Dp,
    val medium: Dp,
    val normal: Dp,
    val small: Dp,
)

val LocalAppColorScheme = staticCompositionLocalOf {
    AppColorScheme(
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        primary = Color.Unspecified,
        onPrimary = Color.Unspecified,
        secondary = Color.Unspecified,
        onSecondary = Color.Unspecified,
        anchor = Color.Unspecified,
        softBlue = Color.Unspecified,
        blueTeal = Color.Unspecified,
        bluePale = Color.Unspecified,
    )
}

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        titleLarge = TextStyle.Default,
        titleBig = TextStyle.Default,
        titleNormal = TextStyle.Default,
        body = TextStyle.Default,
        labelLarge = TextStyle.Default,
        labelNormal = TextStyle.Default,
        labelSmall = TextStyle.Default,
        labelLargeSemiBold = TextStyle.Default,
        labelNormalSemiBold = TextStyle.Default,
        labelSmallSemiBold = TextStyle.Default,
    )
}

val LocalAppShape = staticCompositionLocalOf {
    AppShape(
        container = RectangleShape,
        button = RectangleShape,
    )
}

val LocalAppSize = staticCompositionLocalOf {
    AppSize(
        large = Dp.Unspecified,
        medium = Dp.Unspecified,
        normal = Dp.Unspecified,
        small = Dp.Unspecified,
    )
}