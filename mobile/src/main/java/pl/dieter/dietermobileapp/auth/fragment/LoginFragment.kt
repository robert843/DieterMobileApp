package pl.dieter.dietermobileapp.auth.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.dieter.dietermobileapp.R

//1
class LoginFragment : Fragment() {

    //2
    companion object {

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    //3
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_login, container, false)
    }

}