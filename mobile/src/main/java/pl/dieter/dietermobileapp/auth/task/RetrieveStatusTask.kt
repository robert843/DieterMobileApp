package pl.dieter.dietermobileapp.auth.task

import android.os.AsyncTask
import pl.dieter.dietermobileapp.auth.data.UserInfo
import pl.dieter.dietermobileapp.auth.enum.ActionsEnum
import pl.dieter.dietermobileapp.parser.DieterParser

class RetrieveStatusTask(val sessionID: String): AsyncTask<String, Void, UserInfo?>() {
    override fun doInBackground(vararg p0: String?): UserInfo? {
        var parser = DieterParser(sessionID)
        parser.getUserInfo()
        return parser.getUserInfo()
    }
}