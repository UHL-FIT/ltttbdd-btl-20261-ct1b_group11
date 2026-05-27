package com.btl.buddybudget.ui.home

sealed class Screen(val route: String) {
    object Overview : Screen("overview")
    object Statistics : Screen("statistics")
    object Budget : Screen("budget")
    //object Profile : Screen("profile")
    object AddTransaction : Screen("add_transaction")
    object About : Screen("about")


    object BieuDoTron: Screen("bieudotron")

    //Vi
    object Wallet : Screen("wallet")
    object AddWallet : Screen("add_wallet")
    object SelectWallet : Screen("select_wallet")

    object EditWallet : Screen("edit_wallet/{id}") {
        fun createRoute(id: Int) = "edit_wallet/$id"
    }

    //Danh Muc
    object Category : Screen("category/{type}") {
        fun createRoute(type: String) = "category/$type"
    }

    object ManageCategory : Screen("manage_category")

    object AddCategory : Screen("add_category")

    object EditCategory : Screen("edit_category/{id}") {
        fun createRoute(id: Int) = "edit_category/$id"
    }

    // Giao dich
    object TransactionHistory : Screen("transaction_history")
    object SearchTransactions : Screen("search_transactions")
    object EditTransaction : Screen("edit_transaction/{id}") {
        fun createRoute(id: Int) = "edit_transaction/$id"
    }
    object TransactionReport : Screen("transaction_report/{month}/{year}/{walletId}") {
        fun createRoute(month: Int, year: Int, walletId: Int?): String {
            return "transaction_report/$month/$year/${walletId ?: -1}"
        }
    }
}