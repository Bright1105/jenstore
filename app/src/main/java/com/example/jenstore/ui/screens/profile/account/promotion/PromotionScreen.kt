package com.example.jenstore.ui.screens.profile.account.promotion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.data.model.Promotions


@Composable
fun PromotionScreen(
    onBackClicked: () -> Unit,
    viewModel: PromotionsViewModels = viewModel(factory = AppViewModelProvider.Factory)
) {
    val promotions = viewModel.promotions.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            PromotionTopBar(
                onBackClicked = onBackClicked
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            if (promotions.value.isEmpty()) {
                Text(
                    text = stringResource(R.string.noPromotion),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                PromotionList(
                    promotions = promotions.value
                )
            }
        }
    }
}

@Composable
private fun PromotionList(
    promotions: List<Promotions>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(promotions, key = { promotion -> promotion.id }) { promotion ->
            PromotionCard(
                promotion = promotion
            )
        }
    }
}

@Composable
fun PromotionCard(
    promotion: Promotions,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_3)),
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.dp_300))
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        Row(modifier = modifier) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.dp_10))
            ) {
                Text(
                    text = promotion.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
                Text(
                    text = stringResource(R.string.buy) +" "+ promotion.worth +" "+ stringResource(R.string.worth),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_3)))
                Text(
                    text = stringResource(R.string.get) +" "+ promotion.discount + stringResource(R.string.off),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_3)))
                Text(
                    text = stringResource(R.string.valid) +" "+ promotion.validDays + stringResource(R.string.days),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(promotion.image)
                    .crossfade(true)
                    .build(),
                contentDescription = promotion.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.ic_connection_error),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.dp_200))
                    .fillMaxHeight()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PromotionTopBar(onBackClicked: () -> Unit) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.promotion),
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}