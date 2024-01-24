package pl.edu.pjatk.sso.helicoptertaxi;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public interface OrderConfirmation {
    Context confirm(Context context) throws RemoteException, ContextException;
    Context reject(Context context) throws RemoteException, ContextException;
}