package com.nilax.weatherapp.presentation.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nilax.weatherapp.R
import com.nilax.weatherapp.domain.model.WeeklyForecast
import com.nilax.weatherapp.domain.model.WeeklyForecastData
import com.nilax.weatherapp.presentation.common.ErrorView
import com.nilax.weatherapp.presentation.common.LoadingView
import com.nilax.weatherapp.presentation.theme.WeatherAppTheme

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val homeScreenState by viewModel.homeScreenState.collectAsStateWithLifecycle()

    HomeScreenBody(
        state = homeScreenState,
        onSearchClicked = { viewModel.searchWeatherByCoordinates(it) },
        onTextChanged = { viewModel.onTextChanged(it) },
        onRetry = { viewModel.onRetry() }
    )
}

@Composable
fun HomeScreenBody(
    state: HomeScreenState,
    onSearchClicked: (String) -> Unit,
    onTextChanged: (String) -> Unit,
    onRetry: () -> Unit
) {

    if (state.weatherInfo != null) {
        WeatherInfo(
            weatherInfo = state.weatherInfo,
            searchedText = state.searchedText,
            isValidSearch = state.isValidSearch,
            onTextChanged = onTextChanged,
            onSearchClicked = onSearchClicked
        )
    }

    if (state.error != null) {
        val context = LocalContext.current
        if (state.needRetryScreen) {
            ErrorView(errorMessage = state.error.asString(), onRetry = onRetry)
        } else {
            Toast.makeText(context, state.error.asString(), Toast.LENGTH_LONG).show()
        }
    }

    if (state.isLoading) {
        LoadingView()
    }
}

@Composable
fun WeatherInfo(
    weatherInfo: WeeklyForecastData,
    searchedText: String,
    isValidSearch: Boolean,
    onTextChanged: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_home),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .background(color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                LocationSearch(isValidSearch, onTextChanged, onSearchClicked)
                Spacer(modifier = Modifier.padding(top = 32.dp))
                CurrentTemperature(
                    weatherInfo.currentTemp,
                    weatherInfo.dailyHigh,
                    weatherInfo.dailyLow
                )
                Spacer(modifier = Modifier.padding(top = 32.dp))
            }
        }
        WeeklyTemperatureList(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            searchedText = searchedText,
            list = weatherInfo.weeklyForecast
        )
    }
}

@Composable
fun LocationSearch(
    isValidSearch: Boolean,
    onTextChanged: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(id = R.string.lbl_cd_location)
                )
            },
            keyboardActions = KeyboardActions(onDone = {
                onSearchClicked(searchText)
                keyboardController?.hide()
            }),
            singleLine = true,
            value = searchText,
            onValueChange = {
                searchText = it
                onTextChanged(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 5.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            label = { Text("Location(lat, long)") }
        )
        IconButton(
            onClick = {
                keyboardController?.hide()
                onSearchClicked(searchText)
            },
            enabled = isValidSearch,
            modifier = Modifier
                .padding(start = 8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                tint = if (isValidSearch) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.4f
                ),
                contentDescription = stringResource(id = R.string.lbl_cd_search)
            )
        }
    }
}

@Composable
fun CurrentTemperature(currentTemp: Int, dailyHigh: Int, dailyLow: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textColor = MaterialTheme.colorScheme.onSecondaryContainer
        Text(
            text = stringResource(R.string.lbl_now),
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = "$currentTemp°",
                style = MaterialTheme.typography.displayLarge.copy(
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                color = textColor
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_cloud),
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Bottom),
                tint = textColor,
                contentDescription = stringResource(id = R.string.lbl_cd_temperature),
            )

        }

        Text(
            text = String.format(
                stringResource(R.string.lbl_current_temp_format),
                dailyHigh,
                dailyLow
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp),
            color = textColor
        )


    }

}

@Composable
fun WeeklyTemperatureList(modifier: Modifier, searchedText: String, list: List<WeeklyForecast>) {

    Column(
        modifier = modifier
            .padding(top = 32.dp)
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = stringResource(R.string.lbl_weekly_forecast, searchedText),
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn(Modifier.padding(top = 5.dp)) {
            items(items = list, key = { weather -> weather.date }) { weather ->
                WeeklyTemperature(weather)
            }
        }
    }
}

@Composable
fun WeeklyTemperature(weather: WeeklyForecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                text = weather.dayInfo.asString()
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_cloud),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                modifier = Modifier.defaultMinSize(minWidth = 60.dp),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall,
                text = "${weather.high}°/${weather.low}°"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WeatherAppTheme {
        Column(Modifier.fillMaxSize()) {
            WeatherInfo(
                weatherInfo = WeeklyForecastData(0, 0, 0, emptyList()),
                searchedText = "",
                isValidSearch = false,
                onTextChanged = {},
                onSearchClicked = {}
            )
        }
    }
}