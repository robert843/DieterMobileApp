package pl.dieter.dietermobileapp.auth

import android.Manifest.permission.READ_CONTACTS
import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.LoaderManager.LoaderCallbacks
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_login.*
import pl.dieter.dietermobileapp.R
import pl.dieter.dietermobileapp.auth.data.UserInfo
import pl.dieter.dietermobileapp.auth.enum.ActionsEnum
import pl.dieter.dietermobileapp.auth.task.RetrieveStatusTask
import java.util.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    val accountType = "pl.dieter.dietermobileapp"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val accountManager = AccountManager.get(this)

        var account= accountManager.accounts.find { account -> account.type.equals(accountType) }
        if (account!=null){
            accountManager.getAuthToken(account, "sessionid", savedInstanceState,
                    this, resultCallback, null)
        }
        //CookieManager.getInstance().removeAllCookies {  cookie->true}
        //showLoginDialog()

        val mUserList = arrayListOf<String>()
        val accounts1 = accountManager.accounts
        /*val password =  accountManager.getPassword(
        accounts1
                .filter {
                    !mUserList.contains(it.name) &&
                            it.type.contains("pl.dieter.dietermobileapp")
                }.firstOrNull())
        System.out.println(password)*/
        // Set up the login form.
        /* populateAutoComplete()
         password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
             if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                 attemptLogin()
                 return@OnEditorActionListener true
             }
             false
         })

         email_sign_in_button.setOnClickListener { attemptLogin() }*/
    }

    private fun configureWebViewClient(loginWebview: WebView, dialog: MaterialDialog.Builder) {


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */



    fun registerUserInAccMan(login: String, token: String?) {
        val accountsInfo = ArrayList<String>()
        val accountManager = AccountManager.get(this)

        val accounts = accountManager.accounts
        for (account in accounts) {
            val name = account.name
            val type = account.type
            val describeContents = account.describeContents()
            val hashCode = account.hashCode()

            accountsInfo.add("name = " + name +
                    "\ntype = " + type +
                    "\ndescribeContents = " + describeContents +
                    "\nhashCode = " + hashCode)
        }
        val result = arrayOfNulls<String>(accountsInfo.size)
        accountsInfo.toTypedArray()



        // This is the magic that addes the account to the Android Account Manager
        val account = Account(login, accountType)
        accountManager.addAccountExplicitly(account, null,null)
        accountManager.setAuthToken(account,"sessionid",token)

        //manager.getAuthToken(account,"",null,this,null,null)
        //manager.addAccountExplicitly(account, password, null)

        // Now we tell our caller, could be the Andreid Account Manager or even our own application
        // that the process was successful
       /* val intent = Intent()
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType)
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, "token")

        this.setAccountAuthenticatorResult(intent.extras)
        this.setResult(Activity.RESULT_OK, intent)*/
    }

    fun showLoginDialog() {
        //CookieManager.getInstance().removeAllCookies {  true}
        CookieManager.getInstance().setAcceptCookie(true)
        var webview = WebView(this)

        var dialog = MaterialDialog.Builder(this)
                .customView(webview, true).show()

        webview.getSettings().setJavaScriptEnabled(true)

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return handleUrl(Uri.parse(url)) || super.shouldOverrideUrlLoading(view, url);
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                var uri = request?.getUrl() ?: Uri.parse("google.pl")
                return handleUrl(uri) || super.shouldOverrideUrlLoading(view, request)
            }

            private fun handleUrl(uri: Uri): Boolean {
                val redirectURL = "https://dieter.pl/#_=_"
                if (uri.toString().startsWith(redirectURL)) {
                    return true
                }
                return false
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                val cookies = CookieManager.getInstance().getCookie(url)
                if (url.equals("https://dieter.pl/#_=_")) {
                    val sessID = cookies.split(";").find { cookie -> cookie.contains("PHPSESSID") }?.replace("PHPSESSID=", "")
                    dialog.hide()
                    registerUserInAccMan("robert843",sessID)
                    startMainActivity("robert843",sessID)

                }
                super.onPageFinished(view, url)
            }
        }

        webview.loadUrl("https://dieter.pl/login")

    }

    fun startMainActivity(login:String, sessID: String?) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtra(ActionsEnum.AUTH_TOKEN.name, sessID)
            putExtra(ActionsEnum.LOGIN.name, login)
        }
        startActivity(intent)
    }

    val resultCallback = AccountManagerCallback<Bundle> { future ->
        //"https//dieter.pl/profil/user-info"
        val result = future?.result
        val token = result?.get(AccountManager.KEY_AUTHTOKEN) as String
        val name = result.get(AccountManager.KEY_ACCOUNT_NAME) as String
        val userInfo = RetrieveStatusTask(token).execute().get()

        if (userInfo!=null && userInfo.isLoggedIn){
            startMainActivity(name,token)
        } else {
            showLoginDialog()
        }
    }
}
