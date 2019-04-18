//package com.example.feiradasprofissoes.modules.util
//
//import android.content.Context
//import android.graphics.Color
//import android.support.design.widget.Snackbar
//import android.support.v4.content.ContextCompat
//import android.view.ViewGroup
//import com.example.feiradasprofissoes.R
//
//class SnackBarUtil {
//
//    val SNACKBAR_DURATION: Int = 3500
//
//    fun showSnackbar(status: SnackBarStatus, root: ViewGroup, message: String) {
//        showSnackBar(status,root, message, root.context)
//    }
//
//    fun showSnackBar(status: SnackBarStatus, coordinator: ViewGroup, message: String, ctx: Context) {
//        val snackbar = createSnackBar(status, coordinator, message, ctx)
//        snackbar.show()
//    }
//
//    fun createSnackBar(status: SnackBarStatus, coordinator: ViewGroup, message: String, ctx: Context): Snackbar {
//
//        val snackbar = Snackbar.make(coordinator, message, SNACKBAR_DURATION)
//
//        (snackbar.view.android.support.design.R.id.snackbar_text).setTextColor(Color.WHITE)
//
//        return when (status) {
//            SnackBarStatus.SUCCESS -> snackbar.view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.snackBarSuccess))
//        }
//
//        switch (status) {
//            case SUCCESS:
//            snackbar.view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.snackBarSuccess))
//            break
//            case ERROR:
//            snackbar.view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.snackBarError))
//            break
//            case ALERT:
//            snackbar.view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.snackBarAlert))
//            break
//            case NEUTRAL:
//            snackbar.view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.snackBarNeutral))
//            break
//        }
//
//        return snackbar
//
//    }
//
//}