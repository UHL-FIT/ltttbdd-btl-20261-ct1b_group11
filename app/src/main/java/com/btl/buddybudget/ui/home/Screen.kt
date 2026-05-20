package com.btl.buddybudget.ui.home

sealed class Screen(val route: String) {
    object Overview : Screen("overview")
    object Statistics : Screen("statistics")
    object Budget : Screen("budget")
    //object Profile : Screen("profile")
    object AddTransaction : Screen("add_transaction")
    object About : Screen("about")


    //Vi
    object Wallet : Screen("wallet")
    object AddWallet : Screen("add_wallet")

    object EditWallet : Screen("edit_wallet/{id}") {
        fun createRoute(id: Int) = "edit_wallet/$id"
    }

    //Danh Muc
    object Category : Screen("category")

    object AddCategory : Screen("add_category")

    object EditCategory : Screen("edit_category/{id}") {
        fun createRoute(id: Int) = "edit_category/$id"
    }
}