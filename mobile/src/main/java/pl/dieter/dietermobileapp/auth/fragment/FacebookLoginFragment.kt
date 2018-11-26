package pl.dieter.dietermobileapp.auth.fragment

import android.net.Credentials
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.dieter.dietermobileapp.R

//1
class FacebookLoginFragment : Fragment() {

    //2
    companion object {

        fun newInstance(): FacebookLoginFragment {
            return FacebookLoginFragment()
        }
    }

    //3
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       /* FacebookOAuth.login(activity)
                .setClientId("241784899567697")
                .setClientSecret("9R38RZmnlXih5ZDXn9PGKu6wp9KUzhdU")
                .setAdditionalScopes("public_profile,email")
                .setRedirectUri("https://dieter.pl/login/facebook/check")
                .setCallback(object : OnLoginCallback {
                    override fun onSuccess(token: String, user: SocialUser) {
                        Log.d("Facebook Token", token)
                        Log.d("Facebook User", user.toString())
                    }

                    override fun onError(error: Exception) {
                        error.printStackTrace()
                    }
                })
                .init()*/


        return inflater?.inflate(R.layout.fragment_facebook_login, container, false)
    }

}