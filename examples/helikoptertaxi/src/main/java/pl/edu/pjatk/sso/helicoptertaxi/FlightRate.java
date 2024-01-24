package pl.edu.pjatk.sso.helicoptertaxi;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public interface FlightRate {
    Context addRating(Context context) throws RemoteException, ContextException;
    Context modifyRating(Context context) throws RemoteException, ContextException;
}