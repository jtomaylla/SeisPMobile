package org.ses.android.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
 
public class ArithmeticService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }
    /**
     * IRemote defnition is available here
     */
    private final IRemote.Stub mBinder = new IRemote.Stub() {
 
        @Override
        public int add(int a, int b) throws RemoteException {
            // TODO Auto-generated method stub
            return (a + b);
        }
        @Override
        public String getFilterForms() throws RemoteException {
            // TODO Auto-generated method stub
            return ("DOTS_A1_V1/DOTS_B1_V1");
        }
    };
}
