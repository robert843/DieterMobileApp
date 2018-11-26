package pl.dieter.dietermobileapp.auth

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import pl.dieter.dietermobileapp.R
import pl.dieter.dietermobileapp.R.string.nav_header_title
import pl.dieter.dietermobileapp.auth.enum.ActionsEnum
import pl.dieter.dietermobileapp.auth.fragment.MenuFragment
import android.R.attr.fragment
import android.os.AsyncTask
import pl.dieter.dietermobileapp.auth.fragment.dummy.DummyContent
import pl.dieter.dietermobileapp.auth.task.RetrieveStatusTask
import pl.dieter.dietermobileapp.parser.DieterParser


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MenuFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val isLogged = RetrieveStatusTask(intent.getStringExtra(ActionsEnum.AUTH_TOKEN.name)).execute().get()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu_login.text = intent.getStringExtra(ActionsEnum.LOGIN.name)

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()


        when (item.itemId) {
            R.id.nav_camera -> {
                transaction.replace(R.id.main_content, MenuFragment())
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        transaction.commit()
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
