package com.example.jenstore.ui.screens.profile.account.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.data.model.Notification

@Composable
fun NotificationsScreen(
    onBackClicked: () -> Unit,
    viewModel: NotificationViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val notification = viewModel.notification.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            NotificationAppBar(
                onBackClicked = onBackClicked,
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            NotificationCardList(
                notification = notification.value,
            )
        }
    }
}

@Composable
private fun NotificationCardList(
    notification: List<Notification>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(notification.sortedByDescending {
            it.dateCreated
        }, key = {notify -> notify.id }) { notify ->
            NotificationCardMenu(
                notify = notify
            )
        }
    }
}

@Composable
private fun NotificationCardMenu(
    notify: Notification,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.dp_10)),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        modifier = Modifier.fillMaxWidth()
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        Row(
            modifier = modifier
        ) {
            Image(
                painter = painterResource(R.drawable.jenlogo),
                contentDescription = stringResource(R.string.jenLogo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.dp_10),
                        top = dimensionResource(R.dimen.dp_15),
                        end = dimensionResource(R.dimen.dp_5)
                    )
                    .size(dimensionResource(R.dimen.dp_30))

            )
            Text(
                text = notify.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.dp_15),
                    )
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15)))
        Text(
            text = notify.message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = modifier
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                ),
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        Text(
            text = notify.dateCreated?.toDate().toString(),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    bottom = dimensionResource(R.dimen.dp_10)
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationAppBar(
    onBackClicked: () -> Unit
) {
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
                text = stringResource(R.string.notification),
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}