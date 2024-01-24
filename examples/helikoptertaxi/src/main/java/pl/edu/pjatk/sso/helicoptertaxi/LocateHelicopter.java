package pl.edu.pjatk.sso.helicoptertaxi;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public interface LocateHelicopter {
    Context findAllAvailableHelicopters(Context context) throws RemoteException, ContextException;
    Context findClosestFreeHelicopter(Context context) throws RemoteException, ContextException;
}