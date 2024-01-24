package pl.edu.pjatk.sso.helicoptertaxi;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public interface FlightBook {
    Context create(Context context) throws RemoteException, ContextException;
    Context cancel(Context context) throws RemoteException, ContextException;
}