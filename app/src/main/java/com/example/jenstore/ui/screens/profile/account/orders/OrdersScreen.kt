package com.example.jenstore.ui.screens.profile.account.orders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.Search
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.local.cart.CheckoutEntity
import com.example.jenstore.data.model.Checkout
import com.example.jenstore.data.model.JennyInfo
import com.example.jenstore.ui.screens.common.MyCartIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrdersScreen(
    ordersViewModel: OrdersViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackClicked: () -> Unit,
    onSearchClicked: (StoreDestinations) -> Unit,
    onCartClicked: (StoreDestinations) -> Unit
) {

    val uiState = ordersViewModel.uiState.collectAsState()
    val orders = ordersViewModel.orders.collectAsState(initial = emptyList())
    val checkoutEntity = ordersViewModel.checkoutEntity.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState {
       OrdersTabs.entries.size
    }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    //pagerState.isScrollInProgress
    LaunchedEffect(pagerState.currentPage, ) {
//        if (!pagerState.isScrollInProgress) {
//            selectedTabIndex = pagerState.currentPage
//        }
        selectedTabIndex = pagerState.currentPage
    }
    Scaffold(
        topBar = {
            OrderTopBar(
                onBackClicked = {
                    if (uiState.value.orderDetails) ordersViewModel.back() else onBackClicked()
                },
                onCartClicked,
                onSearchClicked
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            if (uiState.value.orderDetails) {
                uiState.value.checkout?.let {
                    OrdersDetails(
                        checkout = it,
                        onClicked = {
                            scope.launch {
                                ordersViewModel.updateCanceled(it)
                                ordersViewModel.back()
                            }
                        },
                        makePaymentClicked = {
                            ordersViewModel.makePayment()
                        },
                        payment = uiState.value.makePayment,
                        onAlert = {
                            ordersViewModel.alert()
                        },
                        cancelAlert = uiState.value.cancelAlert,
                        onCancelAlert = {
                            ordersViewModel.cancelAlert()
                        },
                        jennyInfo = JennyInfo(),
                        madePayment = uiState.value.paymentMade,
                        noPayment = {
                            ordersViewModel.cancelMakePayment()
                        },
                        onMadePayment = {
                            ordersViewModel.makePayment()
                        }
                    )
                }
            } else {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OrdersTabs.entries.forEachIndexed { index, ordersTabs ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = {
                                scope.launch {
                                    selectedTabIndex = index
                                }
                            },
                            text = {
                                Text(
                                    text = ordersTabs.text,
                                    style = MaterialTheme.typography.labelMedium
                                ) },
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { index ->
                    if (index == 0) {
                        Column {
                            if (orders.value.isEmpty()) {
                                OrdersEmpty()
                            } else {
                                OrdersContentList(
                                    checkouts = orders.value.sortedByDescending { sortP ->
                                        sortP.dateCreated
                                    },
                                    onClicked = { id ->
                                        scope.launch {
                                            ordersViewModel.getCheckoutById(id)
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        if (checkoutEntity.value.isEmpty()) {
                            OrdersEmpty()
                        } else {
                            OrderContentEntityList(
                                checkoutEntities = checkoutEntity.value
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrdersEmpty() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingBag,
            contentDescription = stringResource(R.string.shop),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(dimensionResource(R.dimen.dp_300))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrdersDetails(
    checkout: Checkout,
    onClicked: (Checkout) -> Unit,
    makePaymentClicked: () -> Unit,
    cancelAlert: Boolean,
    payment: Boolean,
    onCancelAlert: () -> Unit,
    onAlert: () -> Unit,
    jennyInfo: JennyInfo,
    onMadePayment: () -> Unit,
    noPayment: () -> Unit,
    madePayment: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                bottom = dimensionResource(R.dimen.dp_10)
            )
    ) {
        OrdersContent(
            checkout = checkout,
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.dp_10))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OrdersDetailsButton(
                onClicked = onAlert,
                text = stringResource(R.string.cancels),
                color = MaterialTheme.colorScheme.background,
                enable = !payment || madePayment,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            )
            OrdersDetailsButton(
                onClicked =  makePaymentClicked,
                text = stringResource(R.string.makePayment),
                color = MaterialTheme.colorScheme.background,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.dp_10))
        )
        if (payment) {
            Row {
                Image(
                    painter = painterResource(R.drawable.jenlogo),
                    contentDescription = stringResource(R.string.jenLogo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.dp_50))
                )
                Text(
                    text = jennyInfo.firstName +" "+ jennyInfo.lastName,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(jennyInfo.bankImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.gtBank),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.dp_50))
                )
                Text(
                    text = jennyInfo.bankName + " :"+ jennyInfo.bankAccount,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Row {
                Image(
                    painter = painterResource(R.drawable.whatsapp),
                    contentDescription = stringResource(R.string.whatapp),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.dp_50))
                )
                Text(
                    text = stringResource(R.string.whatapp) + " :" + jennyInfo.phoneNumber,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text = stringResource(R.string.receipt),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify
            )
            if (!madePayment) {
                Row {
                    Text(
                        text = stringResource(R.string.paymentMade)
                    )
                    OrdersDetailsButton(
                        onClicked = { noPayment() },
                        text = stringResource(R.string.no),
                        color = MaterialTheme.colorScheme.background,
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    )
                    OrdersDetailsButton(
                        onClicked = { onMadePayment() },
                        text = stringResource(R.string.yes),
                        color = MaterialTheme.colorScheme.background,
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.confirmPayment)
                )
            }
        }
    }


    if (cancelAlert) {
        BasicAlertDialog(
            onDismissRequest = onCancelAlert,

            ) {
            Column {
                Card(
                    shape = RoundedCornerShape(dimensionResource(R.dimen.dp_15))
                ) {
                    Text(
                        text = stringResource(R.string.cancelOrder),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(R.dimen.dp_10)
                            )
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
                    Text(
                        text = stringResource(R.string.cancelOrderNote),
                        style = MaterialTheme.typography.displayLarge,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(R.dimen.dp_10),
                                end = dimensionResource(R.dimen.dp_10)
                            )
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
                    Row {
                        OrdersDetailsButton(
                            onClicked = onCancelAlert,
                            text = stringResource(R.string.no),
                            color = MaterialTheme.colorScheme.background,
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                        )
                        OrdersDetailsButton(
                            onClicked = { onClicked(checkout) },
                            text = stringResource(R.string.yes),
                            color = MaterialTheme.colorScheme.background,
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrdersDetailsButton(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    text: String,
    color: Color,
    colors: ButtonColors,
    enable: Boolean = true,
) {
    Button(
        onClick = onClicked,
        colors = colors,
        enabled = enable,
        modifier = modifier
            .padding(dimensionResource(R.dimen.dp_10))
            .width(dimensionResource(R.dimen.dp_150))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}






@Composable
private fun OrdersContentList(
    checkouts: List<Checkout>,
    onClicked: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(checkouts, key = { checkout -> checkout.id }) { checkout ->
            OrdersContent(
                checkout = checkout,
                onClicked = onClicked
            )
        }
    }
}

@Composable
private fun OrderContentEntityList(
    checkoutEntities: List<CheckoutEntity>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(checkoutEntities, key = { checkout -> checkout.id }) {
            OrderContentEntity(checkoutEntity = it)
        }
    }
}

@Composable
private fun OrderContentEntity(
    modifier: Modifier = Modifier,
    checkoutEntity: CheckoutEntity
) {
    Column(
        modifier = modifier
    ) {
        Card(
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_5)),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Row(modifier = Modifier.padding(dimensionResource(R.dimen.dp_10))) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(checkoutEntity.image)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = checkoutEntity.title,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.dp_100))
                        .padding(
                            end = dimensionResource(R.dimen.dp_5)
                        )
                )
                Column {
                    Text(
                        text = checkoutEntity.title,
                        style = MaterialTheme.typography.displayLarge,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.dp_180))
                            .height(dimensionResource(R.dimen.dp_45))
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.background,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(dimensionResource(R.dimen.dp_3)),
                                color = MaterialTheme.colorScheme.error
                            )
                    )
                    Text(
                        text = checkoutEntity.dateCreated.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun OrdersContent(
    modifier: Modifier = Modifier,
    checkout: Checkout,
    onClicked: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {
        Card(
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_5)),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
            onClick = { onClicked(checkout.id) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Row(modifier = Modifier.padding(dimensionResource(R.dimen.dp_10))) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(checkout.ordersEntity.image)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = checkout.ordersEntity.title,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.dp_100))
                        .padding(
                            end = dimensionResource(R.dimen.dp_5)
                        )
                )
                Column {
                    Text(
                        text = checkout.ordersEntity.title,
                        style = MaterialTheme.typography.displayLarge,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.dp_180))
                            .height(dimensionResource(R.dimen.dp_45))
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
                    Text(
                        text = if (checkout.orderPending ) stringResource(R.string.pending) else stringResource(R.string.shipping),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(dimensionResource(R.dimen.dp_3)),
                                color = if (checkout.orderPending) MaterialTheme.colorScheme.primaryContainer else Color.Green
                            )
                    )
                    Text(
                        text = checkout.dateCreated?.toDate().toString(),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderTopBar(
    onBackClicked: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onSearchClicked: (StoreDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        navigationIcon = {
           IconButton(
               onClick = onBackClicked
           ) {
               Icon(
                   imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                   contentDescription = stringResource(R.string.back),
                   tint = MaterialTheme.colorScheme.primary
               )
           }
        },
        title = {
            Text(
                text = stringResource(R.string.orders),
                style = MaterialTheme.typography.titleSmall
            )
        },
        actions = {
            IconButton(
                onClick = { onSearchClicked(Search) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            MyCartIcon(
                onCartClicked = onCartClicked
            )
        }
    )
}

enum class OrdersTabs(val text: String) {
    ONGOING(text = "ONGOING/DELIVERED"),
    CANCELED(text = "CANCELED/RETURNED")
}

