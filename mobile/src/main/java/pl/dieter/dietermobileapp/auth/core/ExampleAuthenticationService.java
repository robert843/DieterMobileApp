package pl.dieter.dietermobileapp.auth.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import pl.dieter.dietermobileapp.auth.authenticator.ExampleAccountAuthenticator;

public class ExampleAuthenticationService extends Service {
  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return new ExampleAccountAuthenticator(this).getIBinder();
  }
}