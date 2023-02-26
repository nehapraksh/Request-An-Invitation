package com.codechallange.blinqcodechallenge

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog


object Util {
    private const val USER_REGISTERED = "USER_REGISTERED"

    /*
    *
    * function to show dialog with a message and two buttons - Yes and No
    * */
     fun showAlert(context: Context, message: String, onClickListener: OnDialogButtonClickListener){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.alert_title))
        builder.setMessage(message)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            onClickListener.onPositiveBtnClick()
            dialog.dismiss()

        }
        builder.setPositiveButton(context.getString(R.string.yes), positiveButtonClick)
        builder.setNegativeButton(context.getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()

    }

    /*
    *
    * function to show dialog box with a message and one button - OK
    * */
    fun showErrorDialog(context: Context, message: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.alert_title))
        builder.setMessage(message)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }
        builder.setPositiveButton(context.getString(R.string.ok), positiveButtonClick)
        builder.show()
    }

    /*
     *
    * function to initialize a sharedpreference to store data in memory
    * */
    fun userPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    /*
    *
    * extension function to save data to the memory using sharedpreference
    * */
    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    /*
    *
    * save and retrieve data from memory using sharedpreference
    * */
    var SharedPreferences.userRegistered
        get() = getBoolean(USER_REGISTERED, false)
        set(value) {
            editMe {
                it.putBoolean(USER_REGISTERED, value)
            }
        }

    /*
    *
    * remove data from memory using sharedpreference
    * */
    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }

     /**
     * function to show view
     */
    fun show(view: View){
        view.visibility = View.VISIBLE
    }
    /**
     * function to hide view
     */
    fun hide(view: View){
        view.visibility = View.GONE
    }

    /**
     * Extension function to simplify setting an afterTextChanged action to EditText components.
     */
    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }


    // function to validate username
    fun isValidName(target: CharSequence): Boolean{
        return !TextUtils.isEmpty(target) && target.length >=3
    }

    // function to validate email pattern
    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    // function to validate confirm emails
    fun isSameEmail(firstEmail: String, secondEmail: String): Boolean {
        return secondEmail.equals(firstEmail)
    }

}