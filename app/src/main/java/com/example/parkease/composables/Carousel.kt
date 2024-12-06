    package com.example.parkease.composables

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.Text
    import androidx.compose.material3.Card
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.graphicsLayer
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.style.TextAlign
    import com.example.parkease.R
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.util.lerp
    import coil.compose.AsyncImage
    import com.example.parkease.ui.theme.AppTheme
    import com.example.parkease.utilities.Location
    import com.google.accompanist.pager.*
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.yield
    import kotlin.math.absoluteValue

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Carousel(
        locationData: List<Location>
    ) {
        val pagerState = rememberPagerState(initialPage = 0)
        val imageSlider = locationData.map { it.thumbnail }
        Column {
            HorizontalPager(
                count = imageSlider.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) { page ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .graphicsLayer{
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                ) {
                    AsyncImage(
                        model = imageSlider[page],
                        contentDescription = locationData[page].name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = locationData[pagerState.currentPage].name, style = AppTheme.typography.titleBig, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = locationData[pagerState.currentPage].description, style = AppTheme.typography.labelNormal, textAlign = TextAlign.Center)
            }

        }

    }
