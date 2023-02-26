package com.codechallange.blinqcodechallenge

import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.codechallange.blinqcodechallenge.Util.afterTextChanged
import com.codechallange.blinqcodechallenge.Util.isSameEmail
import com.codechallange.blinqcodechallenge.Util.isValidEmail
import com.codechallange.blinqcodechallenge.Util.isValidName
import com.codechallange.blinqcodechallenge.Util.show
import com.codechallange.blinqcodechallenge.Util.showAlert
import com.codechallange.blinqcodechallenge.Util.showErrorDialog
import com.codechallange.blinqcodechallenge.Util.userPreference
import com.codechallange.blinqcodechallenge.Util.userRegistered
import com.codechallange.blinqcodechallenge.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var builder: AlertDialog
    private lateinit var invitationViewModel: InvitationViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = TITLE
        // set the view model
        invitationViewModel = ViewModelProvider(this, InvitationViewModelFactory())
            .get(InvitationViewModel::class.java)
        // initialise the sharedpreference
        val prefs = userPreference(this, PREF_NAME)
        // set data to views based on the value saved in memory
        if (prefs.userRegistered)
            setCancelView()
        else
            setInviteView()
        // send or cancel invite button click listener
        binding.invitationBtn.setOnClickListener {
            if (binding.invitationBtn.text == getString(R.string.send_invite))
                openSendInviteDialog()
            else {
                showAlert(this, getString(R.string.cancel_alert_msg),
                    object : OnDialogButtonClickListener {
                        override fun onPositiveBtnClick() {
                            binding.descriptionTv.text = getString(R.string.invite_description)
                            binding.invitationBtn.text = getString(R.string.send_invite)
                            prefs.userRegistered = false
                            openInviteOrCancelSuccessView(false)
                        }
                    })
            }
        }
        // observer to observe the send invite success response
        invitationViewModel.invitationResponse.observe(this) {
            prefs.userRegistered = true
            openInviteOrCancelSuccessView(true)
        }
        // observer to observe the send invite error response
        invitationViewModel.errorMessage.observe(this) {
            builder.dismiss()
            showErrorDialog(this, it)
        }
    }

    // function to validate send invite view fields data
    private fun isValidData(
        name: EditText,
        email: EditText,
        confirmEmail: EditText
    ): Boolean {
        var valid = true
        if (!isValidName(name.text)) {
            name.error = getString(R.string.invalid_full_name)
            valid = false
        }
        if (!isValidEmail(email.text)) {
            email.error = getString(R.string.incorrect_email)
            valid = false
        }
        if (!isSameEmail(email.text.toString(), confirmEmail.text.toString())) {
            confirmEmail.error = getString(R.string.incorrect_confirm_email)
            valid = false
        }
        return valid
    }

    // function to validate if send invite view fields are empty
    private fun isEmpty(
        name: String,
        email: String,
        confirmEmail: String
    ): Boolean {
        var isBlank = false
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(confirmEmail)) {
            isBlank = true
        }
        return isBlank
    }

    // function to open send invite dialog
    private fun openSendInviteDialog() {
        builder = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.send_invite_view, null)
        val sendButton = view.findViewById<Button>(R.id.send_btn)
        val name = view.findViewById<EditText>(R.id.firstname_et)
        val email = view.findViewById<EditText>(R.id.email_et)
        val confirmEmail = view.findViewById<EditText>(R.id.confirm_email_et)
        val loadingBar = view.findViewById<ProgressBar>(R.id.loading_bar)
        builder.setView(view)
        sendButton.isEnabled =
            !isEmpty(name.text.toString(), email.text.toString(), confirmEmail.text.toString())
        name.afterTextChanged {
            if (it.trim().isNotEmpty() && !sendButton.isEnabled) {
                sendButton.isEnabled = true
            }
        }
        email.afterTextChanged {
            if (it.trim().isNotEmpty() && !sendButton.isEnabled) {
                sendButton.isEnabled = true
            }
        }
        confirmEmail.afterTextChanged {
            if (it.trim().isNotEmpty() && !sendButton.isEnabled) {
                sendButton.isEnabled = true
            }
        }
        sendButton.setOnClickListener {
            if (isValidData(name, email, confirmEmail)) {
                show(loadingBar)
                invitationViewModel.sendInvitation(
                    InvitationModel(
                        name = name.text.toString().trim(), //usedemail@blinq.app
                        email = email.text.toString().trim()
                    )
                )

            }
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    // function to open invite sent or canceled successfully dialog
    private fun openInviteOrCancelSuccessView(inRegister: Boolean) {
        val congratsBuilder = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.congratulation_view, null)
        val closeButton = view.findViewById<Button>(R.id.close_btn)
        val title = view.findViewById<TextView>(R.id.title_tv)
        val description = view.findViewById<TextView>(R.id.description_tv)
        val imageView = view.findViewById<ImageView>(R.id.image_view)
        Glide.with(this)
            .load(R.drawable.done)
            .into(imageView)
        if (inRegister) {
            title.text = getString(R.string.congrats_text)
            description.text = getString(R.string.invite_success_description)

        } else {
            title.text = getString(R.string.cancel_text)
            description.text = getString(R.string.cancel_success_description)
        }
        congratsBuilder.setView(view)
        closeButton.setOnClickListener {
            if (inRegister) {
                setCancelView()
            } else {
                setInviteView()
            }
            congratsBuilder.dismiss()
        }
        congratsBuilder.setCanceledOnTouchOutside(false)
        congratsBuilder.show()
        if(this::builder.isInitialized)
           builder.dismiss()
    }

    // function to set the send invite view
    private fun setInviteView() {
        binding.descriptionTv.text = getString(R.string.invite_description)
        binding.invitationBtn.text = getString(R.string.send_invite)
    }

    // function to set the cancel invite view
    private fun setCancelView() {
        binding.descriptionTv.text = getString(R.string.cancel_description)
        binding.invitationBtn.text = getString(R.string.cancel_invite)
    }


    companion object {
        const val TITLE = "Request an invitation"
        const val TAG = "LOG_TAG"
        const val INVITE_GIF =
            "https://media.tenor.com/0AVbKGY_MxMAAAAM/check-mark-verified.gif" ////https://media.tenor.com/kco6S8tl18cAAAAj/blob-cute.gif
        val PREF_NAME = "USER_PREF"
    }
}